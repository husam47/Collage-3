package com.edu.nikita.collage;



import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.edu.nikita.collage.AutoCpmleteView.DelayAutoCompleteAdapter;
import com.edu.nikita.collage.AutoCpmleteView.DelayAutoCompleteTextView;
import com.edu.nikita.collage.Pojo.TokenResponse;
import com.edu.nikita.collage.Retrofit.ApiFactory;
import com.edu.nikita.collage.Retrofit.GetAccessToken;
import com.edu.nikita.collage.Retrofit.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * fragment для выбора логина, активити-хост(или любой другой обьект управляющий активностью) должен реализовать интерфейс FragmentChangedCallback
 * при нажатии на кнопку вызывается метод changeFragment интерфейса FragmentChangedCallback
 * с Bundle где содержится единственная запись- id пользователя
 */

public class ChoseUsernameFragment extends Fragment implements View.OnClickListener, Callback<TokenResponse> {


    public static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences preferences;

    AuthDialog dlg;
    private boolean takingAccessToken = false;


    /**
     * Выпадающее меню с подсказками
     */
    private DelayAutoCompleteAdapter autoCompleteAdapter;
    private DelayAutoCompleteTextView usersChose;

    /**
     * Обьект обрабатывающий собитие завершение работы фрагмента
     */
    private FragmentChangedCallback callback;


    public ChoseUsernameFragment(){
        super();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (FragmentChangedCallback) context;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        preferences = getContext().getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);

        View rootView = inflater.inflate(R.layout.fragment_chose_username,container,false);
        //Настраиваем подсказки
        usersChose = (DelayAutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView);
        usersChose.setThreshold(2);
        String access_token = "";
        if ( preferences.contains(getString(R.string.access_token)) )
        {
            access_token = preferences.getString(getString(R.string.access_token),"");
        }
        autoCompleteAdapter = new DelayAutoCompleteAdapter(getContext(),access_token,this);
        usersChose.setAdapter(autoCompleteAdapter);
        usersChose.setLoadingIndicator((ProgressBar) rootView.findViewById(R.id.progressBar));
        rootView.findViewById(R.id.button).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();




        //AuthDialog dlg = new AuthDialog(getContext());
        //dlg.show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            if(!isNetworkConnected())
                Snackbar.make((getActivity().findViewById(R.id.chose_username_parent)), getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            //При нажатии кнопки мы сохраняем результат в bundle и передаем обработчику
            Bundle data = new Bundle();
            User userId = autoCompleteAdapter.getUser(usersChose.getText().toString());
            if(userId != null)//Равен нулю есть нет интернет соединения
            data.putString(callback.USER_ID, userId.getId());
            callback.changeFragment(data);
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


    public void accessTokenNotValid()
    {
        if(isNetworkConnected() && !takingAccessToken){
            takingAccessToken = true;
            dlg = new AuthDialog(getContext(),this);
            dlg.show();
        }else
            Snackbar.make((getActivity().findViewById(R.id.chose_username_parent)), getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();

    }


        @Override
        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
            SharedPreferences preferences = getContext().getSharedPreferences(ChoseUsernameFragment.APP_PREFERENCES,Context.MODE_PRIVATE);
            String str = response.body().accessToken;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(getContext().getString(R.string.access_token),str);
            editor.apply();
            autoCompleteAdapter.setAccessToken(str);
            dlg.closeDialog();
        }

        @Override
        public void onFailure(Call<TokenResponse> call, Throwable t) {
            Snackbar.make(getActivity().findViewById(R.id.chose_username_parent), t.getMessage(), Snackbar.LENGTH_LONG).show();
            dlg.closeDialog();
        }

    public void authDialogDismis()
    {
        takingAccessToken = false;
    }
}
