package com.example.bankingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    String[] languages = {"English", "Norwegian"};
    int language = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        TextView languageText = findViewById(R.id.languageTextView);
        Button buttonHome = findViewById(R.id.buttonToHome);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            language = extras.getInt("language");
        }


        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Settings.this,
                android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(language);


        final Button saveAndReturn = findViewById(R.id.saveAndReturn);
        saveAndReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toMain = new Intent(Settings.this, MainActivity.class);


                String lang = spinner.getSelectedItem().toString();
                switch (lang) {
                    case "English":
                        toMain.putExtra("language", 0);
                        break;
                    case "Norwegian":
                        toMain.putExtra("language", 1);
                        break;

                    default:
                        break;
                }

                setResult(RESULT_OK, toMain);
                finish();
            }

        });


        final Button backButton = findViewById(R.id.buttonToHome);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout); // Custom animation between activities.
            }
        });


        switch (language) {
            case 0:
                languageText.setText("Choose language");
                buttonHome.setText("Home");
                saveAndReturn.setText("Save");
                break;
            case 1:
                languageText.setText("Velg språk");
                saveAndReturn.setText("Lagre");
                break;

            default:
                break;
        }

    }
}
