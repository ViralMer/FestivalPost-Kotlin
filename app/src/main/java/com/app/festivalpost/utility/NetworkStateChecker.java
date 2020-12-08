package com.app.festivalpost.utility;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.app.festivalpost.R;
import com.app.festivalpost.activity.HomeActivity;



public class NetworkStateChecker extends BroadcastReceiver {

    //context and database helper object
    private Context context;



    @Override
    public void onReceive(final Context context, Intent intent) {

        this.context = context;



        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {



            }
        } else {
            final Dialog dialog = new Dialog(context,
                    R.style.DialogAnimation);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.view_no_internet);
            dialog.setCancelable(true);

            Button button=dialog.findViewById(R.id.btnTryAgain);



            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, HomeActivity.class));
                    dialog.dismiss();
                }
            });


            dialog.show();
        }
    }





}