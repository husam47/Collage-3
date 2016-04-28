package com.edu.nikita.collage;



import android.os.Bundle;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.edu.nikita.collage.Loader.PhotoListLoader;
import com.edu.nikita.collage.Responses.BaseResponse;
import com.edu.nikita.collage.Responses.ResponseSearchUser;
import com.edu.nikita.collage.Retrofit.ApiFactory;
import com.edu.nikita.collage.Retrofit.UserSearch;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<BaseResponse> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }

    @Override
    protected void onStart() {
        super.onStart();
        //
        android.support.design.widget.FloatingActionButton btn = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);
        btn.setOnClickListener(this);
        //
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





    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.fab)
            getSupportLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<BaseResponse> onCreateLoader(int id, Bundle args) {
        return new PhotoListLoader(this,"13460080",getString(R.string.Client_id),33);
    }

    @Override
    public void onLoadFinished(Loader<BaseResponse> loader, BaseResponse data) {

    }

    @Override
    public void onLoaderReset(Loader<BaseResponse> loader) {

    }
}
