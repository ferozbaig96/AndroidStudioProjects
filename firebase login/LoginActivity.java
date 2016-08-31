package com.aftertaxapp.aftertax;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText mEmailField;
    EditText mPasswordField;
    Button mLoginEmailButton, mSignUpEmailButton, mForgotPassword, mLogoutButton;
    private ProgressDialog mProgressDialog;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    GoogleSignInOptions gso;
    private static final int RC_GOOGLE_SIGN_IN = 100;
    private GoogleApiClient mGoogleApiClient;
    SignInButton googleSignInButton;

    LoginButton facebookLoginButton;
    private CallbackManager callbackManager;

    TwitterLoginButton twitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFacebookSDK();
        setupTwitterSDK();
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Email
        initialiseViews();

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.e("FireAuth", "onAuthStateChanged:signed_in:" + user.getUid() + "\t" + user.getEmail());
                    Log.e("FireAuth", "Provider - " + user.getProviderData().get(1).getProviderId());
                    String s = "Signed in as : " + user.getEmail();
                    ((TextView) findViewById(R.id.signed_in_as)).setText(s);
                } else {
                    // User is signed out
                    Log.e("FireAuth", "onAuthStateChanged:signed_out");
                }

                updateUI(user);
            }
        };

        //Google
        configureGoogleSignIn();
        initialiseGoogleSignInBtn();

        //Facebook
        initialiseFacebookSignInBtn();

        //Twitter
        initialiseTwitterSignInBtn();

    }

    //=================General Code=================
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void signOut() {
        //Firebase sign out
        mAuth.signOut();
        updateUI(null);

        //Facebook sign out
        LoginManager.getInstance().logOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });

        //Twitter Sign out
        Twitter.logOut();
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {

            findViewById(R.id.logout_container).setVisibility(View.VISIBLE);
            findViewById(R.id.login_container).setVisibility(View.GONE);
        } else {

            findViewById(R.id.login_container).setVisibility(View.VISIBLE);
            findViewById(R.id.logout_container).setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Google
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                updateUI(null);
            }
        }

        //Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //Twitter
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    //=================Twitter Sign In Code=================
    private void setupTwitterSDK() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_key), getString(R.string.twitter_secret));
        Fabric.with(this, new Twitter(authConfig));
    }

    private void initialiseTwitterSignInBtn() {
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

        findViewById(R.id.custom_twitter_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twitterLoginButton.performClick();
            }
        });

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.e("TAG", "twitterLogin:success " + result);
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e("TAG", "twitterLogin:failure ", exception);
                updateUI(null);
            }
        });

    }

    private void handleTwitterSession(TwitterSession session) {
        Log.e("TAG", "handleTwitterSession:" + session);

        showProgressDialog();

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.e("TAG", "signInWithCredential ", task.getException());

                            String errorMsg = task.getException().toString();
                            errorMsg = errorMsg.substring(errorMsg.indexOf(':') + 2);
                            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(LoginActivity.this, "Welcome " + mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();

                        hideProgressDialog();
                    }
                });
    }

    //=================Facebook Sign In Code=================
    private void setupFacebookSDK() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        callbackManager = CallbackManager.Factory.create();
    }

    private void initialiseFacebookSignInBtn() {
        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions("email", "public_profile");

        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                updateUI(null);
                Toast.makeText(LoginActivity.this, "Login attempt cancelled", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                updateUI(null);
                Toast.makeText(LoginActivity.this, "Login attempt failed", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "facebook:onError", error);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.e("TAG", "handleFacebookAccessToken:" + token);

        showProgressDialog();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.e("TAG", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(LoginActivity.this, "Welcome " + mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();

                        hideProgressDialog();
                    }
                });
    }

    //=================Google Sign In Code=================
    private void initialiseGoogleSignInBtn() {
        googleSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        googleSignInButton.setSize(SignInButton.SIZE_ICON_ONLY);
        googleSignInButton.setScopes(gso.getScopeArray());

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });
    }

    private void configureGoogleSignIn() {
        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("TAG", "onConnectionFailed:" + connectionResult);
        Toast.makeText(LoginActivity.this, " An unresolvable error has occurred and Google APIs (including Sign-In) will not be available.", Toast.LENGTH_LONG).show();
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.e("TAG", "firebaseAuthWithGoogle:" + acct.getDisplayName());

        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.e("TAG", "signInWithCredential:failed", task.getException());

                            String errorMsg = task.getException().toString();
                            errorMsg = errorMsg.substring(errorMsg.indexOf(':') + 2);
                            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(LoginActivity.this, "Welcome " + mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();

                        hideProgressDialog();
                    }
                });
    }

    //=================Email Sign In / Sign Up Code=================
    private void initialiseViews() {
        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);
        mLoginEmailButton = (Button) findViewById(R.id.login);
        mSignUpEmailButton = (Button) findViewById(R.id.sign_up);
        mForgotPassword = (Button) findViewById(R.id.forgot_password);
        mLogoutButton = (Button) findViewById(R.id.logout);

        mLoginEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInEmail(mEmailField.getText().toString().trim(), mPasswordField.getText().toString().trim());
            }
        });

        mSignUpEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpEmail(mEmailField.getText().toString().trim(), mPasswordField.getText().toString().trim());
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPasswordResetEmail();
            }
        });
    }

    private void signUpEmail(String email, String password) {
        Log.e("TAG", "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        hideKeypad();
        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.e("TAG", "createUserWithEmail:failed" + task.getException());

                            String errorMsg = task.getException().toString();
                            errorMsg = errorMsg.substring(errorMsg.indexOf(':') + 2);
                            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(LoginActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                        hideProgressDialog();
                    }
                });
    }

    private void signInEmail(String email, String password) {
        Log.e("TAG", "signInEmail:" + email);
        if (!validateForm()) {
            return;
        }

        hideKeypad();
        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.e("TAG", "signInWithEmail:failed", task.getException());

                            String errorMsg = task.getException().toString();
                            errorMsg = errorMsg.substring(errorMsg.indexOf(':') + 2);
                            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();

                        hideProgressDialog();
                    }
                });
    }

    private void sendPasswordResetEmail() {

        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_outline, null);
        assert d != null;
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

        String email = mEmailField.getText().toString();

        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required", d);
            mEmailField.requestFocus();
            return;
        } else if (!isValidEmail(email)) {
            mEmailField.setError("Invalid", d);
            mEmailField.requestFocus();
            return;
        }

        hideKeypad();
        showProgressDialog();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("TAG", "sendPasswordResetEmail:failed" + task.getException());

                            String errorMsg = task.getException().toString();
                            errorMsg = errorMsg.substring(errorMsg.indexOf(':') + 2);
                            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("TAG", "Email sent");
                            Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_outline, null);
        assert d != null;
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required", d);
            valid = false;
            mEmailField.requestFocus();

        } else if (!isValidEmail(email)) {
            mEmailField.setError("Invalid", d);
            valid = false;
            mEmailField.requestFocus();

        } else if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required", d);
            valid = false;
            mPasswordField.requestFocus();
        } else if (password.length() < 6) {
            mPasswordField.setError("Password should be atleast 6 characters", d);
            valid = false;
            mPasswordField.requestFocus();
        }

        return valid;
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void hideKeypad() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
