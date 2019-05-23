package com.example.mingle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PlayerActivity extends AppCompatActivity {

    ServiceInterface serviceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        serviceInterface = new PlayerService();

        setWidget();
    }

    private void setWidget() {

        findViewById(R.id.btn_play).setOnClickListener(this::btnClick);
        findViewById(R.id.btn_pause).setOnClickListener(this::btnClick);
    }

    private void playMusic(String action) {
        Intent intent = new Intent(this, PlayerService.class);
        intent.setAction(action);
        startService(intent);

    }

    private void btnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                playMusic(PlayerService.ACTION_PLAY);
                break;
            case R.id.btn_pause:
                serviceInterface.pause();
                break;
            default: break;
        }
    }
}
