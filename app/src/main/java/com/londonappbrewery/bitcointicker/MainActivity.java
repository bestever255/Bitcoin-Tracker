package com.londonappbrewery.bitcointicker;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/";

    // Member Variables:
    TextView mPriceTextView;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        final Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);
        name = (TextView)findViewById(R.id.name);

        // Create an ArrayAdapter using the String array and a spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String text = spinner.getSelectedItem().toString();
                if(text.equals("AUD")){
                    name.setText("Australian dollar");
                }else if(text.equals("JPY")){
                    name.setText("Japanese yen");
                }else if(text.equals("EUR")){
                    name.setText("Euro");
                }else if(text.equals("GBP")){
                    name.setText("Pound sterling");
                }else if(text.equals("BRL")){
                    name.setText("Brazilian Real");
                }else if(text.equals("CAD")){
                    name.setText("Canadian dollar");
                }else if(text.equals("CNY")){
                    name.setText("Renminbi");
                }else if(text.equals("HKD")){
                    name.setText("Hong Kong dollar");
                }else if(text.equals("PLN")){
                    name.setText("Polish Zloty");
                }else if(text.equals("RUB")){
                    name.setText("Russian ruble");
                }else if(text.equals("SEK")){
                    name.setText("Swedish Krona");
                }else if(text.equals("USD")){
                    name.setText("United States dollar");
                }else if(text.equals("ZAR")){
                    name.setText("South African rand");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Toast.makeText(MainActivity.this, "Please select an item", Toast.LENGTH_SHORT).show();
            }
        });
        // The Spinner reports back on two events
        // 1. If nothing was selected
        // 2. If an item was selected
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // The "onItemSelected()" method is triggered when something is selected on the Spinner
                Log.d("Bitcoin", "" + parentView.getItemAtPosition(position)); // Print the chosen currency

                String chosenCurrency = (String) parentView.getItemAtPosition(position); // Put the currency into a String called "chosenCurrency"
                String toBeParsed = "BTC" + chosenCurrency; // Append the "chosenCurrency" String variable to the "toBeParsed" String variable

                // Call "letsDoSomeNetWorking()" in onItemSelected() and pass in the final URL that includes the user's chosenCurrency (toBeParsed)
                letsDoSomeNetworking(BASE_URL + toBeParsed);
                Log.d("Bitcoin", "" + BASE_URL + toBeParsed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // The "onNothingSelected()" method is triggered when nothing is selected on the Spinner
                Log.d("Bitcoin", "Nothing selected");
            }

        });


    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {

        /* Create a AsyncHttpClient object to make the API call.
         This Class is included in the library we added in the Gradle dependency*/
        AsyncHttpClient client = new AsyncHttpClient();

        // Supply the parameters needed for the API call (the url with the chosen currency appended to the end)
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // the "onSuccess()" method is called when response HTTP status is "200 OK"
                Log.d("Bitcoin", "JSON: " + response.toString());

                String mPrice = null; // Create a temporary variable to hold the price of the given currency
                try { // Try to...
                    mPrice = response.getString("ask"); // Get the String value of the the "ask"
                    mPriceTextView.setText(mPrice);
                } catch (JSONException e) { // Catch a JSON exception if there is a problem parsing it
                    e.printStackTrace(); // Print the error
                }

                updateUI(mPrice); // Pass the "mPrice" String variable as a parameter to the "updateUI()" method

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // The "onFailure()" method is called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response: " + response);
                Log.e("ERROR", e.toString());
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show(); // Create Toast message
            }
        });
    }

    private void updateUI(String price) {
        Log.d("Bitcoin", "updateUI() callback received");
        // The "updateUI()" method takes care of updating all of the views on screen

        // Update the "mPriceTextView" with the currency price
        mPriceTextView.setText(price);

    }


}
