package com.edu.nikita.collage;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edu.nikita.collage.Table.CollageDB;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;
import com.vk.sdk.api.VKApiConst;

import java.util.ArrayList;


/**
 * Created by Nikita on 29.04.2016.
 * Фрагмент просмотра коллажа и отправки на стену в вк
 */
public class ViewCollageFragment extends Fragment implements View.OnClickListener,VKCallback<VKAccessToken>{

    public static final String [] scope = {VKScope.WALL,VKScope.PHOTOS};

    FragmentChangedCallback callback;
    ArrayList<String> photoIds;

    ImageView imageView;

    CollageMaker maker ;

    public ViewCollageFragment(){}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (FragmentChangedCallback) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.collage_view,container,false);
        rootView.findViewById(R.id.button3).setOnClickListener(this);
        imageView = (ImageView) rootView.findViewById(R.id.imageView2);
        maker = new CollageMaker(imageView);

        Bundle bumdle = getArguments();
        if(bumdle != null)
        {
            photoIds = bumdle.getStringArrayList(callback.SELECTED_IMAGES);
            makeCollage();
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button3) {
            if(!VKSdk.isLoggedIn())
                VKSdk.login(getActivity(), scope);
            else
                onResult();
        }
    }

    /**
     * Метод вызывается для создания коллажа из фотографий указаных в photoIds
     */
    public void makeCollage() {
        String selection = CollageDB.Columns.id + " = ?";
        String[] tmpS = new String[1];
        ArrayList<ImageModel> result = new ArrayList<>(photoIds.size());
        for (int i = 0; i < photoIds.size(); i++) {
            tmpS[0] = photoIds.get(i);
            Cursor cursor = getContext().getContentResolver().query(CollageDB.URI, null, selection, tmpS, null);
            if (cursor != null && cursor.moveToFirst()) {
                result.add(CollageDB.fromCursor(cursor));
                cursor.close();
            }

        }
        maker.ininiate(result);
        if (maker.getCollageWH().width > 0 && maker.getCollageWH().height > 0) {
            for (int index = 0; index < result.size(); index++) {
                Picasso.with(getContext()).load(result.get(index).getUrl()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        maker.onBitmapLoaded(bitmap,from);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                            maker.onBitmapFailed(null);
                        Snackbar.make((getActivity().findViewById(R.id.viewCollageParentView)), getString(R.string.failed_load_picasso), Snackbar.LENGTH_LONG).show();

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        }
    }


    @Override
    public void onResult(VKAccessToken res) {
        onResult();
    }

    private void onResult()
    {
        final Bitmap photo = maker.resultBitmap;
        VKRequest request = VKApi.uploadWallPhotoRequest(new VKUploadImage(photo, VKImageParameters.jpgImage(0.9f)), 0, 0);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d(this.getClass().getSimpleName(),response.responseString);
                VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
                makePost(new VKAttachments(photoModel),null);

            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Snackbar.make((getActivity().findViewById(R.id.viewCollageParentView)), error.errorMessage, Snackbar.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public void onError(VKError error) {

    }


    private void makePost(VKAttachments attachments, String message) {
        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.ATTACHMENTS, attachments, VKApiConst.MESSAGE, message));
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                if (isAdded()) {
                    Snackbar.make((getActivity().findViewById(R.id.viewCollageParentView)), getString(R.string.posted), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(VKError error) {
                Snackbar.make((getActivity().findViewById(R.id.viewCollageParentView)), error.errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
