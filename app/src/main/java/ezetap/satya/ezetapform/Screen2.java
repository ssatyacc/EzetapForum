package ezetap.satya.ezetapform;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedHashMap;

public class Screen2 extends AppCompatActivity {
    private static final LinkedHashMap<String, String> inputValues = new LinkedHashMap<>();
    private static final String TAG = "Screen2";
    private static final int DEFAULT_PADDING = 16;

    // I know that this can be done by passing Intents. This is done on purpose.
    public static void start(Context context, LinkedHashMap<String, String> inputs) {
        Intent starter = new Intent(context, Screen2.class);

        inputValues.clear();
        for (String s : inputs.keySet()) {
            inputValues.put(s, inputs.get(s));
        }
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);

        LinearLayout root = (LinearLayout) findViewById(R.id.root);

        findViewById(R.id.loading).setVisibility(View.GONE);
        findViewById(R.id.retry).setVisibility(View.GONE);

        for (String s : inputValues.keySet()) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);

            TextView textViewName = new TextView(this);
            TextView textViewValue = new TextView(this);

            textViewName.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            textViewName.setText(s + " : ");

            textViewValue.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            textViewValue.setText(inputValues.get(s));

            layout.addView(textViewName);
            layout.addView(textViewValue);
            root.addView(layout);
        }
    }
}
