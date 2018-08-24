package sahityadiary.hindinetex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ShortNoteDetailsActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView textView;
    private Intent intent;
    private String selectedTopicId;
    private int numberOfColumns;
    private int numberOfRows;
    private int tableDataCounter = 0;
    private ArrayList<String> tableData;
    private String selectedTopicTableSize;
    private AdView mAdView;
    InterstitialAd mInterstitialAd;
    AdRequest adRequest2;
    private static final String topicUrl = "https://sheets.googleapis.com/v4/spreadsheets/14c072l7A6mYt3lM61zAI5YGj4bmdvoQA_nSAq4SWUcM/values/SHORT%20NOTES%20TABLE%20CONTENT!A:N?key=AIzaSyAHRy3U0-UXRMxVJ09ubzT66KCmPeKkC98";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_note_details);
        intent = getIntent();
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        textView = (TextView) findViewById(R.id.quiz_no_internet);
        progressBar = (ProgressBar) findViewById(R.id.quiz_progress_bar);
        selectedTopicId = intent.getStringExtra("selectedTopicId");
        selectedTopicTableSize = intent.getStringExtra("selectedTopicTableSize");
        //String string = selectedTopicTableSize.replaceAll("\\*", "x");
        String[] numbers = selectedTopicTableSize.split("\\*");
        numberOfColumns = Integer.valueOf(numbers[0]);
        numberOfRows = Integer.valueOf(numbers[1]);
        tableData = new ArrayList<>();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new TopicAsyncTask().execute(topicUrl);
        } else {
            textView.setText("No internet connection");
            textView.setVisibility(View.VISIBLE);
        }
        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial2));

        adRequest2 = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest2);
    }

    private class TopicAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            URL url;
            HttpURLConnection conn = null;
            InputStream in = null;
            try {
                url = new URL(stringUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");

                conn.connect();
                in = conn.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            super.onPostExecute(jsonResponse);
            progressBar.setVisibility(View.INVISIBLE);
            if (jsonResponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("values");
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                        String topicId = jsonArray1.getString(0);
                        if (topicId.equals(selectedTopicId)) {
                            for (int j = 0, k = 4; j < numberOfColumns; j++, k++) {
                                tableData.add(jsonArray1.getString(k));
                            }
                        }
                    }
                    TableLayout tableLayout = new TableLayout(ShortNoteDetailsActivity.this);
                    tableLayout.setStretchAllColumns(true);
                    for (int i = 0; i < numberOfRows; i++) {
                        TableRow row = new TableRow(ShortNoteDetailsActivity.this);
                        row.setBackgroundResource(R.drawable.border);
                        for (int j = 0; j < numberOfColumns; j++) {
                            TextView tv = new TextView(ShortNoteDetailsActivity.this);
                            tv.setText(tableData.get(tableDataCounter));
                            tv.setGravity(Gravity.CENTER);
                            tv.setPadding(10, 10, 10, 10);
                            tv.setTextSize(23);
                            tv.setTextColor(Color.BLACK);
                            tv.setBackgroundResource(R.drawable.border);
                            row.addView(tv);
                            tableDataCounter++;
                        }
                        tableLayout.addView(row);
                    }
                    RelativeLayout mainLayout = findViewById(R.id.parent);
                    mainLayout.addView(tableLayout);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                textView.setText("Error");
                textView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        if (mInterstitialAd.isLoaded()) {
            showInterstitial();
        }
        super.onDestroy();
    }

}
