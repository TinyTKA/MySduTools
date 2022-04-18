package xyz.taouvw.mysdutools.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import xyz.taouvw.mysdutools.Adapter.MoreOptionAdapter;
import xyz.taouvw.mysdutools.R;

public class AboutActivity extends AppCompatActivity {
    RecyclerView moreOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
    }

    private void init() {
        moreOption = this.findViewById(R.id.moreOption);
        MoreOptionAdapter moreOptionAdapter = new MoreOptionAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        moreOption.setLayoutManager(linearLayoutManager);
        moreOption.setAdapter(moreOptionAdapter);
        moreOption.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }
}