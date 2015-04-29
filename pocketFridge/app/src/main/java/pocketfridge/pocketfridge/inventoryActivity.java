package pocketfridge.pocketfridge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class inventoryActivity extends Activity {

    ArrayList<String> foodNames = new ArrayList<String>();
    ArrayList<Integer> foodCalories = new ArrayList<Integer>();
    ArrayList<Integer> expDay = new ArrayList<Integer>();
    ArrayList<Integer> expMonth = new ArrayList<Integer>();
    ArrayList<Integer> expYear = new ArrayList<Integer>();
    ArrayAdapter<String> adapter;
    private String itemToAdd = "";
    final Context context = this;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inventory);

        loadArray();

        final ListView listview2 = (ListView) findViewById(R.id.listView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            int check = extras.getInt("Edit");

            if (check == 0) {
                ArrayList<String> foodToAdd = extras.getStringArrayList("addToFood");
                ArrayList<Integer> caloriesToAdd = extras.getIntegerArrayList("addToCalories");
                ArrayList<Integer> addDay = extras.getIntegerArrayList("addDay");
                ArrayList<Integer> addMonth = extras.getIntegerArrayList("addMonth");
                ArrayList<Integer> addYear = extras.getIntegerArrayList("addYear");

                for (int i = 0; i < foodToAdd.size(); i++) {
                    foodNames.add(foodToAdd.get(i).toString());
                    foodCalories.add(caloriesToAdd.get(i));
                    expDay.add(addDay.get(i));
                    expMonth.add(addMonth.get(i));
                    expYear.add(addYear.get(i));
                }
            }
            else {
                foodNames.set(extras.getInt("Index"), extras.getString("Food"));
                foodCalories.set(extras.getInt("Index"), extras.getInt("Calories"));
                expDay.set(extras.getInt("Index"), extras.getInt("Day"));
                expMonth.set(extras.getInt("Index"), extras.getInt("Month"));
                expYear.set(extras.getInt("Index"), extras.getInt("Year"));
            }
        }

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, foodNames);
        listview2.setAdapter(adapter);

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                //To help prevent double clicking
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                AlertDialog.Builder adb=new AlertDialog.Builder(inventoryActivity.this);
                adb.setTitle("Information");
                adb.setCancelable(false);
                adb.setMessage("Calories: " + foodCalories.get(position) + "\n" + "Expiration Date: "
                        + (expMonth.get(position)+1) + "/" + expDay.get(position) + "/"
                        + expYear.get(position));

                final int positionClicked = position;
                adb.setNegativeButton("Done", null);
                adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        foodNames.remove(positionClicked);
                        foodCalories.remove(positionClicked);
                        expDay.remove(positionClicked);
                        expMonth.remove(positionClicked);
                        expYear.remove(positionClicked);
                        adapter.notifyDataSetChanged();
                    }
                });

                adb.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveArray();
                        Intent intent = new Intent(getBaseContext(), foodActivity.class);
                        intent.putExtra("Food", foodNames.get(positionClicked));
                        intent.putExtra("Calories", foodCalories.get(positionClicked));
                        intent.putExtra("Day", expDay.get(positionClicked));
                        intent.putExtra("Month", expMonth.get(positionClicked));
                        intent.putExtra("Year", expYear.get(positionClicked));
                        intent.putExtra("Activity", "Inventory");
                        intent.putExtra("Index", positionClicked);
                        startActivity(intent);
                    }
                });
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

    private boolean saveArray()
    {
        SharedPreferences sp = getSharedPreferences("Inventory.txt", Context.MODE_PRIVATE);;
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putInt("Inventory_size", foodNames.size()); /* sKey is an array */

        for (int i = 0; i < foodNames.size();i++)
        {
            mEdit1.remove("Inventory_" + i);
            mEdit1.putString("Inventory_" + i, foodNames.get(i));
            mEdit1.remove("Calories_" + i);
            mEdit1.putInt("Calories_" + i, foodCalories.get(i));
            mEdit1.remove("Days_" + i);
            mEdit1.putInt("Days_" + i, expDay.get(i));
            mEdit1.remove("Months_" + i);
            mEdit1.putInt("Months_" + i, expMonth.get(i));
            mEdit1.remove("Years_" + i);
            mEdit1.putInt("Years_" + i, expYear.get(i));
        }

        return mEdit1.commit();
    }

    public void loadArray()
    {
        SharedPreferences mSharedPreference1 = getSharedPreferences("Inventory.txt", Context.MODE_PRIVATE);;
        foodNames.clear();
        int size = mSharedPreference1.getInt("Inventory_size", 0);

        for(int i=0;i<size;i++)
        {
            foodNames.add(mSharedPreference1.getString("Inventory_" + i, null));
            foodCalories.add(mSharedPreference1.getInt("Calories_" + i, -1));
            expDay.add(mSharedPreference1.getInt("Days_" + i, -1));
            expMonth.add(mSharedPreference1.getInt("Months_" + i, -1));
            expYear.add(mSharedPreference1.getInt("Years_" + i, -1));
        }
    }


    public void backToMain(View view) {
        Button button = (Button) findViewById(R.id.backInventory);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Do something in response to button click
                saveArray();
                startActivity(new Intent(inventoryActivity.this, MainActivity.class));
            }
        });
    }

    public void addInventoryItem(View view) {
        Button button = (Button) findViewById(R.id.addInventoryItem);

        //To help prevent double clicking
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add Inventory Item");

                builder.setCancelable(false);
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
        Calendar c = Calendar.getInstance();
        foodNames.add(add);
        foodCalories.add(0);
        expDay.add(c.get(Calendar.DAY_OF_MONTH));
        expMonth.add(c.get(Calendar.MONTH));
        expYear.add(c.get(Calendar.YEAR));
        adapter.notifyDataSetChanged();
    }
}
