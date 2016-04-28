package com.edu.nikita.collage.Loader;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.edu.nikita.collage.Responses.BaseResponse;

import java.io.IOException;

/**
 * Created by Nikita on 27.04.2016.
 */
public abstract class BaseLoader extends AsyncTaskLoader<BaseResponse> {

    public BaseLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public BaseResponse loadInBackground() {
        try {
            BaseResponse response = apiCall();
            if (response.getRequestResult() == BaseResponse.RequestResult.SUCCESS) {
                response.save(getContext());
                onSuccess();
            } else {
                onError();
            }
            return response;
        } catch (IOException e) {
            onError();
            return new BaseResponse();
        }
    }

    protected void onSuccess() {
    }

    protected void onError() {
    }

    protected abstract BaseResponse apiCall() throws IOException;
}