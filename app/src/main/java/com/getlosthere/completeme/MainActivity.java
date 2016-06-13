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
    ArrayList<Item> items;
    ItemsAdapter itemsAdapter;
    ListView lvItems;
    EditText etNewItem;
    int iNextItemId;
    private final int EDIT_ITEM_CODE = 20;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                //Item deleteItem = Item.load(Item.class, items.get(position).remoteId);
                //deleteItem.delete();
                //items.remove(position);
                Item updatedItem = Item.load(Item.class, items.get(position).remoteId);
                updatedItem.completed = true;
                updatedItem.save();
                items.set(position,updatedItem);
                sortItems();
                itemsAdapter.notifyDataSetChanged();
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
        i.putExtra("itemText",items.get(position).text);
        i.putExtra("position",position);
        i.putExtra("remoteId",items.get(position).remoteId);
        startActivityForResult(i,EDIT_ITEM_CODE);
    }

    public void populateArrayItems() {
        List<Item> queryResults = new Select().from(Item.class)
                .orderBy("Completed ASC").orderBy("Text ASC").limit(100).execute();

        iNextItemId = queryResults.size() + 1;
        items = new ArrayList<Item>();
        itemsAdapter = new ItemsAdapter(this, items);
        if (queryResults != null) {
            itemsAdapter.addAll(queryResults);
        }
    }

    public void onAddItem(View view) {
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Item newItem = new Item(iNextItemId, itemText);
        iNextItemId++;
        newItem.save();
        itemsAdapter.add(newItem);
        etNewItem.setText("");
        sortItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_CODE) {
            String itemText = data.getExtras().getString("itemText");
            int position = data.getExtras().getInt("position",0);
            long remoteId = data.getExtras().getLong("remoteId",0);
            Item updatedItem = Item.load(Item.class, remoteId);
            updatedItem.text = itemText;
            updatedItem.save();

            items.set(position,updatedItem);
            sortItems();

            itemsAdapter.notifyDataSetChanged();
        }
    }

    protected void sortItems(){
        Collections.sort(items, new Comparator<Item>() {
            public int compare(Item item1, Item item2) {
                boolean b1 = item1.completed;
                boolean b2 = item2.completed;
                if( b1 && ! b2 ) {
                    return +1;
                }
                if( ! b1 && b2 ) {
                    return -1;
                }
                return 0;
            }
        });
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
