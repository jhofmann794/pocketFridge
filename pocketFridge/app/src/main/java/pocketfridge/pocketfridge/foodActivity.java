package pocketfridge.pocketfridge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.content.Context;

public class foodActivity extends Activity {

    final Context context = this;

    TextView calorieTextView, titleTextView, expTextView;
    int calories = 0, index, day, month, year;
    String foodName = "", activity = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_food);

        //Pull in all data from calling activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            foodName = extras.getString("Food");
            calories = extras.getInt("Calories");
            day = extras.getInt("Day");
            month = extras.getInt("Month");
            year = extras.getInt("Year");
            index = extras.getInt("Index");
            activity = extras.getString("Activity");

        }

        //Set up activity
        titleTextView = (TextView)findViewById(R.id.title);
        titleTextView.setText(foodName);
        calorieTextView = (TextView)findViewById(R.id.calorieText);
        calorieTextView.setText("Calories: " + calories);
        expTextView = (TextView)findViewById(R.id.expText);
        expTextView.setText("Exp. Date: " + (month+1) + "/" + day + "/" + year);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food, menu);
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

    // This method saves the users edits and goes back to the calling activity
    public void saveEdits(View view) {
        Button button = (Button) findViewById(R.id.save);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent;
                //Create intent based on calling activity
                if (activity.equals("Grocery")) {
                    intent = new Intent(getBaseContext(), groceryActivity.class);
                }
                else {
                    intent = new Intent(getBaseContext(), inventoryActivity.class);
                }

                //push data
                intent.putExtra("Food", foodName);
                intent.putExtra("Calories", calories);
                intent.putExtra("Index", index);
                intent.putExtra("Edit", 1);
                intent.putExtra("Day", day);
                intent.putExtra("Month", month);
                intent.putExtra("Year", year);
                startActivity(intent);
            }
        });

    }

    //Set the name of the food
    public void setName(View view) {
        titleTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Item Name");

                // Set up the input
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(foodName);
                builder.setView(input);
                builder.setCancelable(false);

                // Set up the buttons
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        foodName = input.getText().toString();
                        titleTextView.setText(foodName);
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

    //Edit the calories of the food item
    public void setCalories(View view) {

        //Click on text to change parameters
        calorieTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Calorie Count");

                // Set up the input
                final NumberPicker input = new NumberPicker(context);
                input.setMinValue(0);
                input.setMaxValue(1000);
                input.setValue(calories);
                builder.setView(input);
                builder.setCancelable(false);

                // Set up the buttons
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        calories = input.getValue();
                        calorieTextView.setText("Calories: " + calories);
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

    // Edit the expiration date for the item
    public void setExpDate(View view) {

        expTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Expiration Date");

                // Set up the input
                final DatePicker input = new DatePicker(context);
                input.init(year, month, day, null);
                input.setCalendarViewShown(false);
                builder.setView(input);
                builder.setCancelable(false);


                // Set up the buttons
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        day = input.getDayOfMonth();
                        month = input.getMonth();
                        year = input.getYear();
                        expTextView.setText("Exp. Date: " + (month + 1) + "/" + day + "/" + year);
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
}
