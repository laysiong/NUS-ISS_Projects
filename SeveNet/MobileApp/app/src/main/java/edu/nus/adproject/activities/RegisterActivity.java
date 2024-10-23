package edu.nus.adproject.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.nus.adproject.R;
import edu.nus.adproject.services.CountryService;
import edu.nus.adproject.services.RegisterService;
import edu.nus.adproject.utils.PasswordUtil;

public class RegisterActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    EditText mNameET, mUsernameET, mPasswordET, mEmailET, mPhoneET;
    Spinner mGenderSpinner, mCountrySpinner;
    Button mRegisterBtn, mBackBtn;

    RegisterService registerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mNameET = findViewById(R.id.nameET);

        mUsernameET = findViewById(R.id.usernameET);

        mPasswordET = findViewById(R.id.passwordET);

        mEmailET = findViewById(R.id.emailET);

        mPhoneET = findViewById(R.id.phoneET);

        mGenderSpinner = (Spinner) findViewById(R.id.genderSpn);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(genderAdapter);

        mCountrySpinner = (Spinner) findViewById(R.id.countrySpn);
        new FetchCountriesTask().execute();

        mRegisterBtn = findViewById(R.id.registerBtn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("name", mNameET.getText().toString().trim());
                    jsonParam.put("email", mEmailET.getText().toString().trim());
                    jsonParam.put("username", mUsernameET.getText().toString().trim());

                    String rawPw = mPasswordET.getText().toString().trim();
                    String encryptedPw = PasswordUtil.encodePassword(rawPw);
                    jsonParam.put("password", encryptedPw);

                    String selectedGender = mGenderSpinner.getSelectedItem().toString();
                    jsonParam.put("gender", selectedGender);

                    String selectedCountry = mCountrySpinner.getSelectedItem().toString();
                    jsonParam.put("country", selectedCountry);

                    jsonParam.put("phoneNum", mPhoneET.getText().toString().trim());
                    new RegisterUserTask().execute(jsonParam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mBackBtn = findViewById(R.id.backBtn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class FetchCountriesTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            CountryService cs = new CountryService();
            return cs.getCountries();
        }

        @Override
        protected void onPostExecute(List<String> countries) {
            if (countries != null) {
                Collections.sort(countries);
                ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, countries);
                countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mCountrySpinner.setAdapter(countryAdapter);

                String defaultCountry = "Singapore";
                int defaultPos = countries.indexOf(defaultCountry);
                if (defaultPos != -1) {
                    mCountrySpinner.setSelection(defaultPos);
                }
            }
        }
    }

    private class RegisterUserTask extends AsyncTask<JSONObject, Void, Boolean> {
        @Override
        protected Boolean doInBackground(JSONObject... params) {
            JSONObject jsonParam = params[0];
            registerService = new RegisterService();
            return registerService.registerUser(jsonParam);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if (isSuccess) {
                Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("username", mUsernameET.getText().toString().trim());
                String rawPw = mPasswordET.getText().toString().trim();
                String encryptedPw = PasswordUtil.encodePassword(rawPw);
                intent.putExtra("password", encryptedPw);
                startActivity(intent);
            }
            else {
                Toast.makeText(RegisterActivity.this, "Registration Failed. Try Again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}