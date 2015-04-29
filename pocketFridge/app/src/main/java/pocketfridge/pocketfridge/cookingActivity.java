package pocketfridge.pocketfridge;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;


public class cookingActivity extends Activity {

    int recipeNum;
    TextView ingredientsTextView, instructionsTextView, titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cooking);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipeNum = extras.getInt("Recipe");
        }

        ingredientsTextView = (TextView)findViewById(R.id.recipeList);
        instructionsTextView = (TextView)findViewById(R.id.instructionList);
        titleTextView = (TextView)findViewById(R.id.titleRecipe);

        switch (recipeNum) {
            case 0:
                ingredientsTextView.setText(R.string.cookiesIng);
                instructionsTextView.setText(R.string.cookiesIns);
                titleTextView.setText("Chocolate Chip Cookies");
                break;
            case 1:
                ingredientsTextView.setText(R.string.brownieIng);
                instructionsTextView.setText(R.string.brownieIns);
                titleTextView.setText("Brownies");
                break;
            case 2:
                ingredientsTextView.setText(R.string.pizzaIng);
                instructionsTextView.setText(R.string.pizzaIns);
                titleTextView.setText("Peperoni Pizza");
                break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cooking, menu);
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
}
