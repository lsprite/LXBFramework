package com.lxb.view.fileselectlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lxb.view.fileselectlibrary.photo.AlbumActivity;
import com.lxb.view.fileselectlibrary.photo.Bimp;
import com.lxb.view.fileselectlibrary.photo.ExDialog;
import com.lxb.view.fileselectlibrary.photo.GalleryActivity;
import com.lxb.view.fileselectlibrary.photo.ImageItem;
import com.lxb.view.fileselectlibrary.photo.Res;
import com.lxb.view.fileselectlibrary.util.FileUtil;
import com.lxb.view.fileselectlibrary.util.ImageCompression;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/5.
 */

public class ExampleActivity extends Activity {
    private static final int REQUEST_EX = 772;
    private static final int REQUEST_BROWSER = 773;
    private static final int REQUEST_LOCALIMAGE = 774;
    private static final int REQUEST_PHOTO = 775;
    private String photoName;
    private SelectPicPopupWindow selectPicPopupWindow;
    private MyGridView noScrollgridview;
    private GridAdapter adapter;
    private ArrayList<String> _files = new ArrayList<String>();
    //
    private Button btn_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        Res.init(this);
        Bimp.tempSelectBitmap.clear();
        noScrollgridview = (MyGridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    selectPicPopupWindow = new SelectPicPopupWindow(
                            ExampleActivity.this, itemsOnClick);
                    selectPicPopupWindow.showAtLocation(findViewById(R.id.main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    Intent intent = new Intent(ExampleActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivityForResult(intent, REQUEST_BROWSER);
                }
            }
        });
        //
        btn_file = (Button) findViewById(R.id.btn_file);
        btn_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("explorer_title",
                        getString(R.string.dialog_read_from_dir));
                intent.setDataAndType(Uri.fromFile(new File("/sdcard")), "*/*");
                intent.setClass(ExampleActivity.this, ExDialog.class);
                startActivityForResult(intent, REQUEST_EX);
            }
        });
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            if (selectPicPopupWindow != null) {
                selectPicPopupWindow.dismiss();
            }
            if (v.getId() == R.id.btn_take_photo) {
                photo();
            } else if (v.getId() == R.id.btn_pick_photo) {
                Intent intent = new Intent(ExampleActivity.this,
                        AlbumActivity.class);
                startActivityForResult(intent, REQUEST_LOCALIMAGE);
            }
        }
    };

    public void photo() {
        String SDPATH = Environment.getExternalStorageDirectory()
                + File.separator + "office";
        File pathFile = new File(SDPATH);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoName = getDateToPhotoName();
        String path = SDPATH + File.separator + photoName;
        File photoFile = new File(path);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photoFile));
        startActivityForResult(openCameraIntent, REQUEST_PHOTO);
    }

    private String getDateToPhotoName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        String token = dateFormat.format(date);
        return token + ".jpg";
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_PHOTO:
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
                    if (!TextUtils.isEmpty(photoName)) {
                        String SDPATH = Environment.getExternalStorageDirectory()
                                + File.separator + "office";
                        String filePath = SDPATH + File.separator + photoName;
                        File file = new File(filePath);
                        String tmpSdPath = Environment
                                .getExternalStorageDirectory()
                                + File.separator
                                + "tmp_office";
                        File tmpFolders = new File(tmpSdPath);
                        if (!tmpFolders.exists()) {
                            tmpFolders.mkdirs();
                        }
                        String tmpFilePath = tmpSdPath + File.separator + photoName;
                        File tmpFile = new File(tmpFilePath);
                        FileUtil.copyFile(file, tmpFile);
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setImagePath(tmpFilePath);
                        try {
                            Bitmap bm = Bimp.revitionImageSize(tmpFilePath);
                            ImageCompression.getInstance().qualityPress(
                                    getApplicationContext(), bm, tmpFilePath, 85);
                            takePhoto.setBitmap(bm);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Bimp.tempSelectBitmap.add(takePhoto);
                    }
                }
                adapter.update();
                break;
            case REQUEST_LOCALIMAGE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        boolean isAdd = data.getBooleanExtra("isAdd", false);
                        boolean isDel = data.getBooleanExtra("isDel", false);
                        if (isAdd) {
                            long size = data.getLongExtra("fileSize", 0);
                        }
                        if (isDel) {
                            long size = data.getLongExtra("fileSize", 0);
                        }
                    }
                }
                adapter.update();
                break;
            case REQUEST_BROWSER:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        boolean isDel = data.getBooleanExtra("isDel", false);
                        long delFileSize = data.getLongExtra("delFileSize", 0);
                        if (isDel) {
                        }
                    }
                }
                adapter.update();
                break;
            case REQUEST_EX:
                if (requestCode == REQUEST_EX) {
                    if (null != data) {
                        final Uri uri = data.getData();
                        if (_files.size() > 1) {
                            Toast.makeText(getApplicationContext(), "最多只能添加2个文件",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String str = Environment.getExternalStorageDirectory()
                                    + "/"
                                    + URLDecoder.decode(uri.toString().replace(
                                    "file:///sdcard/", ""));
                            _files.add(str);
                        }

                    }

                }
                break;

        }
    }

    //===========
    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
                        .getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }
}
