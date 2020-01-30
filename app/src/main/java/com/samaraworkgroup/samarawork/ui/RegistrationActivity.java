package com.samaraworkgroup.samarawork.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.FileProvider;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.samaraworkgroup.samarawork.BuildConfig;
import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.other.Const;
import com.samaraworkgroup.samarawork.other.FileCompressor;
import com.samaraworkgroup.samarawork.presenter.RegistrationPresenter;
import com.samaraworkgroup.samarawork.view.RegistrationView;
import com.santalu.maskedittext.MaskEditText;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.text.TextUtils.isEmpty;

public class RegistrationActivity extends MvpAppCompatActivity implements RegistrationView, OnLocaleChangedListener {

    Button signUp;
    private ImageView photo;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private File file;
    private FileCompressor mCompressor;
    private EditText name, city, count;
    private AppCompatEditText pass;
    private MaskEditText phone;
    private CheckBox yes, no;
    private String check = "";
    private MaterialSpinner spec;
    private String key;
    private ProgressBar progressBar;
    private FrameLayout frame;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    @InjectPresenter
    RegistrationPresenter registrationPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        int key_num = (int) ((Math.random()) * 999) + 100;
        String public_key = Base64.encodeToString(String.valueOf(key_num).getBytes(), Base64.DEFAULT);
        long a = (long) (Math.pow(key_num, 3) +
                (3 * Math.pow(key_num, 2) * key_num) +
                (3 * key_num * Math.pow(key_num, 2)) +
                Math.pow(key_num, 3));
        String private_key = sha256(String.valueOf(a));

        key = public_key + "&" + private_key;

        signUp = findViewById(R.id.sign_up);
        photo = findViewById(R.id.image);
        name = findViewById(R.id.edt_name);
        city = findViewById(R.id.edt_surname);
        phone = findViewById(R.id.edt_phone);
        pass = findViewById(R.id.edt_pass);
        count = findViewById(R.id.edt_count);
        progressBar = findViewById(R.id.progress);
        frame = findViewById(R.id.frame);

        spec = findViewById(R.id.edt_spec);

        yes = findViewById(R.id.check_yes);
        no = findViewById(R.id.check_no);

        mCompressor = new FileCompressor(this);

        if (Const.lng.equals("uk")) {
            photo.setImageResource(R.drawable.photo_uk);
        } else if (Const.lng.equals("ru")) {
            photo.setImageResource(R.drawable.photo_ru);
        }

        yes.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                check = "true";
                no.setChecked(false);
            }
        });

        no.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                check = "false";
                yes.setChecked(false);
            }
        });

        setupVerificationCallback();
        signUp.setOnClickListener(view -> {
            if (validate(name.getText().toString(), city.getText().toString(), "+38" + phone.getRawText(),
                    pass.getText().toString(), count.getText().toString(), check, spec.getSelectedItemPosition())) {

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+38" + phone.getRawText(),
                        60,
                        TimeUnit.SECONDS,
                        this,
                        mCallbacks);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                final EditText input = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alertDialog.setTitle(getString(R.string.code));
                alertDialog.setMessage(getString(R.string.input_code));
                alertDialog.setView(input);

                if (!input.getText().equals("")) {
                    alertDialog.setPositiveButton("OK",
                            (dialog, which) -> {
                                verifyPhoneNumberWithCode(mVerificationId, input.getText().toString());
                            });
                }
                else
                    Toast.makeText(this, getString(R.string.input_ver_code),Toast.LENGTH_LONG).show();
                alertDialog.setNegativeButton(getString(R.string.canceled),
                        (dialog, which) -> dialog.cancel());

                alertDialog.show();
            }


        });


        photo.setOnClickListener(v -> selectImage());
    }

    private void setupVerificationCallback() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        if (file != null) {
                            RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file.getAbsoluteFile());
                            MultipartBody.Part part = MultipartBody.Part.createFormData("photo", file.getName(), fileReqBody);
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                                registrationPresenter.getSignUp(key.replace("\n", "").replace("\r", ""),
                                        name.getText().toString(),
                                        pass.getText().toString(),
                                        city.getText().toString(),
                                        "+38" + phone.getRawText(),
                                        Integer.parseInt(count.getText().toString()),
                                        Boolean.valueOf(check),
                                        spec.getSelectedItem().toString(),
                                        part,instanceIdResult.getToken());
                            });

                        } else {
                            RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                            MultipartBody.Part part = MultipartBody.Part.createFormData("photo", "", fileReqBody);
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                                registrationPresenter.getSignUp(key.replace("\n", "").replace("\r", ""),
                                        name.getText().toString(),
                                        pass.getText().toString(),
                                        city.getText().toString(),
                                        "+38" + phone.getRawText(),
                                        Integer.parseInt(count.getText().toString()),
                                        Boolean.valueOf(check),
                                        spec.getSelectedItem().toString(),
                                        part,instanceIdResult.getToken());
                            });
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, getString(R.string.error_code), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        if (!code.equals("")) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else Toast.makeText(this, getString(R.string.error_code), Toast.LENGTH_LONG).show();

    }


    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean validate(String name, String city, String phone, String password, String count, String comand, int spec) {


        if (isEmpty(name)) {
            Toast.makeText(this, getString(R.string.error_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEmpty(city)) {
            Toast.makeText(this, getString(R.string.error_address), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEmpty(count)) {
            Toast.makeText(this, getString(R.string.error_amount), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (count.equals("0")) {
            Toast.makeText(this, getString(R.string.error_small_amount), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEmpty(comand)) {
            Toast.makeText(this, getString(R.string.error_tour), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spec == 0) {
            Toast.makeText(this, getString(R.string.error_spec), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isEmpty(phone)) {
            Toast.makeText(this, getString(R.string.error_phone), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEmpty(password)) {
            Toast.makeText(this, getString(R.string.error_pass), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isPasswordValid(password)) {
            Toast.makeText(this, getString(R.string.invalid_pass), Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo),
                getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals(getString(R.string.take_photo))) {
                requestStoragePermission();
            } else if (items[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void requestStoragePermission() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            dispatchTakePictureIntent();
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Toast.makeText(this, "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.need_perm));
        builder.setMessage(getString(R.string.msg_perm));
        builder.setPositiveButton(getString(R.string.go_setting), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(Objects.requireNonNull(this).getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                file = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(mFileName, ".jpg", storageDir);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    file = mCompressor.compressToFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(this).load(file).apply(new RequestOptions().centerCrop().placeholder(R.drawable.photo_uk)).into(photo);
            }
        }
    }

    @Override
    public void registration(String token) {
        if (!token.equals("")) {
            Toast.makeText(this, getString(R.string.reg_success), Toast.LENGTH_LONG).show();
            Intent back = new Intent(this, MainActivity.class);
            startActivity(back);
            finish();
        }
    }

    @Override
    public void showProgress(boolean isProgress) {
        progressBar.setVisibility(isProgress ? View.VISIBLE : View.GONE);
        frame.setVisibility(isProgress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String error) {
        if (error.equals("HTTP 500 Internal Server Error"))
            Toast.makeText(this, getString(R.string.error_user_found), Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(newBase));
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    @Override
    public void onBeforeLocaleChanged() {
    }

    @Override
    public void onAfterLocaleChanged() {
    }
}
