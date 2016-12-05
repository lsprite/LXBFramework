package com.lxb.view.fileselectlibrary.util;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

/**
 * 
 * @author qzyd
 * 功能:图片压缩和合成
 */
public class ImageCompression {

	private final String TAG ="ImageCompression";
	private static ImageCompression imgCompression = null; 
	private ImageCompression(){};
	public static synchronized ImageCompression getInstance()
	{
		if(imgCompression == null)
		{
			imgCompression = new ImageCompression();
		}
		return imgCompression;
	}
	
	/**
	 * 质量压缩一张图片
	 * @param bmp 要压缩图片
	 * @return  Bitmap
	 */
	public void qualityPress(Context context,Bitmap bmp,String path,int compressRatio)
	{
		if(null == bmp){
			return;
		}
		try {
			Log.e(TAG, "path == "+path);
			File exsitFile = new File(path);
			if(exsitFile.exists()){
				exsitFile.delete();
			}
			exsitFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(exsitFile);
			bmp.compress(CompressFormat.JPEG, compressRatio, fos);
			fos.flush();
			fos.close();
//			if (!bmp.isRecycled()) {
//				bmp.recycle();
//			}
//			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse(
//					"file://"+ CameraUtil.mPhotoPath)));
			Log.e(TAG, "压缩比例 ："+compressRatio);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 图片缩略图按比例压缩
	 * @param path  图片文件路径
	 * @return  Bitmap
	 */
	public Bitmap scalePressImage(String path,int minSideLength,int maxNumOfPixels)
	{
		Bitmap bmp = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			bmp = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, opts);
			int be = computeSampleSize(opts,minSideLength,maxNumOfPixels);
			Log.e(TAG, "be ---> "+be);
			opts.inSampleSize = be;
			opts.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, opts);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bmp;
	}
		
	
	/**
	 * 
	 * @param options  生成原始Bitmap
	 * @param minSideLength  生成的缩略图的宽高中的较小的值 
	 * @param maxNumOfPixels 生成缩略图的总像素
	 * @return
	 */
	public  int computeSampleSize(BitmapFactory.Options options,    
            int minSideLength, int maxNumOfPixels) {    
        int initialSize = computeInitialSampleSize(options, minSideLength,    
                maxNumOfPixels);    
        Log.e(TAG, "initialSize = "+initialSize); 
        int roundedSize;    
        if (initialSize <= 8) {    
            roundedSize = 1;    
            while (roundedSize < initialSize) {    
                roundedSize <<= 1;    
                Log.e(TAG, "roundedSize <<= 1 ----> "+roundedSize); 
            }    
            Log.e(TAG, "roundedSize < 8 ----> "+roundedSize); 
        } else {    
            roundedSize = (initialSize + 7) / 8 * 8;  
            Log.e(TAG, "roundedSize > 8 ----> "+roundedSize); 
        }    
         
        return roundedSize;    
    }    
    
	/**
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
    private  int computeInitialSampleSize(BitmapFactory.Options options,    
            int minSideLength, int maxNumOfPixels) {    
        double w = options.outWidth;    
        double h = options.outHeight;    
        Log.e(TAG, "w ----> "+w);
        Log.e(TAG, "h ----> "+h);
        Log.e(TAG, "w*h ----> "+w*h + " w * h / maxNumOfPixels ----> "+(w * h / maxNumOfPixels));
        int lowerBound = (maxNumOfPixels == -1) ? 1 :    
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels)); 
        Log.e(TAG, "lowerBound = "+lowerBound);
        int upperBound = (minSideLength == -1) ? 128 :    
                (int) Math.min(Math.floor(w / minSideLength),    
                Math.floor(h / minSideLength));    
        Log.e(TAG, "upperBound = "+upperBound);
        if (upperBound < lowerBound) {    
            // return the larger one when there is no overlapping zone.
        	 Log.e(TAG, "upperBound < lowerBound = "+lowerBound); 
            return lowerBound;    
        }    
       
        if ((maxNumOfPixels == -1) &&    
                (minSideLength == -1)) {   
        	Log.e(TAG, "maxNumOfPixels == -1 && minSideLength == -1");
            return 1;    
        } else if (minSideLength == -1) {  
        	Log.e(TAG, "minSideLength == -1");
            return lowerBound;    
        } else {    
        	Log.e(TAG, "maxNumOfPixels == -1");
            return upperBound;    
        }
        
    }       
	
    /**
     * 将图片转成字符串
     * @param bitmap
     * @return
     */
    public  String getImageBinary(Bitmap bitmap) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 85, baos);
			String uploadBuffer = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
			File file = new File(Environment.getExternalStorageDirectory() + "/gg.txt");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(uploadBuffer.getBytes(), 0, uploadBuffer.getBytes().length);
			baos.flush();
			baos.close();
			fos.flush();
			fos.close();
			return uploadBuffer;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
    
	/**
	 * 图片打水印
	 * @param bmpSrc 底图
	 * @param bmpMark  水印图片
	 * @param strMark  字体标记
	 * @return
	 */
	public Bitmap mergeImage(Context context,Bitmap bmpSrc,String strMark,
			String path,String name,int compressRatio)
	{
		if(bmpSrc != null)
		{

			int srcWidth = bmpSrc.getWidth();   //底图宽度
			int srcHeight = bmpSrc.getHeight();	//底图高度					
			Bitmap newBmp = Bitmap.createBitmap(srcWidth, srcHeight, Config.ARGB_8888);
			Canvas canvas = new Canvas(newBmp);//创建画布
			canvas.drawBitmap(bmpSrc, 0, 0, null);
			if(!TextUtils.isEmpty(strMark))
			{
//				Paint strPaint = new Paint();
				TextPaint txtPaint = new TextPaint();
				txtPaint.setColor(Color.WHITE);
				Typeface font = Typeface.create("宋体", Typeface.NORMAL);
				txtPaint.setTypeface(font);
				txtPaint.setTextSize(13.0f);
				txtPaint.setTextScaleX(1.05f);
				canvas.drawText(strMark, 5 , srcHeight - 10, txtPaint);
//				StaticLayout  layout = new StaticLayout(strMark,txtPaint,srcWidth,Alignment.,1.5f,0,false);
//				layout.draw(canvas);
			}
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
			saveImage(context,newBmp,name,compressRatio);
			return newBmp;
		}else
		{
			return null;
		}
	}
	
	/**
	 * 图片打水印
	 * @param bmpSrc 底图
	 * @param bmpMark  水印图片
	 * @param strMark  字体标记
	 * @return
	 */
	public Bitmap mergeImage(Context context,Bitmap bmpSrc,String mark1,
			String mark2,String path,String name,int compressRatio)
	{
		if(bmpSrc != null)
		{

			int srcWidth = bmpSrc.getWidth();   //底图宽度
			int srcHeight = bmpSrc.getHeight();	//底图高度					
			Bitmap newBmp = Bitmap.createBitmap(srcWidth, srcHeight, Config.ARGB_8888);
			Canvas canvas = new Canvas(newBmp);//创建画布
			canvas.drawBitmap(bmpSrc, 0, 0, null);			
			
			if(!TextUtils.isEmpty(mark1) && !"null".equals(mark1))
			{
				Paint strPaint = new Paint();
				strPaint.setColor(Color.WHITE);
				Typeface font = Typeface.create("宋体", Typeface.NORMAL);
				strPaint.setTypeface(font);
				strPaint.setTextSize(13.0f);
				strPaint.setTextScaleX(1.05f);
				canvas.drawText(mark1, 5 , srcHeight - 55, strPaint);
			}
			if(!TextUtils.isEmpty(mark2) && !"null".equals(mark2))
			{
				Paint strPaint = new Paint();
				strPaint.setColor(Color.WHITE);
				Typeface font = Typeface.create("宋体", Typeface.NORMAL);
				strPaint.setTypeface(font);
				strPaint.setTextSize(13.0f);
				strPaint.setTextScaleX(1.05f);
				int length = mark2.length();
				if(length >= 15){
					String h1 = mark2.substring(0, 15);
					canvas.drawText(h1, 5 , srcHeight - 40, strPaint);
					
					int h2_len = length - 15;
					if(h2_len >= 15){
						String h2 = mark2.substring(15, 30);
						canvas.drawText(h2, 5 , srcHeight - 25, strPaint);
						int h3_len = length - 30;
						if(h3_len >= 15){
							String h3 = mark2.substring(30, length);
							canvas.drawText(h3, 5 , srcHeight - 10, strPaint);
						}
					}else{
						String h2 = mark2.substring(15, length);
						canvas.drawText(h2, 5 , srcHeight - 25, strPaint);
					}
				}else{
					canvas.drawText(mark2, 5 , srcHeight - 40, strPaint);
				}
				
			}
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
			saveImage(context,newBmp,name,compressRatio);
			return newBmp;
		}else
		{
			return null;
		}
	}
	
	public void saveImage(Context context,Bitmap bmp,String name,int compressRatio)
	{
		Log.e(TAG, "合成水印compressRatio == "+compressRatio);
		String filePath = Environment.
				getExternalStorageDirectory()+File.separator+"DCIM"+File.separator+"Camera";
		try
		{
			File f = new File(filePath);
			if(f.exists()){
				Log.v(TAG, "---->水印存储文件存在");
				File file = new File(filePath,name);
				if(file.exists()){
				   Log.v(TAG, "----> 文件存在");
				   file.delete();	
				}else{
				   file.createNewFile();
				   Log.v(TAG, "----> 文件不存在");
				}
				FileOutputStream fos = new FileOutputStream(file);
				boolean isCompress = bmp.compress(CompressFormat.JPEG, compressRatio, fos);
				if(isCompress)
				{
					fos.flush();
					fos.close();
					context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse(
							"file://"+ Environment.getExternalStorageDirectory())));
				}
			}else{
				Log.v(TAG, "---->水印存储文件不存在");
				f.mkdirs();
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
}
