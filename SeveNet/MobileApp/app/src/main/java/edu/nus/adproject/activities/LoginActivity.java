package edu.nus.adproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import edu.nus.adproject.R;
import edu.nus.adproject.models.Role;
import edu.nus.adproject.models.User;
import edu.nus.adproject.utils.ApiConfig;
import edu.nus.adproject.utils.PasswordUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mUsernameET;
    EditText mPasswordET;
    Button mLoginBtn;
    Button mRegisterBtn;
    TextView mErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mUsernameET = findViewById(R.id.usernameET);

        mPasswordET = findViewById(R.id.passwordET);

        mLoginBtn = findViewById(R.id.loginBtn);
        mLoginBtn.setOnClickListener(this);

        mRegisterBtn = findViewById(R.id.registerBtn);
        mRegisterBtn.setOnClickListener(this);

        mErrorMsg = findViewById(R.id.ErrorMsg);

        SharedPreferences pref = getSharedPreferences("user_credential", MODE_PRIVATE);
        String username = pref.getString("username", "");
        String password = pref.getString("password", "");

        if (pref.contains("username") && pref.contains("password")) {
            loginAutomatically(username, password);
        }

        Intent intent = getIntent();
        if (intent.hasExtra("username") && intent.hasExtra("password")) {
            username = intent.getStringExtra("username");
            password = intent.getStringExtra("password");

            mUsernameET.setText(username);
            mPasswordET.setText(password);

            loginAutomatically(username, password);
        }

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
                System.exit(0);
            }
        };

        onBackPressedDispatcher.addCallback(this, callback);
    }

    private void loginAutomatically(String username, String encryptedPW) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = login(username, encryptedPW);
                runOnUiThread(() -> {
                    if (user != null) {
                        SharedPreferences pref = getSharedPreferences("user_credential",
                                MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", username);
                        editor.putString("password", encryptedPW);
                        editor.commit();

                        startAppActivity(user);
                    } else {
                        mErrorMsg.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void startAppActivity(User user) {
        Intent intent = new Intent(LoginActivity.this, AppActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        String username = mUsernameET.getText().toString();
        String rawPassword = mPasswordET.getText().toString();
        String encryptedPassword = PasswordUtil.encodePassword(rawPassword);

        if (id == R.id.loginBtn) {
            loginAutomatically(username, encryptedPassword);
        } else if (id == R.id.registerBtn) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private User login(String username, String password) {
        HttpURLConnection conn = null;
        StringBuilder response = null;
        try {
            URL url = new URL(ApiConfig.getBaseUrl()+"/user/login");
            Log.d("Login", "query" + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", username);
            jsonParam.put("password", password);

            OutputStream out = conn.getOutputStream();
            out.write(jsonParam.toString().getBytes());
            out.flush();
            out.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                response = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                User user = new User();
                user.setId(Integer.parseInt(jsonResponse.getString("id")));
                user.setName(jsonResponse.getString("name"));
                user.setEmail(jsonResponse.getString("email"));
                user.setUsername(jsonResponse.getString("username"));
                user.setGender(jsonResponse.getString("gender"));
                user.setCountry(jsonResponse.getString("country"));
                user.setStatus(jsonResponse.getString("status"));
                user.setSocialScore(jsonResponse.getInt("socialScore"));
                user.setName(jsonResponse.getString("name"));
                user.setBlockList(jsonResponse.getString("blockList"));
                user.setPassword(jsonResponse.getString("password"));
                user.setPassword(jsonResponse.getString("phoneNum"));

                user.setJoinDate(jsonResponse.getString("joinDate"));

                setUserRoleFromJson(user, jsonResponse);

                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public void setUserRoleFromJson(User user, JSONObject jsonResponse) {
        try {
            if (jsonResponse.has("role")) {
                JSONObject roleJson = jsonResponse.getJSONObject("role");

                if (roleJson.has("id") && roleJson.has("type")) {
                    int roleId = roleJson.getInt("id");
                    String roleType = roleJson.getString("type");

                    Role role = new Role(roleId, roleType);

                    user.setRole(role);
                } else {
                    System.err.println("Role JSON object does not contain expected keys.");
                }
            } else {
                System.err.println("Role key not found in JSON response.");
            }
        } catch (Exception e) {
            System.err.println("Error parsing role from JSON: " + e.getMessage());
        }
    }
}