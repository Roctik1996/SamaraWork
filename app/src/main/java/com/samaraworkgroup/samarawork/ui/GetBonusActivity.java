package com.samaraworkgroup.samarawork.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.samaraworkgroup.samarawork.BuildConfig;
import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.other.Const;
import com.samaraworkgroup.samarawork.other.FileCompressor;
import com.samaraworkgroup.samarawork.presenter.WorkPresenter;
import com.samaraworkgroup.samarawork.view.InviteView;
import com.samaraworkgroup.samarawork.view.WorkView;
import com.santalu.maskedittext.MaskEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.text.TextUtils.isEmpty;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_PASS;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_PHONE;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_TOKEN;

public class GetBonusActivity extends MvpAppCompatActivity implements OnLocaleChangedListener, WorkView {

    Button send;
    private EditText name,city,amount;
    private ImageView photo;
    private ProgressBar progressBar;
    private FrameLayout frame;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_GALLERY_PHOTO = 2;
    private File file;
    private FileCompressor mCompressor;
    private MaskEditText phone;
    private Uri selectedImage;
    private SharedPreferences mSettings;
    double percent;

    @InjectPresenter
    WorkPresenter workPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_bonus);
        send = findViewById(R.id.btn_send);
        progressBar = findViewById(R.id.progress);
        frame = findViewById(R.id.frame);
        name = findViewById(R.id.edt_title);
        city = findViewById(R.id.edt_city);
        amount = findViewById(R.id.edt_sum);
        photo = findViewById(R.id.photo);
        phone = findViewById(R.id.edt_phone);

        if (Const.lng.equals("uk")) {
            photo.setImageResource(R.drawable.report_uk);
        } else if (Const.lng.equals("ru")) {
            photo.setImageResource(R.drawable.report_ru);
        }

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_PHONE)) {
            if (!Objects.requireNonNull(mSettings.getString(APP_PREFERENCES_PHONE, "")).equals("")) {
                phone.setText(mSettings.getString(APP_PREFERENCES_PHONE, ""));
            }
        }

        mCompressor = new FileCompressor(this);

        photo.setOnClickListener(view -> selectImage());

        send.setOnClickListener(view -> {
            if (validate(name.getText().toString(),city.getText().toString(),phone.getRawText(),amount.getText().toString(),file)){
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file.getAbsoluteFile());
                MultipartBody.Part part = MultipartBody.Part.createFormData("photo", file.getName(), fileReqBody);
                SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                if (city.getText().toString().contains("Дніпро")||city.getText().toString().contains("Днепр")||
                        city.getText().toString().contains("дніпро")||city.getText().toString().contains("днепр"))
                    percent = Double.parseDouble(amount.getText().toString())*2.0/100;
                else
                    percent = Double.parseDouble(amount.getText().toString())*4.0/100;

                if (mSettings.contains(APP_PREFERENCES_TOKEN)) {
                    if (!Objects.requireNonNull(mSettings.getString(APP_PREFERENCES_TOKEN, "")).equals("")) {
                        workPresenter.newWork(mSettings.getString(APP_PREFERENCES_TOKEN, ""),
                                (int) percent,
                                "work",
                                "+38"+phone.getRawText(),
                                city.getText().toString(),
                                name.getText().toString(),
                                amount.getText().toString(),
                                part);
                    }
                }
            }
        });
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
                Glide.with(this).load(file).apply(new RequestOptions().centerCrop()).into(photo);
            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                selectedImage = data.getData();
                try {
                    file = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(this).load(file).apply(new RequestOptions().centerCrop()).into(photo);

            }
        }
    }

    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
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
    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.library),
                getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals(getString(R.string.take_photo))) {
                requestStoragePermission(true);
            } else if (items[item].equals( getString(R.string.library))) {
                requestStoragePermission(false);
            } else if (items[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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

    private boolean validate(String name, String city,String phone, String amount, File file) {
        if (isEmpty(name)) {
            Toast.makeText(this, getString(R.string.error_name_obj), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEmpty(phone)) {
            Toast.makeText(this, getString(R.string.error_phone), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Integer.parseInt(amount)<=0) {
            Toast.makeText(this,getString(R.string.error_sum_zero), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEmpty(amount)){
            Toast.makeText(this, getString(R.string.error_sum), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isEmpty(city)) {
            Toast.makeText(this, getString(R.string.error_city_address), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (file==null) {
            Toast.makeText(this, getString(R.string.error_photo), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.samaraworkgroup.samarawork";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(getString(R.string.bonus_work))
                .setContentText(getString(R.string.success))
                .setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());
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

    @Override
    public void work(String success) {
        sendNotification();
        Intent back = new Intent(this, ContentActivity.class);
        startActivity(back);
        finish();
    }

    @Override
    public void showProgress(boolean isProgress) {
        progressBar.setVisibility(isProgress ? View.VISIBLE : View.GONE);
        frame.setVisibility(isProgress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
    }
}
