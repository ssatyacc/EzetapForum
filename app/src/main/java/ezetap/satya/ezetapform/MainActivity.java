package ezetap.satya.ezetapform;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EzetapForumData dataApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataApi = new EzetapForumData(new EzetapForumData.ForumDataListener() {
            @Override
            public void onSuccess(JSONObject response) {
                showForum(response);
            }

            @Override
            public void onFailure() {
                showRetry();
            }
        });

        dataApi.getForumData();
    }

    private void showForum(JSONObject response) {
        // TODO: 9/29/16 Create view based on response
    }

    private void showRetry() {
        findViewById(R.id.loading).setVisibility(View.GONE);
        findViewById(R.id.retry).setVisibility(View.VISIBLE);

        findViewById(R.id.retry).findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                dataApi.getForumData();
            }
        });
    }

    private void showLoading() {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        findViewById(R.id.retry).setVisibility(View.GONE);
    }
}
