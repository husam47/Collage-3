package com.edu.nikita.collage.AutoCpmleteView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nikita.collage.R;
import com.edu.nikita.collage.Retrofit.ResponseSearchUser;
import com.edu.nikita.collage.Retrofit.User;
import com.edu.nikita.collage.Retrofit.ApiFactory;
import com.edu.nikita.collage.Retrofit.UserSearch;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Nikita on 29.04.2016.
 * Адаптер для AutoCompleteTextView при вводе имени пользователя выполняет запрос в Instagram api,
 * получает список пользователей с похожим ником
 */
public class DelayAutoCompleteAdapter extends BaseAdapter implements Filterable {


    /**
     * Ссылка на контекст
     */
    private final Context mContext;
    /**
     *  результат полученый от сервера, используется для выпадающего меню "подсказок"
     */
    private List<User> mResults;
    /**
     * id приложения для instagram
     */
    private String client;



    public DelayAutoCompleteAdapter(Context context, @NonNull String client_) {
        mContext = context;
        mResults = new ArrayList<User>();
        client = client_;
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public User getItem(int index) {
        return mResults.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.user_in_autocomplete, parent, false);
        }
        User user = getItem(position);
        ((TextView) convertView.findViewById(R.id.username)).setText(user.getUsername());
        //загрузка аватарки пользователя при выводе подсказок
        Picasso.with(mContext).
                load(user.getImageUrl()).resize(100,100)
                .centerCrop().into(((ImageView) convertView.findViewById(R.id.avatar)));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new Filter.FilterResults();
                ResponseSearchUser users = null;
                if (constraint != null) {
                    try {
                        UserSearch service = ApiFactory.getUserSearch();
                        //Формируем запрос
                        Call<ResponseSearchUser> call = service.search(String.valueOf(constraint), client);
                        //Получаем ответ
                        users = call.execute().body();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (users != null) {
                        filterResults.values = users.getUsers();
                        filterResults.count = users.getUsers().size();
                    } else {
                        filterResults.values = null;
                        filterResults.count = 0;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    mResults = (List<User>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};

        return filter;
    }

    /**
     * Метод для получения обьекта User из списка пользователей по имени
     * @param username имя пользователя
     * @return объект User для запрашиваемого имени пользователя
     */
    public User getUser(String username)
    {
        User tmp = new User(username);
        int index = mResults.indexOf(tmp);
        if(index != -1)
            return mResults.get(index);

        return null;
    }
}