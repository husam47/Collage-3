package com.edu.nikita.collage;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.edu.nikita.collage.Loader.PhotoListLoader;
import com.edu.nikita.collage.Responses.BaseResponse;
import com.edu.nikita.collage.Table.CollageDB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Фрагмент для выбора фотографий
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<BaseResponse>  {

    public final static int MAX_PHOTO_QUERY = 33;

    private GridLayoutManager lLayout;

    //Переменные для id пользователя
    String userId = null;

    /**
     * Переменная для обратного вызова в активность-хост, уведомляет активность о том что фотографии выбраны
     */
    FragmentChangedCallback callback;

    RecyclerView mRecyclerView;
    PhotoRecyclerAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (FragmentChangedCallback) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new PhotoRecyclerAdapter();


        Bundle bundle = getArguments();
        userId = bundle.getString(callback.USER_ID);

        if (savedInstanceState != null) {
            //Пробуем получить данные которые сохраняется в OnSaveInstanceState
                ArrayList<String> recoverList = savedInstanceState.getStringArrayList(callback.SELECTED_IMAGES);
                if (recoverList != null) {
                    //Пробуем получить список из базы данных
                    Cursor cursor = getContext().getContentResolver().query(CollageDB.URI, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        adapter.setPhotosLinkList(CollageDB.listFromCursor(cursor));
                        adapter.setSelectedPhoto(recoverList);
                    }
                }
        }

        if (userId != null) {
            getLoaderManager().initLoader(R.id.photo_list_loader, null, this);
        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.photo_picker, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.photo_recycler);
        assert mRecyclerView != null;
        mRecyclerView.setAdapter(adapter);

        lLayout = new GridLayoutManager(getActivity(),4);
        mRecyclerView.setLayoutManager(lLayout);

        //Кнопка для получения коллажа
        rootView.findViewById(R.id.button2).setOnClickListener(this);
        return rootView;
    }

    @Override
    public Loader<BaseResponse> onCreateLoader(int id, Bundle args) {
        if(id == R.id.photo_list_loader)
            return new PhotoListLoader(getContext(),userId,getString(R.string.Client_id),MAX_PHOTO_QUERY);
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isNetworkConnected())
        {
            Snackbar.make((getActivity().findViewById(R.id.photo_picker_linearLayout)), getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoadFinished(Loader<BaseResponse> loader, BaseResponse data) {
        if(loader.getId() == R.id.photo_list_loader) {
            if (data.getRequestResult() == BaseResponse.RequestResult.SUCCESS) {//mAnswer== null когда аккаунт закрыт
                ArrayList<ImageModel> list = data.getTypedAnswer();
                adapter.setPhotosLinkList(list);
            }else
            {
                if(!isNetworkConnected()) {
                    Snackbar.make((getActivity().findViewById(R.id.photo_picker_linearLayout)), getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                }else {
                    Snackbar.make((getActivity().findViewById(R.id.photo_picker_linearLayout)), getString(R.string.failed_load_picasso), Snackbar.LENGTH_LONG).show();
                }

            }
        }
    }


    @Override
    public void onLoaderReset(Loader<BaseResponse> loader) {

    }

    @Override
    public void onClick(View v) {
        //Записываем список выбранных изображений в bundle и передаем в активити
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(callback.SELECTED_IMAGES,adapter.getSelectedPhoto());
        callback.changeFragment(bundle);
    }


    //Сохраняем данные на случай если фрагмент пересоздается
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Сохранить список выбранных изображений
        outState.putStringArrayList(callback.SELECTED_IMAGES,adapter.getSelectedPhoto());
    }

    public class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder> {

        /**
         * id выбранных изображений
         */
        private ArrayList<String> selectedPhoto = new ArrayList<>();

        /**
         * Данные для списка
         */
        private ArrayList<ImageModel> photosLinkList;

         public ArrayList<String> getSelectedPhoto() {
            return selectedPhoto;
        }


        public void setSelectedPhoto(ArrayList<String> selectedPhoto) {
            this.selectedPhoto = selectedPhoto;
            notifyDataSetChanged();
        }


        /**
         * Задать данные для адаптера, массив дожен быть отсортирован
         * @param photosLinkList список модели изображений
         */
        public void setPhotosLinkList(ArrayList<ImageModel> photosLinkList) {
            this.photosLinkList = photosLinkList;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_picker_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.model = photosLinkList.get(position);

            holder.imageUrl = holder.model.getUrl();
            //Если изображение в списке выбранных есть в списке то ставим checkBox в активное состояние
            holder.checkBox.setChecked(selectedPhoto.contains(holder.model.getId()));

            //Загружаем картинку
            Picasso.with(holder.image.getContext()).
                    load(holder.imageUrl).resize(200, 200)
                    .centerCrop().into(holder.image);

        }

        @Override
        public int getItemCount() {
            if (photosLinkList == null)
                return 0;
            else
                return photosLinkList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
            ImageModel model;
            String imageUrl;
            ImageView image;
            CheckBox checkBox;

            public ViewHolder(View view) {
                super(view);
                image = (ImageView) view.findViewById(R.id.imageView);
                checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                checkBox.setOnCheckedChangeListener(this);
            }


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Если пользователь ставит галочку и id этого изображения нет в нашем списке то добавляем егов список
                if (isChecked && !selectedPhoto.contains(model.getId())) {
                    selectedPhoto.add(model.getId());
                }
                // Если пользователь убирает галочку удаляем id из списка выбранных
                else if (!isChecked && selectedPhoto.contains(model.getId())) {
                    selectedPhoto.remove(model.getId());
                }
            }
        }
    }

    /**
     * GПроверка наличия сети
     * @return true если сеть есть false если нет
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }
}
