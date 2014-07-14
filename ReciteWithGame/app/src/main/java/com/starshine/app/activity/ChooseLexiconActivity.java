package com.starshine.app.activity;

import android.widget.ListView;

import com.starshine.app.R;
import com.starshine.app.adapter.LexiconAdapter;
import com.starshine.app.model.Lexicon;

import java.util.ArrayList;
import java.util.List;

public class ChooseLexiconActivity extends BaseActivity {
    private ListView mLexiconListView;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected int setDrawableId() {
        return R.layout.activity_choose_lexicon;
    }

    @Override
    protected void initContentView() {
        mLexiconListView = (ListView) findViewById(R.id.lv_lexicon);
    }

    @Override
    protected void initData() {
        LexiconAdapter adapter = new LexiconAdapter(this);
        List<Lexicon> lexiconList = new ArrayList<Lexicon>();
        String[] lexicons = getResources().getStringArray(R.array.lexicon);
        String[] tableName = getResources().getStringArray(R.array.table_name);
        for (int i = 0;i < lexicons.length; i++){
            Lexicon lexicon = new Lexicon();
            lexicon.setCode(i);
            lexicon.setName(lexicons[i]);
            lexicon.setTableName(tableName[i]);
            lexiconList.add(lexicon);
        }
        adapter.setData(lexiconList);
        mLexiconListView.setAdapter(adapter);
    }
}
