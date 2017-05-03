package an.it.disasterassistancesystem.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.adapters.GalleryAdapter;
import an.it.disasterassistancesystem.objects.PictureSelectItem;
import an.it.utils.FileManager;

public class TakePictureActivity extends Activity {
    public static final int CAPTURE_IMAGE = 3;
    public static final int SELECT_PHOTO = 1;
    public static boolean PICTURE_NORMAL = true;
    public static boolean PICTURE_SCRACH = false;
    public static final String CAMERA_DATA = "camera_data";
    private static Uri fileUri;
    private GridView gallery;
    private GalleryAdapter adapter;
    private ImageButton btnDelete;
    private ArrayList<PictureSelectItem> listImage;
    private String ID = "";
    private TextView direct, help;
    private boolean type;
    private String mediaStorageDir;
    private String timeStamp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_take_picture);
        mediaStorageDir = "sdcard/Prop Manager/pictures/";
        gallery = (GridView) findViewById(R.id.gallery);
        btnDelete = (ImageButton) findViewById(R.id.btn_remove_picture);
        direct = (TextView) findViewById(R.id.pic_directory);
        help = (TextView) findViewById(R.id.pic_help);
        direct.setSelected(true);
        help.setSelected(true);
        listImage = (ArrayList<PictureSelectItem>) getLastNonConfigurationInstance();
        if (listImage == null) {
            listImage = getIntent().getParcelableArrayListExtra(CAMERA_DATA);
        }
        //get list
        ID = getIntent().getStringExtra("ID");
        type = getIntent().getBooleanExtra("TYPE", PICTURE_NORMAL);
        //set direct
        direct.setText(mediaStorageDir);
        //adapter
        adapter = new GalleryAdapter(this, listImage);
        gallery.setAdapter(adapter);
        gallery.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
                if (adapter.isChoose()) {
                    PictureSelectItem item = adapter.getItem(pos);
                    if (item.isChecked()) {
                        item.setChecked(false);
                        resetAllChoose();
                    } else {
                        item.setChecked(true);
                    }
                    adapter.notifyDataSetInvalidated();
                } else {
                    adapter.showFullImage(pos);
                }
            }

        });
        gallery.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View v, int pos, long id) {
                if (!adapter.isChoose()) {
                    adapter.setChoose(true);
                    btnDelete.setVisibility(View.VISIBLE);
                } else {
                    PictureSelectItem item = adapter.getItem(pos);
                    if (item.isChecked()) {
                        item.setChecked(false);
                        resetAllChoose();
                    } else {
                        item.setChecked(true);
                    }
                }
                adapter.notifyDataSetInvalidated();
                return false;
            }

        });
        resetAllChoose();
    }

    private void resetAllChoose() {

        if (listImage != null) {
            boolean check = false;
            for (PictureSelectItem item : listImage) {
                if (item.isChecked()) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                adapter.setChoose(false);
                btnDelete.setVisibility(View.GONE);
            } else {
                btnDelete.setVisibility(View.VISIBLE);
                adapter.setChoose(true);
            }
        }


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case CAPTURE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    listImage.add(new PictureSelectItem(fileUri.getPath(), "IMG_" + timeStamp + ".jpg", false));
                    adapter.notifyDataSetInvalidated();
                }
                break;
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK && data != null) {
                    File folder = new File(mediaStorageDir);
                    folder.mkdirs();
                    timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
                    fileUri = Uri.fromFile(new File(mediaStorageDir + File.separator + File.separator + "IMG_"
                            + timeStamp + ".jpg"));

                    new AsyncTask<Void, Void, String>() {
                        ProgressDialog pd;

                        @Override
                        public void onPreExecute() {
                            pd = new ProgressDialog(TakePictureActivity.this);
                            pd.setMessage("Loading data...");
                            pd.show();
                            pd.setCancelable(false);
                        }

                        @Override
                        protected String doInBackground(Void... params) {
                            try {
                                FileManager.copy(new File(getPath(data.getData())), new File(fileUri.getPath()));
                                return "success";
                            } catch (Exception e) {
                                return e.getMessage();
                            }

                        }

                        protected void onPostExecute(String result) {
                            pd.dismiss();
                            if ("success".equals(result)) {
                                listImage.add(new PictureSelectItem(fileUri.getPath(), "IMG_" + timeStamp + ".jpg", false));
                                adapter.notifyDataSetInvalidated();
                            } else {
                                Toast.makeText(TakePictureActivity.this, result, Toast.LENGTH_LONG).show();
                            }
                        }

                        ;

                    }.execute();

                }
                break;
        }
    }

    public void onTakePicture(View v) {
        startCameraIntent();
    }

    public void onOpen(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public void onRemove(View v) {
        for (int i = 0; i < listImage.size(); i++) {
            PictureSelectItem item = listImage.get(i);
            if (item.isChecked()) {
                FileManager.deleteFiles(item.getLinkSDCard());
                listImage.remove(i);
                i--;
            }
        }
        adapter.notifyDataSetInvalidated();
        resetAllChoose();
    }

    @Override
    public void onBackPressed() {
        if (adapter.isChoose()) {
            for (PictureSelectItem item : listImage) {
                item.setChecked(false);
            }
            adapter.setChoose(false);
            adapter.notifyDataSetInvalidated();
        } else {
           /* Intent i = new Intent();
            i.putParcelableArrayListExtra(InventoryInformationActivity.CAMERA_DATA, listImage);
            i.putExtra("ID", ID);
            i.putExtra("TYPE", type);
            setResult(RESULT_OK, i);
            this.finish();*/
        }
    }

    private void startCameraIntent() {
        //String mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        File folder = new File(mediaStorageDir);
        folder.mkdirs();
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        fileUri = Uri.fromFile(new File(mediaStorageDir + File.separator + File.separator + "IMG_"
                + timeStamp + ".jpg"));

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(cameraIntent, CAPTURE_IMAGE);
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return listImage;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

}