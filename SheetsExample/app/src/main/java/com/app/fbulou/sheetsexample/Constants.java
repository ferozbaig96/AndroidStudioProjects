package com.app.fbulou.sheetsexample;

import com.google.api.services.sheets.v4.SheetsScopes;

public class Constants {

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    static final String PREF_ACCOUNT_NAME = "accountName";
    static final String[] SCOPES = {SheetsScopes.SPREADSHEETS_READONLY};
}
