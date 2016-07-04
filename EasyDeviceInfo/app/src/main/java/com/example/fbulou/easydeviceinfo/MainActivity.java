package com.example.fbulou.easydeviceinfo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import github.nisrulz.easydeviceinfo.EasyAppMod;
import github.nisrulz.easydeviceinfo.EasyBatteryMod;
import github.nisrulz.easydeviceinfo.EasyBluetoothMod;
import github.nisrulz.easydeviceinfo.EasyConfigMod;
import github.nisrulz.easydeviceinfo.EasyCpuMod;
import github.nisrulz.easydeviceinfo.EasyDeviceInfo;
import github.nisrulz.easydeviceinfo.EasyDeviceMod;
import github.nisrulz.easydeviceinfo.EasyDisplayMod;
import github.nisrulz.easydeviceinfo.EasyIdMod;
import github.nisrulz.easydeviceinfo.EasyLocationMod;
import github.nisrulz.easydeviceinfo.EasyMemoryMod;
import github.nisrulz.easydeviceinfo.EasyNetworkMod;
import github.nisrulz.easydeviceinfo.EasySimMod;

public class MainActivity extends AppCompatActivity {

    /*
        https://github.com/nisrulz/easydeviceinfo/wiki/Usage
        https://github.com/nisrulz/easydeviceinfo/
    */

    String resultString = null;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultString = "DEVICE INFORMATION : \n\n\n";
                recordInfo(MainActivity.this);
                showInfo();
            }
        });

        result = (TextView) findViewById(R.id.result);
    }

    private void record(String TAG, String value) {
        Log.e(TAG, value);
        resultString = resultString + TAG + " : " + value + "\n\n";
    }

    private void showInfo() {
        result.setText(resultString);
    }

    private void recordInfo(Context context) {
        ConfigMod(context);
        IdMod(context);
        NetworkMod(context);
        MemoryMod(context);
        AppMod(context);
        BatteryMod(context);
        BluetoothMod(context);
        CpuMod();
        DeviceMod(context);
        DisplayMod(context);
        SimMod(context);
        LocationMod(context);
        DeviceInfo();
    }

    void ConfigMod(Context context) {
        resultString += "Config -- \n\n";
        EasyConfigMod easyConfigMod = new EasyConfigMod(context);

        long epoch_time = easyConfigMod.getTime();
        String epoch_time_in_ms = String.valueOf(epoch_time);
        String since_last_boot_time_in_ms = String.valueOf(easyConfigMod.getUpTime());

        long five_hour_30_mins = 1000 * (5 * 3600 + 30 * 60);       //todo GMT : 5:30
        long india_time_in_ms = epoch_time + five_hour_30_mins;
        int sec = (int) (india_time_in_ms / 1000) % 60;
        int min = (int) ((india_time_in_ms / (1000 * 60)) % 60);
        int hr = (int) ((india_time_in_ms / (1000 * 60 * 60)) % 24);
        //Formatted Time (24Hr) - India GMT
        String formatted_time_24hr = String.format(Locale.US, "%02d:%02d:%02d", hr, min, sec);

        record("Epoch time in ms", epoch_time_in_ms);
        record("Time since last boot", since_last_boot_time_in_ms);
        record("Current Time (24 Hr)", formatted_time_24hr);

        int ringer_mode = easyConfigMod.getDeviceRingerMode();

        switch (ringer_mode) {
            case EasyConfigMod.RINGER_MODE_NORMAL:
                record("Ringer Mode", "Normal");
                break;
            case EasyConfigMod.RINGER_MODE_VIBRATE:
                record("Ringer Mode", "Vibrate");
                break;
            case EasyConfigMod.RINGER_MODE_SILENT:
                record("Ringer Mode", "Silent");
                break;
            default:
                record("Ringer Mode", "Other");
                break;
        }
    }

    void IdMod(Context context) {

        resultString += "\n\n\n ID -- \n\n";
        EasyIdMod easyIdMod = new EasyIdMod(context);
        String PseudoID = easyIdMod.getPseudoUniqueID();
        String AndroidID = easyIdMod.getAndroidID();
        String ua = easyIdMod.getUA();
        String gsf_id = easyIdMod.getGSFID();       //manifest READ_GSERVICES

        //Get Google Email ID
        String[] emailIds = easyIdMod.getAccounts();    //manifest GET_ACCOUNTS
        StringBuilder emailString = new StringBuilder();
        if (emailIds != null && emailIds.length > 0) {
            for (String e : emailIds) {
                emailString.append(e).append("\n");
            }
        } else {
            emailString.append("-");
        }

        String emailId = emailString.toString();

        record("PseudoID", PseudoID);
        record("AndroidID", AndroidID);
        record("ua", ua);
        record("gsf_id", gsf_id);
        record("emailID", emailId);
    }

    void NetworkMod(Context context) {
        resultString += "\n\n\n NETWORK -- \n\n";
        EasyNetworkMod easyNetworkMod = new EasyNetworkMod(context);
        boolean isNetworkAvailable = easyNetworkMod.isNetworkAvailable();
        boolean isWifiAvailable = easyNetworkMod.isWifiEnabled();
        String IPv4Address = easyNetworkMod.getIPv4Address();
        String IPv6Address = easyNetworkMod.getIPv6Address();
        String wifi_mac_address = easyNetworkMod.getWifiMAC();      //manifest ACCESS_WIFI_STATE

        record("isWifiAvailable ", isWifiAvailable + "");
        record("isNetworkAvailable ", isNetworkAvailable + "");
        record("IPv4Address ", IPv4Address);
        record("IPv6Address  ", IPv6Address);
        record("wifi_mac_address  ", wifi_mac_address);

        int network_type = easyNetworkMod.getNetworkType();
        //manifest ACCESS_NETWORK_STATE & INTERNET

        switch (network_type) {
            case EasyNetworkMod.CELLULAR_UNKNOWN:
                record("Network Type", "Unknown");
                break;
            case EasyNetworkMod.CELLULAR_UNIDENTIFIED_GEN:
                record("Network Type", "Cellular Unidentified Generation");
                break;
            case EasyNetworkMod.CELLULAR_2G:
                record("Network Type", " Cellular 2G");
                break;
            case EasyNetworkMod.CELLULAR_3G:
                record("Network Type", " Cellular 3G");
                break;
            case EasyNetworkMod.CELLULAR_4G:
                record("Network Type", " Cellular 4G");
                break;
            case EasyNetworkMod.WIFI_WIFIMAX:
                record("Network Type", " WIFI");
                break;
            default:
                record("Network Type", "Other");
                break;
        }
    }

    void MemoryMod(Context context) {
        resultString += "\n\n\n MEMORY -- \n\n";
        EasyMemoryMod easyMemoryMod = new EasyMemoryMod(context);

        //in Mb
        long Total_RAM = easyMemoryMod.getTotalRAM();
        long Available_Internal_Memory = easyMemoryMod.getAvailableInternalMemorySize();
        long Total_Internal_Memory = easyMemoryMod.getTotalInternalMemorySize();
        long Available_External_Memory = easyMemoryMod.getAvailableExternalMemorySize();
        long Total_External_Memory = easyMemoryMod.getTotalExternalMemorySize();

        record("Total_RAM", Total_RAM + "");
        record("AvailableInternalMemory", Available_Internal_Memory + "");
        record("AvailableExternalMemory", Available_External_Memory + "");
        record("Total_Internal_Memory", Total_Internal_Memory + "");
        record("Total_External_Memory", Total_External_Memory + "");
    }

    void AppMod(Context context) {
        resultString += "\n\n\n APP -- \n\n";
        EasyAppMod easyAppMod = new EasyAppMod(context);

        String ActivityName = easyAppMod.getActivityName();
        String PackageName = easyAppMod.getPackageName();
        String AppStore = easyAppMod.getStore();
        String AppName = easyAppMod.getAppName();
        String AppVersion = easyAppMod.getAppVersion();
        String AppVersionCode = easyAppMod.getAppVersionCode();

        record("ActivityName", ActivityName);
        record("PackageName", PackageName);
        record("AppStore", AppStore);
        record("AppName", AppName);
        record("AppVersion", AppVersion);
        record("AppVersionCode", AppVersionCode);


    }

    void BatteryMod(Context context) {

        resultString += "\n\n\n BATTERY -- \n\n";
        EasyBatteryMod easyBatteryMod = new EasyBatteryMod(context);

        int BatteryPercentage = easyBatteryMod.getBatteryPercentage();
        boolean isDeviceCharging = easyBatteryMod.isDeviceCharging();
        String BatteryTechnology = easyBatteryMod.getBatteryTechnology();
        float BatteryTemperature = easyBatteryMod.getBatteryTemperature();
        int BatteryVoltage = easyBatteryMod.getBatteryVoltage();
        boolean isBatteryPresent = easyBatteryMod.isBatteryPresent();

        record("BatteryPercentage", BatteryPercentage + "");
        record("isDeviceCharging", isDeviceCharging + "");
        record("BatteryTechnology", BatteryTechnology);
        record("BatteryTemperature", BatteryTemperature + "");
        record("BatteryVoltage", BatteryVoltage + "");
        record("isBatteryPresent", isBatteryPresent + "");

        int battery_health = easyBatteryMod.getBatteryHealth();

        switch (battery_health) {
            case EasyBatteryMod.HEALTH_GOOD:
                record("Battery health ", " Good");
                break;
            case EasyBatteryMod.HEALTH_HAVING_ISSUES:
                record("Battery health ", " Having issues");
                break;
            default:
                record("Battery health ", "Having issues");
                break;
        }

        int charging_source = easyBatteryMod.getChargingSource();

        switch (charging_source) {
            case EasyBatteryMod.CHARGING_VIA_AC:
                record("Charging Source", "Device charging via AC");
                break;
            case EasyBatteryMod.CHARGING_VIA_USB:
                record("Charging Source", "Device charging via USB");
                break;
            case EasyBatteryMod.CHARGING_VIA_WIRELESS:
                record("Charging Source", "Device charging via Wireless");
                break;
            case EasyBatteryMod.CHARGING_VIA_UNKNOWN_SOURCE:
                record("Charging Source", "Device charging via Unknown Source");
                break;
            default:
                record("Charging Source", "Device charging via Unknown Source");
                break;
        }

    }

    void BluetoothMod(Context context) {

        resultString += "\n\n\n BLUETOOTH -- \n\n";
        EasyBluetoothMod easyBluetoothMod = new EasyBluetoothMod(context);

        String bluetooth_mac = easyBluetoothMod.getBluetoothMAC();  //manifest BLUETOOTH
        record("Bluetooth MAC Address", bluetooth_mac);
    }

    void CpuMod() {

        resultString += "\n\n\n CPU -- \n\n";
        EasyCpuMod easyCpuMod = new EasyCpuMod();

        String supported_ABIS = easyCpuMod.getStringSupportedABIS();
        String supported_32bit_ABIS = easyCpuMod.getStringSupported32bitABIS();
        String supported_64bit_ABIS = easyCpuMod.getStringSupported64bitABIS();

        record("supported_ABIS", supported_ABIS);
        record("supported_32bit_ABIS", supported_32bit_ABIS);
        record("supported_64bit_ABIS", supported_64bit_ABIS);


    }

    void DeviceMod(Context context) {

        resultString += "\n\n\n DEVICE -- \n\n";

        EasyDeviceMod easyDeviceMod = new EasyDeviceMod(context);

        String IMEI = easyDeviceMod.getIMEI();
        String ScreenDisplayID = easyDeviceMod.getScreenDisplayID();
        String BuildVersionCodename = easyDeviceMod.getBuildVersionCodename();
        String BuildVersionIncremental = easyDeviceMod.getBuildVersionIncremental();
        int BuildVersionSDK = easyDeviceMod.getBuildVersionSDK();
        String BuildID = easyDeviceMod.getBuildID();
        String Manufacturer = easyDeviceMod.getManufacturer();
        String Model = easyDeviceMod.getModel();
        String OS_Codename = easyDeviceMod.getOSCodename();
        String OS_Version = easyDeviceMod.getOSVersion();
        String PhoneNo = easyDeviceMod.getPhoneNo();
        String RadioHardwareVersion = easyDeviceMod.getRadioVer();
        String Product = easyDeviceMod.getProduct();
        String Device = easyDeviceMod.getDevice();
        String Board = easyDeviceMod.getBoard();
        String Hardware = easyDeviceMod.getHardware();
        String Bootloader = easyDeviceMod.getBootloader();
        String Fingerprint = easyDeviceMod.getFingerprint();
        boolean isDeviceRotated = easyDeviceMod.isDeviceRooted();
        String Build_Brand = easyDeviceMod.getBuildBrand();
        String Build_Host = easyDeviceMod.getBuildHost();
        String Build_Tags = easyDeviceMod.getBuildTags();
        long Build_Time = easyDeviceMod.getBuildTime();
        String Build_User = easyDeviceMod.getBuildUser();
        String BuildVersionRelease = easyDeviceMod.getBuildVersionRelease();

        record("IMEI", IMEI);
        record("ScreenDisplayID", ScreenDisplayID);
        record("BuildVersionCodename", BuildVersionCodename);
        record("BuildVersionIncremental", BuildVersionIncremental);
        record("BuildVersionSDK", BuildVersionSDK + "");
        record("BuildID", BuildID);
        record("Manufacturer", Manufacturer);
        record("Model", Model);
        record("OS_Codename", OS_Codename);
        record("OS_Version", OS_Version);
        record("PhoneNo", PhoneNo);
        record("RadioHardwareVersion", RadioHardwareVersion);
        record("Product", Product);
        record("Device", Device);
        record("Board", Board);
        record("Hardware", Hardware);
        record("Bootloader", Bootloader);
        record("Fingerprint", Fingerprint);
        record("isDeviceRotated", isDeviceRotated + "");
        record("Build_Brand", Build_Brand);
        record("Build_Host", Build_Host);
        record("Build_Tags", Build_Tags);
        record("Build_Time", Build_Time + "");
        record("Build_User", Build_User);
        record("BuildVersionRelease", BuildVersionRelease);

        int device_type = easyDeviceMod.getDeviceType(MainActivity.this);

        switch (device_type) {
            case EasyDeviceMod.DEVICE_TYPE_WATCH:
                record("Device Type", "Watch");
                break;
            case EasyDeviceMod.DEVICE_TYPE_PHONE:
                record("Device Type", "phone");
                break;
            case EasyDeviceMod.DEVICE_TYPE_PHABLET:
                record("Device Type", "phablet");
                break;
            case EasyDeviceMod.DEVICE_TYPE_TABLET:
                record("Device Type", "tablet");
                break;
            case EasyDeviceMod.DEVICE_TYPE_TV:
                record("Device Type", "tv");
                break;
        }

        int phone_type = easyDeviceMod.getPhoneType();

        switch (phone_type) {
            case EasyDeviceMod.PHONE_TYPE_CDMA:
                record("Phone Type ", "CDMA");
                break;
            case EasyDeviceMod.PHONE_TYPE_GSM:
                record("Phone Type ", "GSM");
                break;
            case EasyDeviceMod.PHONE_TYPE_NONE:
                record("Phone Type ", "None");
                break;
            default:
                record("Phone Type ", "Unknown");
                break;
        }

        int device_orientation = easyDeviceMod.getOrientation(MainActivity.this);
        switch (device_orientation) {
            case EasyDeviceMod.ORIENTATION_LANDSCAPE:
                record("Orientation ", "Landscape");
                break;
            case EasyDeviceMod.ORIENTATION_PORTRAIT:
                record("Orientation ", "Portrait");
                break;
            case EasyDeviceMod.ORIENTATION_UNKNOWN:
                record("Orientation ", "Unknown");
                break;
            default:
                record("Orientation ", "Unknown");
                break;
        }
    }

    void DisplayMod(Context context) {
        resultString += "\n\n\n DISPLAY -- \n\n";
        EasyDisplayMod easyDisplayMod = new EasyDisplayMod(context);

        String Display_Resolution = easyDisplayMod.getResolution();
        String Screen_Density = easyDisplayMod.getDensity();

        record("Display_Resolution", Display_Resolution);
        record("Screen_Density", Screen_Density);
    }

    void SimMod(Context context) {
        resultString += "\n\n\n SIM -- \n\n";
        EasySimMod easySimMod = new EasySimMod(context);

        String IMSI = easySimMod.getIMSI();
        String SIM_Serial_No = easySimMod.getSIMSerial();
        String Country = easySimMod.getCountry();
        String Carrier = easySimMod.getCarrier();

        record("IMSI", IMSI);
        record("SIM_Serial_No", SIM_Serial_No);
        record("Country", Country);
        record("Carrier", Carrier);
    }

    void LocationMod(Context context) {
        resultString += "\n\n\n LOCATION -- \n\n";
        EasyLocationMod easyLocationMod = new EasyLocationMod(context);

        //Get Lat-Long
        double[] l = easyLocationMod.getLatLong();
        String lat = String.valueOf(l[0]);
        String lon = String.valueOf(l[1]);
        //manifest ACCESS_COARSE_LOCATION  OR ACCESS_FINE_LOCATION

        record("Latitude", lat);
        record("Longitude", lon);
    }

    void DeviceInfo() {
        resultString += "\n\n\n DEVICE INFO -- \n\n";
        EasyDeviceInfo easyDeviceInfo = new EasyDeviceInfo();

        String Library_Version = easyDeviceInfo.getLibraryVersion();

        record("Library_Version", Library_Version);
    }

}
