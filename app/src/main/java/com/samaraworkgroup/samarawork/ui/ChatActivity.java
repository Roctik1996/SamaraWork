package com.samaraworkgroup.samarawork.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.samaraworkgroup.samarawork.BuildConfig;
import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.model.AllMessage;
import com.samaraworkgroup.samarawork.model.ChatId;
import com.samaraworkgroup.samarawork.model.MessageList;
import com.samaraworkgroup.samarawork.other.FileCompressor;
import com.samaraworkgroup.samarawork.presenter.AllMessagePresenter;
import com.samaraworkgroup.samarawork.presenter.SendMessagePresenter;
import com.samaraworkgroup.samarawork.ui.adapter.MessageListAdapter;
import com.samaraworkgroup.samarawork.view.AllMessageView;
import com.samaraworkgroup.samarawork.view.SendMessageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_ID;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_TOKEN;

public class ChatActivity extends MvpAppCompatActivity implements AllMessageView, SendMessageView {

    private RecyclerView recyclerView;
    private ArrayList<MessageList> messages;
    private EditText body;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_GALLERY_PHOTO = 2;
    private File file;
    private FileCompressor mCompressor;
    MessageListAdapter adapter;
    boolean check = true;
    Timer timer;
    String token;
    Integer userId;
    private ConstraintLayout photoConstraint;
    private ImageView photo;

    @InjectPresenter
    AllMessagePresenter getMessagePresenter;

    @InjectPresenter
    SendMessagePresenter sendMessagePresenter;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messages = new ArrayList<>();
        mCompressor = new FileCompressor(Objects.requireNonNull(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        body = findViewById(R.id.edt_msg);
        photoConstraint = findViewById(R.id.photo_constraint);
        ImageView send = findViewById(R.id.send);
        photo = findViewById(R.id.photo);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_TOKEN)) {
            if (!Objects.requireNonNull(mSettings.getString(APP_PREFERENCES_TOKEN, "")).equals("")) {
                token = mSettings.getString(APP_PREFERENCES_TOKEN, "");
            }
            if (!Objects.requireNonNull(mSettings.getString(APP_PREFERENCES_ID, "")).equals("")) {
                userId = Integer.parseInt(mSettings.getString(APP_PREFERENCES_ID, ""));
            }
        }


        send.setOnClickListener(view -> {
            if (!body.getText().toString().equals("")) {
                if (file != null) {
                    RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file.getAbsoluteFile());
                    MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
                    sendMessagePresenter.sendMessage(token, body.getText().toString(),
                            getIntent().getIntExtra("id", 0), part);
                    file = null;
                    body.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_camera, 0);
                    photoConstraint.setVisibility(View.GONE);
                } else {
                    RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                    MultipartBody.Part part = MultipartBody.Part.createFormData("image", "", fileReqBody);
                    sendMessagePresenter.sendMessage(token, body.getText().toString(),
                            getIntent().getIntExtra("id", 0), part);
                    file = null;
                    body.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_camera, 0);
                    photoConstraint.setVisibility(View.GONE);
                }
            } else if (file != null) {
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file.getAbsoluteFile());
                MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
                sendMessagePresenter.sendMessage(token, "",
                        getIntent().getIntExtra("id", 0), part);
                file = null;
                body.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_camera, 0);
                photoConstraint.setVisibility(View.GONE);
            }
        });

        body.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (body.getRight() - body.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (file == null) {
                        selectImage();
                    } else {
                        file = null;
                        photoConstraint.setVisibility(View.GONE);
                        body.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_camera, 0);
                    }
                    return true;
                }
            }

            return false;
        });

        getMessagePresenter.getMessage(token, new ChatId(getIntent().getIntExtra("id", 0)));

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> getMessagePresenter.getMessage(token, new ChatId(getIntent().getIntExtra("id", 0))));
            }
        }, 0, 4000);
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.library),
                getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals(getString(R.string.take_photo))) {
                requestStoragePermission(true);
            } else if (items[item].equals(getString(R.string.library))) {
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
        File storageDir = Objects.requireNonNull(this).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(mFileName, ".jpg", storageDir);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    file = mCompressor.compressToFile(file);
                    body.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_close, 0);
                    photoConstraint.setVisibility(View.VISIBLE);
                    Glide.with(this)
                            .load(file)
                            .apply(new RequestOptions().fitCenter())
                            .into(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                try {
                    file = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    body.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close, 0);
                    photoConstraint.setVisibility(View.VISIBLE);
                    Glide.with(this)
                            .load(file)
                            .apply(new RequestOptions().fitCenter())
                            .into(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    @Override
    public void onBackPressed() {
        finish();
        timer.cancel();
        super.onBackPressed();
    }

    @Override
    public void getAll(AllMessage allMessage) {
        if (allMessage != null) {
            if (allMessage.getMessageList() != null) {
                messages.clear();
                messages.addAll(allMessage.getMessageList());
                if (check) {
                    adapter = new MessageListAdapter(this, messages, userId);
                    recyclerView.setAdapter(adapter);
                    check = false;
                }
                try {
                    adapter.notifyDataSetChanged();
                } catch (NullPointerException e) {
                    e.getMessage();
                }
            }
        }
    }

    @Override
    public void showProgress(boolean isProgress) {

    }

    @Override
    public void send(String success) {
        body.setText("");
        getMessagePresenter.getMessage(token, new ChatId(getIntent().getIntExtra("id", 0)));
        file = null;
    }

    @Override
    public void showError(String error) {

    }
}
