package com.flytrom.learning.activities.LiveVideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.flytrom.learning.R;
import com.flytrom.learning.adapters.LiveVideoAdapter;
import com.flytrom.learning.adapters.QBankCategoryAdapter;

public class LiveNewActivity extends AppCompatActivity {

    RecyclerView RVLiveVideos;
    LiveVideoAdapter liveVideoAdapter;
    ImageView image_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_new);

        RVLiveVideos = findViewById(R.id.RVLiveVideos);
        image_back = findViewById(R.id.image_back);


        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        liveVideoAdapter = new LiveVideoAdapter(this);
        RVLiveVideos.setHasFixedSize(true);
        RVLiveVideos.setLayoutManager(new LinearLayoutManager(this));
        RVLiveVideos.setAdapter(liveVideoAdapter);
    }
}