package com.getlosthere.completeme;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<Object> items;
    ItemsAdapter itemsAdapter;
    ListView lvItems;
    EditText etNewItem;
    private final int EDIT_ITEM_CODE = 20;
    Item currentItem;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<Object>();
        itemsAdapter = new ItemsAdapter(this, items);

        populateArrayItems();

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View item, int position, long id) {
                if (items.get(position) instanceof Item) {
                    currentItem = (Item) items.get(position);
                    Item updatedItem = Item.load(Item.class, currentItem.getId());
                    updatedItem.completed = !updatedItem.completed;
                    updatedItem.save();
                    populateArrayItems();
                    itemsAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchEditItemView(position);
            }
        });
    }

    public void launchEditItemView(int position) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        currentItem = (Item) items.get(position);
        i.putExtra("itemText",currentItem.text);
        i.putExtra("position",position);
        i.putExtra("itemId",currentItem.getId());
        startActivityForResult(i,EDIT_ITEM_CODE);
    }

    public void populateArrayItems() {
        List<Item> queryResults = new Select().from(Item.class)
                .orderBy("Completed ASC").limit(100).execute();

//        items = new ArrayList<Object>();
//        itemsAdapter = new ItemsAdapter(this, items);
        itemsAdapter.clear();

        boolean previousCompleted = false;
        for(int i = 0; i < queryResults.size(); i++){
            if(queryResults.get(i).completed != previousCompleted){
                itemsAdapter.add(new SectionHeader("Completed Items"));
                previousCompleted =queryResults.get(i).completed;
                itemsAdapter.add(queryResults.get(i));
            }
            else{
                itemsAdapter.add(queryResults.get(i));
            }
        }
//        itemsAdapter.addAll(queryResults);

        itemsAdapter.notifyDataSetChanged();
    }

    public void onAddItem(View view) {
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        if(etNewItem != null) {
            String itemText = etNewItem.getText().toString();
            Item newItem = new Item(itemText);
            newItem.save();
            populateArrayItems();
            etNewItem.setText("");
        }
    }


    public void onClearCompleted(View view){
        new Delete().from(Item.class).where("Completed = ?",true).execute();
        itemsAdapter.clear();
        populateArrayItems();
        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_CODE) {
            String itemText = data.getExtras().getString("itemText");
            int position = data.getExtras().getInt("position",0);
            long itemId = data.getExtras().getLong("itemId",0);
            Item updatedItem = Item.load(Item.class, itemId);
            updatedItem.text = itemText;
            updatedItem.save();

            items.set(position,updatedItem);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.getlosthere.completeme/http/host/path")
        );
        AppIndex.AppIndexApi.start(mClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.getlosthere.completeme/http/host/path")
        );
        AppIndex.AppIndexApi.end(mClient, viewAction);
        mClient.disconnect();
    }
}
