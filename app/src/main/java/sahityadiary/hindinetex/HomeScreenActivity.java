package sahityadiary.hindinetex;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class HomeScreenActivity extends AppCompatActivity {

    GridView gridView;
    private AdView mAdView;

    String[] values = {

            "Notes",
            "MCQ",
            "Short Notes",
            "More Apps",
            "Rate Us",
            "Share",


    };

    int[] images = {
            R.drawable.notes,
            R.drawable.ic_assignment_black_24dp,
            R.drawable.pointstoremember,
            R.drawable.moreapps,
            R.drawable.likeus,
            R.drawable.share,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setTitle("Welcome");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        gridView = (GridView) findViewById(R.id.griview);
        GridAdapter gridAdapter = new GridAdapter(HomeScreenActivity.this, values, images);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent intent3 = new Intent(HomeScreenActivity.this, NotesActivity.class);
                        startActivity(intent3);
                        break;
                    case 1:
                        Intent intent = new Intent(HomeScreenActivity.this, MCQActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent intent2 = new Intent(HomeScreenActivity.this, ShortNoteActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        String devName = "SAHITYA DIARY";
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://store/apps/developer?id=" + devName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=" + devName)));
                        }
                        break;
                    case 4:
                        final String appPackageName = getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        break;
                    case 5:
                        ShareCompat.IntentBuilder
                                .from(HomeScreenActivity.this)
                                .setType("text/plain")
                                .setChooserTitle("Share Via")
                                .setText("I FOUND AN AWSUM APP NAME \"HINDI NETEX\"\n" +
                                        "MUST DOWNLOAD\n"+"https://play.google.com/store/apps/details?id=sahityadiary.hindinetex")
                                .startChooser();
                        break;
                }
            }
        });
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
        super.onDestroy();
    }
}
