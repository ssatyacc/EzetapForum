package ezetap.satya.ezetapform;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by Satya Chaitanya on 9/29/16.
 */

class EzetapForumData {
    private static final String TAG = "EzetapForumData";
    private static final String FORUM_URL = "http://d.eze.cc/mobileapps/test.json";
    private ForumDataListener mListener;

    EzetapForumData(ForumDataListener listener) {
        mListener = listener;
    }

    public void getForumData() {
        String url = FORUM_URL;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mListener.onFailure();
            }
        });
        EzetapForum.getInstance().addToRequestQueue(request);
    }

    interface ForumDataListener {

        void onSuccess(JSONObject response);

        void onFailure();
    }

}
