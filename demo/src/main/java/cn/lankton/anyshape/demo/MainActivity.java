package cn.lankton.anyshape.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.lankton.anyshape.PathManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Integer> ids = new ArrayList<>();
        ids.add(R.drawable.singlestar);
        ids.add(R.drawable.rings);
        ids.add(R.drawable.text);
        /*
        You can also use PathManager.getInstance().createPaths(this, ids);
        If so, the default limit will be used for avoiding OOM
        the unit of the limit is px.
        */
        PathManager.getInstance().createPaths(this, ids, 1000, 1000);
        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
