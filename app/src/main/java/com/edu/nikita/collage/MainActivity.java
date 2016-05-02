package com.edu.nikita.collage;



import android.content.Intent;
import android.os.Bundle;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements FragmentChangedCallback{


    public final static String USERNAME_FRAGMENT ="usernameFragment";
    public final static String PHOTO_FRAGMENT = "photoFragment";
    public final static String COLLAGE_FRAGMENT = "collageFragment";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.container,new ChoseUsernameFragment(),USERNAME_FRAGMENT).commit();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Вызывается когда fragment закончил свою работу и должен быть заменен на другой
     * в зависимости от того какой фрагмент заканчивает свою работу в bundle будут разные данные
     * если завершается фрагмент с выбором username то bundle имеет отдну строчку
     */
    @Override
    public void changeFragment(Bundle bundle) {
        //Если есть user_id значит вызываем фрагмент с списком изображений для выбора
        String user_id = bundle.getString(USER_ID);
        if(user_id != null)
        {
            //Если userid в аргументах имеется значит запускаем фрагмент для выбора изображенийы
            MainActivityFragment fragment = new MainActivityFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment,PHOTO_FRAGMENT).commit();
        }
        else
        {
            //Пробуем получить список изображений если список есть запускаем фрагмент для создания и просмотра коллажа
            ArrayList<String> selected = bundle.getStringArrayList(SELECTED_IMAGES);
            if(selected != null && selected.size() > -1)
            {
                ViewCollageFragment fragment = new ViewCollageFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment,COLLAGE_FRAGMENT).commit();
            }
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                ViewCollageFragment fragment = (ViewCollageFragment) getSupportFragmentManager().findFragmentByTag(COLLAGE_FRAGMENT);
                if(fragment != null)
                    fragment.onResult(res);
    
            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                ViewCollageFragment fragment = (ViewCollageFragment) getSupportFragmentManager().findFragmentById(R.id.viewCollageParentView);
                if(fragment != null)
                    fragment.onError(error);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
