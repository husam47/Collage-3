package com.edu.nikita.collage;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.edu.nikita.collage.AutoCpmleteView.DelayAutoCompleteAdapter;
import com.edu.nikita.collage.AutoCpmleteView.DelayAutoCompleteTextView;
import com.edu.nikita.collage.Retrofit.User;

/**
 * fragment для выбора логина, активити-хост(или любой другой обьект управляющий активностью) должен реализовать интерфейс FragmentChangedCallback
 * при нажатии на кнопку вызывается метод changeFragment интерфейса FragmentChangedCallback
 * с Bundle где содержится единственная запись- id пользователя
 */

public class ChoseUsernameFragment extends Fragment implements View.OnClickListener {


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
       View rootView = inflater.inflate(R.layout.fragment_chose_username,container,false);
        //Настраиваем подсказки
        usersChose = (DelayAutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView);
        usersChose.setThreshold(2);
        autoCompleteAdapter = new DelayAutoCompleteAdapter(getContext(),getString(R.string.Client_id));
        usersChose.setAdapter(autoCompleteAdapter);
        usersChose.setLoadingIndicator((ProgressBar) rootView.findViewById(R.id.progressBar));
        rootView.findViewById(R.id.button).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            //При нажатии кнопки мы сохраняем результат в bundle и передаем обработчику
            Bundle data = new Bundle();
            User userId = autoCompleteAdapter.getUser(usersChose.getText().toString());
            if(userId != null)
            data.putString(callback.USER_ID, userId.getId());
            callback.changeFragment(data);
        }
    }
}
