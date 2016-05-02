package com.edu.nikita.collage;

import android.os.Bundle;

/**
 * Created by Nikita on 29.04.2016.
 */
public interface FragmentChangedCallback {

    String SELECTED_IMAGES = "SELECTED";
    String USER_ID = "user_id";

    void changeFragment(Bundle bundle);
}
