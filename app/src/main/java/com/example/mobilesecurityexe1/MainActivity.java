package com.example.mobilesecurityexe1;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    //UI Variables
    private EditText EDT_password;
    private MaterialButton BTN_login;

    //System Services
    private static BatteryManager myBatteryManager;
    private ConnectivityManager connManager;
    private AudioManager audioManager;
    private BluetoothAdapter bluetoothadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initSystemServices(MainActivity.this);
        initButtons();

    }
    private void findViews() {
        EDT_password = findViewById(R.id.EDT_password);
        BTN_login = findViewById(R.id.BTN_login);
    }

    private void initSystemServices(Context context) {
        //Battery Service
        myBatteryManager =  (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        //Connectivity Service
        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //Bluetooth Adapter
        bluetoothadapter = BluetoothAdapter.getDefaultAdapter();

    }


    private void initButtons() {
        BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EDT_password.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in your password", Toast.LENGTH_SHORT).show();
                } else if (checkConditions(MainActivity.this))
                    Toast.makeText(MainActivity.this, "login successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkConditions(MainActivity context) {
        String enteredPassword = EDT_password.getText().toString();
        // Condition 1 - Battery percentages are equal to the password
        if(!enteredPassword.equals(String.valueOf(getBatteryPercentage()))){
            Toast.makeText(MainActivity.this, "Battery percentages need to be equal to the password", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Condition 2 - Airplane Mode is Off
        if(isAirplaneModeOn(context)){
            Toast.makeText(MainActivity.this, "Airplane Mode is On", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Condition 3 - Wifi connect
        if(!isWifiConnect()){
           Toast.makeText(MainActivity.this, "Wifi is not connected", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Condition 4 - Bluetooth is Enable
       if(!isBluetoothEnable()){
           Toast.makeText(MainActivity.this, "Bluetooth is disabled", Toast.LENGTH_SHORT).show();
            return false;
        }

       return true;
    }

    public static int getBatteryPercentage(){
        return myBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }
    public boolean isWifiConnect(){
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }
    private boolean isBluetoothEnable() {
        if(bluetoothadapter.isEnabled())
            return true;
        else
            return false;
    }

    private static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

}