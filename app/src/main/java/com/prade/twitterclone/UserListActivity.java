package com.prade.twitterclone;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        if(ParseUser.getCurrentUser().get("isFollowing") == null) {
            List<String> emptyList =new ArrayList<>();
            ParseUser.getCurrentUser().put("isFollowing",emptyList);
        }

        setTitle("User List");
        final ListView listView=(ListView)findViewById(R.id.listViewUser);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_checked,users);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                if (checkedTextView.isChecked()) {
                    Log.i("Info", "checked");
                    ParseUser.getCurrentUser().getList("isFollowing").add(users.get(i));
                    ParseUser.getCurrentUser().saveInBackground();
                } else {
                    Log.i("Info", "not checked");
                    ParseUser.getCurrentUser().getList("isFollowing").remove(users.get(i));
                    ParseUser.getCurrentUser().saveInBackground();
                }
            }

        });

        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null){
                    if(objects.size()>0){
                        for(ParseUser user:objects){ users.add(user.getUsername());}
                        arrayAdapter.notifyDataSetChanged();
                        for(String username: users) {
                            if(ParseUser.getCurrentUser().getList("isFollowing").contains(username)){
                                listView.setItemChecked(users.indexOf(username),true);
                            }
                        }
                    }
                }
                else {}
            }
        });

        }

}
