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


public class groceryActivity extends Activity {

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
        setContentView(R.layout.activity_grocery);

        //Create the list view for the food items
        final ListView listview = (ListView) findViewById(R.id.groceryList2);

        //Load in the existing food items
        loadArray();

        //Pull in data from other activity if necessary
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            foodNames.set(extras.getInt("Index"), extras.getString("Food"));
            foodCalories.set(extras.getInt("Index"), extras.getInt("Calories"));
            expDay.set(extras.getInt("Index"), extras.getInt("Day"));
            expMonth.set(extras.getInt("Index"), extras.getInt("Month"));
            expYear.set(extras.getInt("Index"), extras.getInt("Year"));
        }

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, foodNames);
        listview.setAdapter(adapter);

        //Set up what happens when an item is clicked
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                //Used to prevent double clicking
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //Create an alert dialog
                AlertDialog.Builder adb=new AlertDialog.Builder(groceryActivity.this);
                final int positionClicked = position;
                adb.setTitle("Information");
                adb.setCancelable(false);
                adb.setMessage("Calories: " + foodCalories.get(position) + "\n" + "Expiration Date: "
                                + (expMonth.get(position)+1) + "/" + expDay.get(position) + "/"
                                + expYear.get(position));

                //Do nothing button
                adb.setNegativeButton("Done", null);

                //Delete item from list
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

                //Set edit button
                adb.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveArray();
                        Intent intent = new Intent(getBaseContext(), foodActivity.class);
                        intent.putExtra("Food", foodNames.get(positionClicked));
                        intent.putExtra("Calories", foodCalories.get(positionClicked));
                        intent.putExtra("Day", expDay.get(positionClicked));
                        intent.putExtra("Month", expMonth.get(positionClicked));
                        intent.putExtra("Year", expYear.get(positionClicked));
                        intent.putExtra("Activity", "Grocery");
                        intent.putExtra("Index", positionClicked);
                        startActivity(intent);

                    }
                });
                adb.show();

            }

        });
    }

    // This method saves the grocery list to the local file system
    private boolean saveArray()
    {
        SharedPreferences sp = getSharedPreferences("Grocery.txt", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putInt("Grocery_size", foodNames.size()); /* sKey is an array */

        //Save all items one by one
        for (int i = 0; i < foodNames.size();i++)
        {
            mEdit1.remove("Grocery_" + i);
            mEdit1.putString("Grocery_" + i, foodNames.get(i));
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

    //This method loads items from local file system
    public void loadArray()
    {
        SharedPreferences mSharedPreference1 = getSharedPreferences("Grocery.txt", Context.MODE_PRIVATE);
        foodNames.clear();
        int size = mSharedPreference1.getInt("Grocery_size", 0);

        for(int i=0;i<size;i++)
        {
            foodNames.add(mSharedPreference1.getString("Grocery_" + i, null));
            foodCalories.add(mSharedPreference1.getInt("Calories_" + i, -1));
            expDay.add(mSharedPreference1.getInt("Days_" + i, -1));
            expMonth.add(mSharedPreference1.getInt("Months_" + i, -1));
            expYear.add(mSharedPreference1.getInt("Years_" + i, -1));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grocery, menu);
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

    //Method sends user back to the main activity
    public void backToMain(View view) {
        Button button = (Button) findViewById(R.id.backGrocery);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Do something in response to button click
                saveArray();
                startActivity(new Intent(groceryActivity.this, MainActivity.class));
            }
        });
    }

    // Method adds a new item to the grocery list
    public void addGrocery(View view) {
        Button button = (Button) findViewById(R.id.addGrocery);

        //To prevent double clicking
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        //Get new item
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Do something in response to button click

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add Grocery Item");

                builder.setCancelable(false);

                // Set up the text input
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);


                // Set up the buttons
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Save away input
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

    //This method moves everything from the grocery list to the inventory
    public void checkout(View view) {
        Button button = (Button) findViewById(R.id.checkout);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Make sure user wanted to do action
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setCancelable(false);

                // Move all items to inventory
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(groceryActivity.this, inventoryActivity.class);
                        i.putExtra("addToFood", foodNames);
                        i.putExtra("addToCalories", foodCalories);
                        i.putExtra("addDay", expDay);
                        i.putExtra("addMonth", expMonth);
                        i.putExtra("addYear", expYear);
                        i.putExtra("Edit", 0);
                        startActivity(i);
                        foodNames.clear();
                        foodCalories.clear();
                        saveArray();
                    }
                });

                // Cancel action
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

    // This methods adds item information to respective lists
    public void addItems(String add) {
        Calendar c = Calendar.getInstance();
        foodNames.add(add);
        foodCalories.add(0);

        //Set expiration date to current day
        expDay.add(c.get(Calendar.DAY_OF_MONTH));
        expMonth.add(c.get(Calendar.MONTH));
        expYear.add(c.get(Calendar.YEAR));
        adapter.notifyDataSetChanged();
    }
}