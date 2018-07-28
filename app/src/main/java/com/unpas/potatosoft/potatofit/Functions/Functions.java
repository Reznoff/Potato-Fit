package com.unpas.potatosoft.potatofit.Functions;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by fhblu on 4/13/2018.
 */

public class Functions {
    /*
    method method static yang di pakai, biar mudah di panggil
     */

    // method untuk ngeload gambar dari internet dan di inject ke aplikasi android
    public static void getImageFromUrl(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);
    }
}
