package com.GGI.crossfire.android;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.GGI.crossfire.Crossfire;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;



public class AndroidLauncher extends AndroidApplication {

  private static final String AD_UNIT_ID = "ca-app-pub-4179095773889612/5163959689";
  private static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/developer?id=Good+Game+Industries";
  protected AdView adView;
  protected View gameView;
  private Timer timer;
  private AndroidLauncher launch;
  private boolean isPaused = false;
  
  final static String APP_ID  = "app56bc6285cf7540509c";
  final static String ZONE_ID = "vz52893f7879cd47a5a9";
  
  private int t = 4001;
	  
  
 
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
    cfg.useAccelerometer = false;
    cfg.useCompass = false;
    launch = this;
    
    
    
    

    // Do the stuff that initialize() would do for you
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

    RelativeLayout layout = new RelativeLayout(this);
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    layout.setLayoutParams(params);

    AdView admobView = createAdView();
    layout.addView(admobView);
    View gameView = createGameView(cfg);
    layout.addView(gameView);

    setContentView(layout);
    startAdvertising(admobView);
  }

  private AdView createAdView() {
    adView = new AdView(this);
    adView.setAdSize(AdSize.SMART_BANNER);
    adView.setAdUnitId(AD_UNIT_ID);
    adView.setId(12345); // this is an arbitrary id, allows for relative positioning in createGameView()
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
    params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
    adView.setLayoutParams(params);
    adView.setBackgroundColor(Color.BLACK);
    return adView;
  }

  private View createGameView(AndroidApplicationConfiguration cfg) {
    gameView = initializeForView(new Crossfire(), cfg);
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
    params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
    params.addRule(RelativeLayout.ABOVE, adView.getId());
    gameView.setLayoutParams(params);
    return gameView;
  }

  private void startAdvertising(AdView adView) {
    AdRequest adRequest = new AdRequest.Builder().build();
    adView.loadAd(adRequest);
    
  }
  
  @Override
  public void onResume() {
    super.onResume();
    if (adView != null) adView.resume();
    isPaused = false;
   
  }

  @Override
  public void onPause() {
	timer = null;
	isPaused = true;
    if (adView != null) adView.pause();
    super.onPause();
    
  }

  @Override
  public void onDestroy() {
    if (adView != null) adView.destroy();
    timer = null;
    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
    final Dialog dialog = new Dialog(this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

    LinearLayout ll = new LinearLayout(this);
    ll.setOrientation(LinearLayout.VERTICAL);

    Button b1 = new Button(this);
    b1.setText("Quit");
    b1.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        finish();
      }
    });
    ll.addView(b1);

    Button b2 = new Button(this);
    b2.setText("RGB");
    b2.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL)));
        dialog.dismiss();
      }
    });
    ll.addView(b2);

    dialog.setContentView(ll);
    dialog.show();
  }
  
  
}