package io.uplink.metashare;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

public class UploadActivity extends AppCompatActivity {

    private EditText mSubjectView;
    private ImageView mImagePreviewView;
    private static final String TAG = "MetaShareUpload";
    private static final long MAX_IMAGE_SIZE = 30 * 1024 * 1024; // 30 Mebibytes.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Set up the upload form.
        mSubjectView = (EditText)findViewById(R.id.upload_subject);
        mImagePreviewView = (ImageView)findViewById(R.id.upload_image_preview);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (action.equals(Intent.ACTION_SEND) && type != null) {
            if (type.startsWith("image/")) {
                handleSendImage(intent);
            } else {
                // Something went wrong!
            }
        }
    }

    private void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        Log.d(TAG, "Shared Image URI: " + imageUri.toString());
        Cursor fileCursor = getContentResolver().query(imageUri, null, null, null, null);

        int nameIndex = fileCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = fileCursor.getColumnIndex(OpenableColumns.SIZE);

        fileCursor.moveToFirst();

        String fileName = fileCursor.getString(nameIndex);
        long fileSize = fileCursor.getLong(sizeIndex);

        mImagePreviewView.setImageURI(imageUri);
        mSubjectView.setHint(fileName);

        Log.d(TAG, "Image name: " + fileName);
        Log.d(TAG, "Image size: " + fileSize);
    }
}
