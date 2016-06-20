package com.getlosthere.completeme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    EditText etEditItem;
    int iPosition = 0;
    long itemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String editText = getIntent().getStringExtra("itemText");
        iPosition = getIntent().getIntExtra("position",0);
        itemId = getIntent().getLongExtra("itemId",0);

        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(editText);
        etEditItem.requestFocus();
    }

    public void saveItem(View view){
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        String itemText = etEditItem.getText().toString();
        if(itemText.isEmpty()) {
         etEditItem.setError("Item cannot be blank");
        }
        else {
            Intent data = new Intent();

            data.putExtra("itemText",itemText);
            data.putExtra("position",iPosition);
            data.putExtra("itemId",itemId);

            setResult(RESULT_OK,data);
            finish();
        }
    }

}
