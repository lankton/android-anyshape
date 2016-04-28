package cn.lankton.anyshape.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.lankton.anyshape.AnyshapeImageView;

public class SecondActivity extends AppCompatActivity {

    int colors[] = {Color.BLUE, Color.WHITE, Color.YELLOW, Color.LTGRAY, Color.RED, Color.GREEN, Color.CYAN};
    View root;
    AnyshapeImageView iv_rings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        root = findViewById(R.id.root);
        iv_rings = (AnyshapeImageView) findViewById(R.id.iv_rings);
        findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int)(colors.length * Math.random());
                iv_rings.setBackColor(colors[index]);
            }
        });
    }
}
