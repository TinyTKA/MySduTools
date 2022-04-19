package xyz.taouvw.mysdutools.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.nfc.TagLostException;
import android.os.Bundle;
import android.widget.TextView;

import xyz.taouvw.mysdutools.Adapter.MoreOptionAdapter;
import xyz.taouvw.mysdutools.R;
import xyz.taouvw.mysdutools.utils.PropertiesUtils;

public class AboutActivity extends AppCompatActivity {
    RecyclerView moreOption;
    TextView nowVersion;
    PropertiesUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
    }

    private void init() {
        nowVersion = this.findViewById(R.id.nowVersion);
        utils = PropertiesUtils.getInstance(AboutActivity.this, "values.properties");
        utils.init();
        String nowVersionCode = utils.readString("nowVersion", "1.0");
        nowVersion.setText("当前版本:v" + nowVersionCode);
        moreOption = this.findViewById(R.id.moreOption);
        MoreOptionAdapter moreOptionAdapter = new MoreOptionAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        moreOption.setLayoutManager(linearLayoutManager);
        moreOption.setAdapter(moreOptionAdapter);
        moreOption.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}