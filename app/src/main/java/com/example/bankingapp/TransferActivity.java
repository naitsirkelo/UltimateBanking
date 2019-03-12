package com.example.bankingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Locale;

public class TransferActivity extends AppCompatActivity {

    // Declare class variables
    private static double input;
    private static double balance;
    private static String receiver;
    String[] friends;
    int language = 0;
    TextView bEURText, reciText, amouText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        // Create dropdown menu (spinner).
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);

        // Update balance
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            friends = extras.getStringArray("friendsList");
            language = extras.getInt("language");
        }


        input = 0;
        balance = getIntent().getExtras().getDouble("myBalance");


        // Define spinner settings and apply 'friends' array.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TransferActivity.this,
                android.R.layout.simple_spinner_item, friends);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        // Defining label for check of amount.
        TextView lbl_amount_check = findViewById(R.id.amountError);

        // Defining area that user inputs number.
        EditText txt_amount = findViewById(R.id.inputAmount);

        // Defining button used to complete transfer.
        final Button btn_pay = findViewById(R.id.buttonPay);
        // Initially set button to not be enabled.
        btn_pay.setEnabled(false);

        // Add check to see if a number is correctly inputted.
        txt_amount.addTextChangedListener(new TextWatcher() {

            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // Convert EditText to string, and store int from string in variable.
                if (!txt_amount.getText().toString().equals("")) {
                    TransferActivity.input = Integer.parseInt(txt_amount.getText().toString());
                } else {
                    input = 0;
                }

                // Button disabled if input is blank, is 0 or is too high.
                if (!s.toString().equals("") && input > balance) {
                    btn_pay.setEnabled(false);
                    btn_pay.setBackgroundColor(Color.GRAY);

                    lbl_amount_check.setText("Input amount too high.");

                } else if (s.toString().equals("0") || TextUtils.isEmpty(s)) {
                    btn_pay.setEnabled(false);
                    btn_pay.setBackgroundColor(Color.GRAY);

                    lbl_amount_check.setText("Input amount can not be 0.");

                } else {
                    btn_pay.setEnabled(true);
                    btn_pay.setBackgroundColor(Color.parseColor("#FF6C1A"));

                    lbl_amount_check.setText("");
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                balance -= input;   // Update balance with chosen amount.
                receiver = spinner.getSelectedItem().toString();    // Store chosen name from menu.

                // Putting the transaction information in a bundle for the Main intent.
                Intent toMain = new Intent(TransferActivity.this, MainActivity.class);
                toMain.putExtra("newBalance", balance);
                toMain.putExtra("t", getTime());
                toMain.putExtra("r", receiver);

                /* Formatting output from Double to String to preserve 2 decimals
                   and convert correctly according to local language settings. */
                toMain.putExtra("a", String.format(Locale.getDefault(), "%.2f", input));
                toMain.putExtra("b", String.format(Locale.getDefault(), "%.2f", balance));

                setResult(RESULT_OK, toMain);

                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout); // Custom animation between activities.
            }
        });

        btn_pay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "Click once to complete transfer.", Toast.LENGTH_LONG).show();
                return true;
            }
        });


        // Defining button used to return to home page.
        final Button backButton = findViewById(R.id.buttonToHome);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toMain = new Intent(TransferActivity.this, MainActivity.class);
                toMain.putExtra("newBalance", balance);
                toMain.putExtra("language", language);

                finish();
                startActivity(toMain);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout); // Custom animation between activities.
            }
        });

        backButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "Return to Home page", Toast.LENGTH_LONG).show();
                return true;
            }
        });


        // Update text field with correct balance.
        final TextView lbl_balance = (TextView) findViewById(R.id.balanceTextView);
        lbl_balance.setText(String.format(Locale.getDefault(), "%.2f", balance));


        bEURText = findViewById(R.id.bEURTextView);
        reciText = findViewById(R.id.textReci);
        amouText = findViewById(R.id.textAmount);
        switch (language) {
            case 0:
                bEURText.setText("Balance [EUR]");
                reciText.setText("Recipient");
                amouText.setText("Amount");
                btn_pay.setText("Pay");
                backButton.setText("Home");
                break;
            case 1:
                bEURText.setText("Balanse [EUR]");
                reciText.setText("Mottaker");
                amouText.setText("Sum");
                btn_pay.setText("Betal");
                backButton.setText("Hjem");
                break;

            default:
                break;
        }

    }


    public String getTime() {

        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();

        long ms = Long.parseLong(ts);

        return DateFormat.format("HH:mm:ss", new Date(ms)).toString();
    }

}
