package com.edu.nikita.collage.AutoCpmleteView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

/**
 * Кастомный AutoCompleteTextView, запрашивает даные у адаптера если пользователь не менял текст
 * в течении DEFAULT_AUTOCOMPLETE_DELAY секунд
 */
public class DelayAutoCompleteTextView extends AutoCompleteTextView {


    private static final int MESSAGE_TEXT_CHANGED = 100;
    /**
     * Время в течении которого изменения не учитываются,
     * если в течении этоо времени пользователь не изменил текст то будуд запрошены данные из адаптера
     */
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;

    private int mAutoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;

    /**
     * Индикатор загрузки, отобрается пока выполняется запрос за данными в адаптер
     */
    private ProgressBar mLoadingIndicator;

    /**
     * Handler используется для отложки времени выполнения запроса к адаптеру
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DelayAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    };

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Задать ProgressBar, показывается во время запроса к адаптеру
     * @param progressBar
     */
    public void setLoadingIndicator(ProgressBar progressBar) {
        mLoadingIndicator = progressBar;
    }

    /**
     * Задать время в течении которого изменения текста не учитывается
     * @param autoCompleteDelay
     */
    public void setAutoCompleteDelay(int autoCompleteDelay) {
        mAutoCompleteDelay = autoCompleteDelay;
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), mAutoCompleteDelay);
    }

    @Override
    public void onFilterComplete(int count) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.GONE);
        }
        super.onFilterComplete(count);
    }
}
