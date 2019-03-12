package com.example.bankingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TransactionsActivity extends AppCompatActivity {

    // Declaring ListView, Adapter and ArrayList to show as a scrollable list in app.
    ListView listViewT;
    ArrayAdapter<String> adapter;
    static ArrayList<String> listItems = new ArrayList<>();

    private static double newB; // Temporarily store balance before returning it to main.
    static int spaces = 15;     // For formatting output


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        // Receive updated balance, to return correct value to MainActivity.
        newB = getIntent().getExtras().getDouble("myBalance", 0);


        // Defining ListView item and creating a new adapter with the initialized ArrayList.
        listViewT = findViewById(R.id.listTransactions);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, (List<String>) listItems);


        // Check if there is a bundle from Main
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            // Get info about first transfer to the account.
            if (extras.getString("t2") != null) {

                // Getting strings from Main and creating the first row of information.
                String t =
                        extras.getString("t2") + "            " +
                                extras.getString("r2") + "            " +
                                extras.getString("a2") + "            " +
                                extras.getString("b2");

                // Updating ListView and adapter.
                listItems.add(t);
                adapter.notifyDataSetChanged();
            }


            // Get info about new transaction from the account.
            if (extras.getString("t") != null) {    // If

                // Formatting output:
                String a = "", b = "", c = "", t;
                int lenR = extras.getString("r").length();
                int lenA = extras.getString("a").length();
                int i;
                for (i = 0; i < spaces - lenR; i++) {
                    a = a + " ";
                }
                for (i = 0; i < spaces - lenA; i++) {
                    b = b + " ";
                }
                for (i = 0; i < spaces - lenA; i++) {
                    c = c + " ";
                }

                // Getting strings from Main and creating new list string to be added to list.
                t = extras.getString("t") + a +
                        extras.getString("r") + b +
                        extras.getString("a") + c +
                        extras.getString("b");

                // Updating ListView and adapter.
                listItems.add(t);
                adapter.notifyDataSetChanged();
            }

        }
        // Set adapter to show items in transaction Array.
        listViewT.setAdapter(adapter);


        // Show toast with Recipient and Amount after a long click on an item in the list.
        listViewT.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {

                String t = adapter.getItem(position);
                String out = "Recipient       Amount\n" + t.substring(13, (t.length() - 10));

                Toast.makeText(getApplicationContext(), out, Toast.LENGTH_LONG).show();
                return true;
            }

        });


        final Button backButton = findViewById(R.id.buttonToHome);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toMain = new Intent(TransactionsActivity.this, MainActivity.class);
                toMain.putExtra("newBalance", newB);

                setResult(RESULT_CANCELED, toMain);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout); // Custom animation between activities.
            }
        });

        // Long click on the Home button shows a toast with a description.
        backButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "Return to Home page", Toast.LENGTH_LONG).show();
                return true;
            }
        });

    }

}

