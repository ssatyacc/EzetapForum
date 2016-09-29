package ezetap.satya.ezetapform;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Screen1 extends AppCompatActivity {
    private static final String TAG = "Screen1";

    private static final int DEFAULT_PADDING = 16;
    TreeMap<String, EditText> inputValues = new TreeMap<>();
    private Screen1Data dataApi;
    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);

        root = (LinearLayout) findViewById(R.id.root);
        dataApi = new Screen1Data(new Screen1Data.ForumDataListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    showForum(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    showRetry();
                }
            }

            @Override
            public void onFailure() {
                showRetry();
            }
        });

        dataApi.getForumData();
    }

    private void showForum(JSONObject response) throws JSONException {
        findViewById(R.id.loading).setVisibility(View.GONE);
        findViewById(R.id.retry).setVisibility(View.GONE);

        JSONArray items = response.getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);

            String itemType = item.optString("itemType", "");
            switch (itemType) {
                case "label":
                    String labelName = item.getString("name");
                    TextView labelTextView = new TextView(this);
                    labelTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    labelTextView.setText(labelName);
                    layout.addView(labelTextView);
                    break;
                case "textbox":
                    String textboxName = item.getString("name");
                    final String textboxInputType = item.getString("type");
                    int textboxMaxLength = item.optInt("maxlength", -1);
                    final int textboxMinValue = item.optInt("minValue", Integer.MIN_VALUE);
                    final int textboxMaxValue = item.optInt("maxValue", Integer.MAX_VALUE);

                    TextView textboxTextView = new TextView(this);
                    textboxTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textboxTextView.setText(textboxName);

                    final EditText textboxEditText = new EditText(this);
                    textboxEditText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    switch (textboxInputType) {
                        default:
                        case "text":
                            textboxEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                        case "phone":
                            textboxEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                            break;
                        case "number":
                            textboxEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                            break;
                    }

                    List<InputFilter> filters = new ArrayList<>();
                    if (textboxMaxLength != -1) {
                        filters.add(new InputFilter.LengthFilter(textboxMaxLength));
                    }

                    if (textboxEditText.getInputType() == InputType.TYPE_CLASS_NUMBER) {
                        textboxEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                // Do Nothing
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // Do Nothing
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String replaceString;
                                String inputNumber = textboxEditText.getText().toString();

                                if (!isInt(inputNumber) || Integer.valueOf(inputNumber) > textboxMaxValue) {
                                    replaceString = "" + textboxMaxValue;
                                    textboxEditText.setText(replaceString);
                                    textboxEditText.setSelection(replaceString.length());
                                } else if (Integer.valueOf(inputNumber) < textboxMinValue) {
                                    replaceString = "" + textboxMinValue;
                                    textboxEditText.setText(replaceString);
                                    textboxEditText.setSelection(replaceString.length());
                                }
                            }

                            private boolean isInt(String s) {
                                try {
                                    Integer.valueOf(s);
                                    return true;
                                } catch (Exception e) {
                                    return false;
                                }
                            }
                        });
                    }

                    InputFilter[] inputFilters = new InputFilter[filters.size()];
                    if (filters.size() > 0) {
                        textboxEditText.setFilters(filters.toArray(inputFilters));
                    }

                    layout.addView(textboxTextView);
                    layout.addView(textboxEditText);

                    inputValues.put(textboxName, textboxEditText);

                    break;
                case "dropdown":
                    String dropdownHint = item.getString("name");
                    JSONArray valuesArray = item.getJSONArray("values");

                    String[] dropdownValues = new String[valuesArray.length()];
                    for (int j = 0; j < valuesArray.length(); j++) {
                        dropdownValues[j] = valuesArray.getString(j);
                    }

                    TextView dropdownTextView = new TextView(this);
                    dropdownTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    dropdownTextView.setText(dropdownHint);

                    Spinner dropdownSpinner = new Spinner(this);
                    ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, dropdownValues);
                    dropdownSpinner.setAdapter(dropdownAdapter);

                    layout.addView(dropdownTextView);
                    layout.addView(dropdownSpinner);

                    break;
                case "button":
                default:
                    String buttonText = item.getString("name");
                    Button buttonButton = new Button(this);
                    buttonButton.setText(buttonText);

                    buttonButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            proceedToScreen2();
                        }
                    });
                    layout.addView(buttonButton);
                    break;
            }

            root.addView(layout);
        }
    }

    private void proceedToScreen2() {
        Screen2.start(this);
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
