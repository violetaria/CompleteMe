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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Item> items;
    ItemsAdapter itemsAdapter;
    ListView lvItems;
    EditText etNewItem;
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
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
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
        startActivityForResult(i,EDIT_ITEM_CODE);
    }

    private void readItems() {
        File fileDir = getFilesDir();
        File itemFile = new File(fileDir, "item.txt");
        try {
            ArrayList<String> inputData = new ArrayList<String>(FileUtils.readLines(itemFile));
            Item tempItem;
            items = new ArrayList<Item>();
            for(int i = 0; i< inputData.size(); i ++){
                tempItem = new Item(inputData.get(i));
                items.add(i,tempItem);
            }
        } catch (IOException e) {
            items = new ArrayList<Item>();
        }
    }

    private void writeItems() {
        File fileDir = getFilesDir();
        File itemFile = new File(fileDir, "item.txt");
        try {
            ArrayList<String> outputData = new ArrayList<String>();
            for(int i = 0; i < items.size(); i++){
                outputData.add(i,items.get(i).text);
            }
            FileUtils.writeLines(itemFile, outputData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateArrayItems() {
        readItems();
        itemsAdapter = new ItemsAdapter(this, items);
    }

    public void onAddItem(View view) {
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Item newItem = new Item(itemText);
        itemsAdapter.add(newItem);
        etNewItem.setText("");
        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_CODE) {
            String itemText = data.getExtras().getString("itemText");
            Item updatedItem = new Item(itemText);
            int position = data.getExtras().getInt("position",0);
            items.set(position,updatedItem);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
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
