//package com.example.weatherapp2;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.widget.TextView;
//
//import com.google.android.ads.nativetemplates.TemplateView;
//import com.google.android.gms.ads.nativead.NativeAd;
//
//public class ExitDialog extends Dialog
//{
//    NativeAd ad;
//    Activity activity;
//    public ExitDialog(Activity activity, NativeAd ad)
//    {
//        super(activity);
//        this.activity = activity;
//        this.ad = ad;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.exit_dialog);
//        TextView yes =findViewById(R.id.btn_yes);
//        TextView no =findViewById(R.id.btn_no);
//        yes.setOnClickListener(view -> {
//            activity.finish();
//            System.exit(0);
//        });
//        no.setOnClickListener(view -> dismiss());
//        TemplateView ad = findViewById(R.id.ad_template);
//        if(this.ad == null)
//        {
//            ad.setVisibility(View.GONE);
//        }
//        else
//        {
//            ad.setVisibility(View.VISIBLE);
//            ad.setNativeAd(this.ad);
//        }
//    }
//}
//
