package com.n8plus.vhiep.cyberzone.ui.login.signup;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.ui.login.LoginActivity;
import com.n8plus.vhiep.cyberzone.ui.login.signin.SigninFragment;
import com.n8plus.vhiep.cyberzone.ui.login.signup.confirmsignup.ConfirmSignupFragment;
import com.n8plus.vhiep.cyberzone.util.TypeLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class SignupFragment extends Fragment implements SignupContract.View, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private String TAG = "SignupFragment";
    private LinearLayout mSigninNow, mSignUpFacebook, mSignUpGoogle;
    private Button mSignupButton;
    private EditText mEditName, mEditEmail, mEditPhone, mEditUsername, mEditPassword, mEditConfirmPassword;
    private TextView mTextGender, mTextBirthday;
    private RadioGroup mGroupGender;
    private RadioButton mButtonBoy, mButtonGirl;
    private ImageView mDatePicker;
    private DatePickerDialog mDatePickerDialog;
    private Calendar calendar;

    private LoginButton mLoginButton;
    private CallbackManager mCallbackManager;
    private SignupPresenter mSignupPresenter;
    private int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mSignupPresenter = new SignupPresenter(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signup_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mSigninNow = (LinearLayout) view.findViewById(R.id.lnr_signin_now);
        mSignupButton = (Button) view.findViewById(R.id.btn_signup);
        mSignUpGoogle = (LinearLayout) view.findViewById(R.id.signup_with_google);
        mSignUpFacebook = (LinearLayout) view.findViewById(R.id.signup_with_facebook);
        mLoginButton = (LoginButton) view.findViewById(R.id.login_button);
        mEditName = (EditText) view.findViewById(R.id.edt_name);
        mEditEmail = (EditText) view.findViewById(R.id.edt_email);
        mEditPhone = (EditText) view.findViewById(R.id.edt_phone);
        mEditUsername = (EditText) view.findViewById(R.id.edt_username);
        mEditPassword = (EditText) view.findViewById(R.id.edt_password);
        mEditConfirmPassword = (EditText) view.findViewById(R.id.edt_confirm_password);
        mGroupGender = (RadioGroup) view.findViewById(R.id.rbg_gender);
        mButtonBoy = (RadioButton) view.findViewById(R.id.rbn_boy);
        mButtonGirl = (RadioButton) view.findViewById(R.id.rbn_girl);
        mTextGender = (TextView) view.findViewById(R.id.tv_gender);
        mTextBirthday = (TextView) view.findViewById(R.id.tv_birthday);
        mDatePicker = (ImageView) view.findViewById(R.id.img_date_picker);

        mSignupPresenter.loadData();
        customDatePickerDialog();

        mSigninNow.setOnClickListener(this);
        mSignupButton.setOnClickListener(this);
        mSignUpGoogle.setOnClickListener(this);
        mSignUpFacebook.setOnClickListener(this);
        mDatePicker.setOnClickListener(this);
        mGroupGender.setOnCheckedChangeListener(this);

        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        mLoginButton.setFragment(this);

        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d(TAG, response.toString());
                                try {
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String email = object.getString("email");

                                    Log.d(TAG, "FBID: " + id);

                                    // Prepare customer
                                    Customer customer = new Customer();
                                    if (id != null) customer.setAccount(new Account(id));
                                    if (name != null) customer.setName(name);
                                    if (email != null) customer.setEmail(email);

                                    // Sign up with facebook account
                                    mSignupPresenter.signUp(customer);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
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

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.img_date_picker:
                mDatePickerDialog.show();
                break;
            case R.id.lnr_signin_now:
                fragmentTransaction.replace(R.id.frm_login, new SigninFragment());
                fragmentTransaction.commit();
                break;
            case R.id.btn_signup:
                signUpDefault();
                break;
            case R.id.signup_with_google:
                signUpGoogle();
                break;
            case R.id.signup_with_facebook:
                mLoginButton.performClick();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mTextGender.setTextColor(getResources().getColor(R.color.whiteColor));
        switch (checkedId) {
            case R.id.rbn_boy:
                break;
            case R.id.rbn_girl:
                break;
        }
    }

    private void customDatePickerDialog() {
        mDatePickerDialog = new DatePickerDialog(
                this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                mTextBirthday.setText(sdf.format(calendar.getTime()));
                mDatePickerDialog.dismiss();
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE));
    }

    private void signUpDefault() {
        if (validate()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                String name = mEditName.getText().toString();
                String gender = mGroupGender.getCheckedRadioButtonId() == mButtonBoy.getId() ? "Nam" : "Nữ";
                Date birthday = simpleDateFormat.parse(mTextBirthday.getText().toString());
                String email = mEditEmail.getText().toString();
                String phone = mEditPhone.getText().toString();
                String username = mEditUsername.getText().toString();
                String password = mEditPassword.getText().toString();

                mSignupPresenter.createCustomerDefault(new Account("", username, password), new Customer(name, gender, birthday, email, phone));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validate() {
        boolean valid = true;

        if (mEditName.getText().toString().isEmpty()) {
            valid = false;
            mEditName.setError("Không được để trống");
        }

        if (!mButtonBoy.isChecked() && !mButtonGirl.isChecked()) {
            valid = false;
            showAlert("Vui lòng chọn giới tính");
        }

        if (mEditEmail.getText().toString().isEmpty()) {
            valid = false;
            mEditEmail.setError("Không được để trống");
        }

        if (mEditPhone.getText().toString().isEmpty()) {
            valid = false;
            mEditPhone.setError("Không được để trống");
        }

        if (mEditPhone.getText().toString().isEmpty()) {
            valid = false;
            mEditPhone.setError("Không được để trống");
        }

        if (mEditUsername.getText().toString().isEmpty()) {
            valid = false;
            mEditUsername.setError("Không được để trống");
        }

        if (mEditPassword.getText().toString().isEmpty()) {
            valid = false;
            mEditPassword.setError("Không được để trống");
        }

        if (mEditConfirmPassword.getText().toString().isEmpty()) {
            valid = false;
            mEditConfirmPassword.setError("Không được để trống");
        }

        if (!mEditPassword.getText().toString().equals(mEditConfirmPassword.getText().toString())) {
            valid = false;
            showAlert("Mật khẩu không trùng khớp");
        }

        return valid;
    }

    private void showAlert(String s) {
        Toast.makeText(this.getContext(), s, Toast.LENGTH_SHORT).show();
    }


    private void signUpGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this.getContext(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                Log.d(TAG, "GGID: " + account.getId());

                // Prepare customer
                Customer customer = new Customer();
                customer.setAccount(new Account(account.getId()));
                customer.setName(account.getDisplayName());
                customer.setEmail(account.getEmail());

                // Sign up with google account
                mSignupPresenter.signUp(customer);
            }
        } catch (ApiException e) {
            e.printStackTrace();
            Log.w(TAG, "signInResult: failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void createCustomerResult(boolean b) {
        Toast.makeText(this.getContext(), b ? "Đăng ký thành công!" : "Đăng ký thất bại. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAlertAccountAlready() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Thông báo");
        builder.setMessage("Tài khoản đã có trong hệ thống !");
        builder.setCancelable(false);
        builder.setPositiveButton("Đăng nhập ngay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginManager.getInstance().logOut();
                mGoogleSignInClient.signOut();
                moveToSignin();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginManager.getInstance().logOut();
                mGoogleSignInClient.signOut();
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void moveToConfirmSignup(Customer customer) {
        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut();

        Bundle signupBundle = new Bundle();
        signupBundle.putSerializable("customer", customer);
        ConfirmSignupFragment confirmSignupFragment = new ConfirmSignupFragment();
        confirmSignupFragment.setArguments(signupBundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frm_login, confirmSignupFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void moveToSignin() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frm_login, new SigninFragment());
        fragmentTransaction.commit();
    }


}
