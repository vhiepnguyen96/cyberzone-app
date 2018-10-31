package com.n8plus.vhiep.cyberzone.ui.login.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.ui.home.HomeActivity;
import com.n8plus.vhiep.cyberzone.ui.login.signup.SignupFragment;
import com.n8plus.vhiep.cyberzone.ui.product.ProductActivity;
import com.n8plus.vhiep.cyberzone.ui.productdetails.ProductDetailActivity;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class SigninFragment extends Fragment implements SigninContract.View, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private LinearLayout mSignInGoogle, mSignInFacebook;
    private CallbackManager mCallbackManager;

    private LoginButton mSigninFacebook;
    private Button mLoginButton;
    private EditText mUsernameText, mPasswordText;
    private LinearLayout mSignupLayout;
    private ProgressDialog mProgressDialog;

    private AccessTokenTracker mAccessTokenTracker;
    private ProfileTracker mProfileTracker;

    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    private SessionManager mSessionManager;

    private int RC_SIGN_IN = 9001;
    private String TAG = "SigninFragment";
    private SigninPresenter mSigninPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Profile profile = Profile.getCurrentProfile();
                        if (profile != null) {
                            onSigninFacebookSuccess(profile);
                        } else {
                            onSigninFacebookFailed();
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.v(TAG, "Cancle");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.v(TAG, exception.getCause().toString());
                    }
                });

        mSessionManager = new SessionManager(this.getActivity().getApplicationContext());

        if (mSessionManager.isLoggedIn()) {
            Intent intentHome = new Intent(this.getActivity(), HomeActivity.class);
            startActivity(intentHome);
        }

        mCallbackManager = CallbackManager.Factory.create();

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                if (newProfile != null) {
                    onSigninFacebookSuccess(newProfile);
                } else {
                    onSigninFacebookSuccess(oldProfile);
                }
            }
        };

        mAccessTokenTracker.startTracking();
        mProfileTracker.startTracking();

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signin_frag, container, false);
        mSigninPresenter = new SigninPresenter(this);

        mSigninFacebook = (LoginButton) view.findViewById(R.id.login_button);

        mSigninFacebook.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        mSigninFacebook.setFragment(this);

        mSigninFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d(TAG, response.getJSONObject().toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    onSigninFacebookSuccess(profile);
                } else {
                    onSigninFacebookFailed();
                }
            }

            @Override
            public void onCancel() {
                Log.v(TAG, "Cancle");
            }

            @Override
            public void onError(FacebookException error) {
                Log.v(TAG, error.getCause().toString());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mSignInFacebook = (LinearLayout) view.findViewById(R.id.signin_with_facebook);
        mSignInGoogle = (LinearLayout) view.findViewById(R.id.signin_with_google);

        mLoginButton = (Button) view.findViewById(R.id.btn_login);
        mUsernameText = (EditText) view.findViewById(R.id.edt_username);
        mPasswordText = (EditText) view.findViewById(R.id.edt_password);
        mSignupLayout = (LinearLayout) view.findViewById(R.id.lnr_signup_now);

        // Custom view
        mProgressDialog = new ProgressDialog(this.getActivity(), R.style.AppTheme_Dark_Dialog);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this.getContext(), gso);


        mSignInFacebook.setOnClickListener(this);
        mSignInGoogle.setOnClickListener(this);

        mLoginButton.setOnClickListener(this);
        mSignupLayout.setOnClickListener(this);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            onSigninFacebookSuccess(profile);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_with_facebook:
                mSigninFacebook.performClick();
                break;
            case R.id.signin_with_google:
                signInGoogle();
                break;
            case R.id.btn_login:
                signInDefault();
                break;
            case R.id.lnr_signup_now:
                signUp();
                break;
        }
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                onSigninGoogleSuccess(account);
            } else {
                onSigninGoogleFailed();
            }
        } catch (ApiException e) {
            e.printStackTrace();
            Log.w(TAG, "signInResult: failed code=" + e.getStatusCode());
            onSigninGoogleFailed();
        }

    }

    private void signUp() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frm_login, new SignupFragment());
        fragmentTransaction.commit();
    }

    public void signInDefault() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onSigninGoogleFailed();
            return;
        }

        mLoginButton.setEnabled(false);

        showProgressDialog();

        String username = mUsernameText.getText().toString();
        String password = mPasswordText.getText().toString();
        mSigninPresenter.checkLogin(username, password);
    }

    private boolean validate() {
        boolean valid = true;

        String username = mUsernameText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (!username.isEmpty()) {
            mUsernameText.setError(null);
        } else {
            valid = false;
            mUsernameText.setError("Vui lòng nhập tên đăng nhập");
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 12) {
            mPasswordText.setError("Mật khẩu phải từ 4 đến 12 ký tự");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        return valid;
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this.getContext());
            mProgressDialog.setMessage("Đang xác thực..");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onSigninDefaultSuccess(Account account) {
        hideProgressDialog();
        Toast.makeText(this.getContext(), "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
        mSessionManager.setLogin(true);
        mSessionManager.setAccountLogin(account.getId());
        moveToHome();
    }

    @Override
    public void onSigninDefaultFailed() {
        hideProgressDialog();
        Toast.makeText(this.getContext(), "Đăng nhập thất bại!", Toast.LENGTH_LONG).show();
        mSessionManager.setLogin(false);
        mSessionManager.setAccountLogin(null);
        mLoginButton.setEnabled(true);
    }

    @Override
    public void onSigninGoogleSuccess(GoogleSignInAccount googleAccount) {
        Toast.makeText(this.getContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
        mSessionManager.setLogin(true);
        mSessionManager.setAccountLogin(googleAccount.getId());
        moveToHome();
    }

    @Override
    public void onSigninGoogleFailed() {
        mProgressDialog.dismiss();
        Toast.makeText(this.getContext(), "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
        mSessionManager.setLogin(false);
        mSessionManager.setAccountLogin(null);

    }

    @Override
    public void onSigninFacebookSuccess(Profile profile) {
        Toast.makeText(this.getContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
        mSessionManager.setLogin(true);
        mSessionManager.setAccountLogin(profile.getId());
        moveToHome();
    }

    @Override
    public void onSigninFacebookFailed() {
        Toast.makeText(this.getContext(), "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
        mSessionManager.setLogin(false);
        mSessionManager.setAccountLogin(null);
    }

    @Override
    public void moveToHome() {
        startActivity(new Intent(this.getActivity(), HomeActivity.class));
        this.getActivity().finish();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

}