package cn.lankton.anyshape.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    int colors[] = {Color.BLUE, Color.WHITE, Color.YELLOW, Color.LTGRAY, Color.RED, Color.GREEN, Color.CYAN};
    View root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.root);
        findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int)(colors.length * Math.random());
                root.setBackgroundColor(colors[index]);
            }
        });
    }
}
