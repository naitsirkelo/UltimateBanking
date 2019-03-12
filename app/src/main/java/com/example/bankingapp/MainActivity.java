package com.example.bankingapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    final static int MIN = 90, MAX = 110, REQUEST_TRANSFER = 1, TRANSACTIONS = 2;
    private static double balance = ThreadLocalRandom.current().nextDouble(MIN, MAX + 1);;
    private String t, r, a, b;
    boolean setup = true;
    String[] friends = {"Joey", "Rachel", "Chandler", "Monica", "Ross", "Phoebe"};
    TextView lbl_balance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button transferButton = findViewById(R.id.btn_transfer);
        transferButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toTransfer = new Intent(MainActivity.this, TransferActivity.class);
                toTransfer.putExtra("myBalance", balance);
                toTransfer.putExtra("friendsList", friends);

                startActivityForResult(toTransfer, REQUEST_TRANSFER);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout); // Custom animation between activities.
            }
        });


        transferButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "Transfer money to a friend.", Toast.LENGTH_LONG).show();
                return true;
            }
        });


        final Button transactionButton = findViewById(R.id.btn_transactions);
        transactionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toTransactions = new Intent(MainActivity.this, TransactionsActivity.class);
                toTransactions.putExtra("myBalance", balance);

                toTransactions.putExtra("t", t);
                toTransactions.putExtra("r", r);
                toTransactions.putExtra("a", a);
                toTransactions.putExtra("b", b);

                String bString = String.format(Locale.getDefault(), "%.2f", balance);

                if (setup) {
                    toTransactions.putExtra("t2", getTime());
                    toTransactions.putExtra("r2", "Angel");
                    toTransactions.putExtra("a2", bString);
                    toTransactions.putExtra("b2", bString);

                    setup = false;
                }

                startActivityForResult(toTransactions, TRANSACTIONS);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout); // Custom animation between activities.
            }
        });

        transactionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "View all transactions.", Toast.LENGTH_LONG).show();
                return true;
            }
        });


        // Update text field with correct balance.
        lbl_balance = findViewById(R.id.balanceTextView);
        lbl_balance.setText(String.format(Locale.getDefault(), "%.2f", balance));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_TRANSFER) {

                if (resultCode == RESULT_OK) {

                    if (data.getExtras() != null) {

                        balance = data.getDoubleExtra("newBalance", 0);
                        t = data.getStringExtra("t");
                        r = data.getStringExtra("r");
                        a = data.getStringExtra("a");
                        b = data.getStringExtra("b");

                        lbl_balance.setText(String.format(Locale.getDefault(), "%.2f", balance));

                    }
                }
            }
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public String getTime() {

        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();

        long ms = Long.parseLong(ts);
        return DateFormat.format("HH:mm:ss", new Date(ms)).toString();
    }

}
