package pocketfridge.pocketfridge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class inventoryActivity extends Activity {

    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private String itemToAdd = "";
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        final ListView listview2 = (ListView) findViewById(R.id.listView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ArrayList<String> listToAdd = extras.getStringArrayList("addToList");
            for(int i = 0; i < listToAdd.size(); i++) {
                list.add(listToAdd.get(i).toString());
            }
        }

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview2.setAdapter(adapter);

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(inventoryActivity.this);
                adb.setTitle("Information");
                adb.setMessage("Calories: 200");
                final int positionToRemove = position;
                adb.setNegativeButton("Done", null);
                adb.setPositiveButton("Delete", new AlertDialog.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                    }});
                adb.show();

            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void backToMain(View view) {
        Button button = (Button) findViewById(R.id.backInventory);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Do something in response to button click
                startActivity(new Intent(inventoryActivity.this, MainActivity.class));
            }
        });
    }

    public void addInventoryItem(View view) {
        Button button = (Button) findViewById(R.id.addInventoryItem);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add Inventroy Item");

                // Set up the input
                final EditText input = new EditText(context);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemToAdd = input.getText().toString();
                        addItems(itemToAdd);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });
    }

    public void addItems(String add) {
        list.add(add);
        adapter.notifyDataSetChanged();
    }
}
