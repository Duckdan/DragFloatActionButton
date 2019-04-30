package study.yang.dragfloatbuttonaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DragFloatActionButton button = (DragFloatActionButton) findViewById(R.id.dfab);
        button.setOnClickListener(new DragFloatActionButton.OnClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(MainActivity.this, "悬浮按钮被点击了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
