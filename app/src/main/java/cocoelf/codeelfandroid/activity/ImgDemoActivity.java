package cocoelf.codeelfandroid.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.longsh.optionframelibrary.OptionCenterDialog;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.activity.helper.ImageHelper;
@EActivity
public class ImgDemoActivity extends AppCompatActivity {
    private VisionServiceClient client;
    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_SELECT_IMAGE_IN_ALBUM = 1;
    // The URI of photo taken from gallery
    private Uri mUriPhotoTaken;
    // File of the photo taken with camera
    private File mFilePhotoTaken;
    // The URI of the image selected to detect.
    private Uri mImageUri;
    // The image selected to detect.
    private Bitmap mBitmap;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEditText=(EditText)findViewById(R.id.editTextResult);
        initVisionServiceClient();
    }

    private void initVisionServiceClient(){
        if (client==null){
            client = new VisionServiceRestClient(getString(R.string.subscription_key), getString(R.string.subscription_apiroot));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.photo) {
            final ArrayList<String> list = new ArrayList<>();
            list.add((this.getString(R.string.take_photo)));
            list.add((this.getString(R.string.select_image_in_album)));
            final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
            optionCenterDialog.show(ImgDemoActivity.this, list);
            optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(id==0){
                        takePhoto(view);
                    }else if(id==1){
                        selectImageInAlbum(view);
                    }
                        optionCenterDialog.dismiss();

                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // When the button of "Take a Photo with Camera" is pressed.
    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            // Save the photo taken to a temporary file.
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                mFilePhotoTaken = File.createTempFile(
                        "IMG_",  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Create the File where the photo should go
                // Continue only if the File was successfully created
                if (mFilePhotoTaken != null) {
                    mUriPhotoTaken = FileProvider.getUriForFile(this,
                            "cocoelf.codeelfandroid.fileprovider",
                            mFilePhotoTaken);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);

                    // Finally start camera activity
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "错误信息：" + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    // When the button of "Select a Photo in Album" is pressed.
    public void selectImageInAlbum(View view) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 打开手机相册,设置请求码
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM);
    }

    // Deal with the result of selection of the photos and faces.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    mImageUri=Uri.fromFile(mFilePhotoTaken);
                }
                break;
            case REQUEST_SELECT_IMAGE_IN_ALBUM:
                if (resultCode == RESULT_OK) {
                    if (data == null || data.getData() == null) {
                        mImageUri = mUriPhotoTaken;
                    } else {
                        mImageUri = data.getData();
                    }
                }
                break;
            default:
                break;
        }
        mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(mImageUri, getContentResolver());
        if (mBitmap != null) {
            // Show the image on screen.
            ImageView imageView = (ImageView) findViewById(R.id.selectedImage);
            imageView.setImageBitmap(mBitmap);

            // Add detection log.
            Log.d("AnalyzeActivity", "Image: " + mImageUri + " resized to " + mBitmap.getWidth()
                    + "x" + mBitmap.getHeight());

            doRecognize();
        }
    }


    public void doRecognize() {

        mEditText.setText("Analyzing...");

        try {
           process();
        } catch (Exception e)
        {
            mEditText.setText("Error encountered. Exception is: " + e.toString());
        }
    }

    @Background
    public void process() {
        Exception e=null;
        String result="";
        Gson gson = new Gson();
        try {

            // Put the image into an input stream for detection.
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

            OCR ocr;
            ocr = this.client.recognizeText(inputStream, LanguageCodes.ChineseSimplified, true);

            result = gson.toJson(ocr);
            Log.d("result", result);
        }catch (Exception e1) {
                e = e1;    // Store error
        }

        Log.d("result", result);
        onPostExecute(result,e);

    }

    @UiThread
    public void onPostExecute(String data,Exception e) {
        // Display based on error existence

        if (e != null) {
            mEditText.setText("Error: " + e.getMessage());
        } else {
            Gson gson = new Gson();
            OCR r = gson.fromJson(data, OCR.class);

            String result = "";
            for (Region reg : r.regions) {
                for (Line line : reg.lines) {
                    for (Word word : line.words) {
                        result += word.text + " ";
                    }
                    result += "\n";
                }
                result += "\n\n";
            }

            mEditText.setText(result);
        }
    }

}
