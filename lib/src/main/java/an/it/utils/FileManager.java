package an.it.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class FileManager {
    public static final int DEFAULT_BUFFER_SIZE_IN_BYTES = 8 * 1024;
   // public static float IMAGE_SIZE = 204800F;//200kb

    public static boolean upload(String url, String from, String to) throws Exception {
        return uploadFile(url, from, to);
    }

    public static boolean upload(String url, String from, String to, float width, float height) throws Exception {
        return uploadImage(url, from, to, width, height);

    }

    public static File downloadFile(String requestUrl, String path) throws Exception {
        File mFile = null;
        InputStream reader = null;
        FileOutputStream output = null;
        byte buffer[] = new byte[DEFAULT_BUFFER_SIZE_IN_BYTES];
        URL url = new URL(requestUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            reader = new BufferedInputStream(urlConnection.getInputStream());
            output = new FileOutputStream(path);
            int readByte = 0;
            synchronized (buffer) {
                while ((readByte = reader.read(buffer)) != -1) {
                    output.write(buffer, 0, readByte);
                }
                output.flush();
                output.close();
                reader.close();
            }
            return mFile;
        } catch (Exception e) {
            urlConnection.disconnect();
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static boolean deleteFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    public static void copy(File src, File dst) throws Exception {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[DEFAULT_BUFFER_SIZE_IN_BYTES];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static Bitmap resizeImageBitmap(String path, int width, int height) {
        float scale = 1;
        // decode full image
        Bitmap bitmapOriginal = BitmapFactory.decodeFile(path);
        if (bitmapOriginal.getWidth() > width) scale = bitmapOriginal.getWidth() / width;
        if (bitmapOriginal.getHeight() > height) {
            float tmp = bitmapOriginal.getHeight() / height;
            if (tmp > scale) scale = tmp;
        }

        // resize bitmap
        Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmapOriginal, (int) (bitmapOriginal.getWidth() / scale), (int) (bitmapOriginal.getHeight() / scale), true);
        bitmapOriginal.recycle();
        return bitmapsimplesize;
    }

    private static ByteArrayInputStream resizeImage(String path, float width, float height) throws IOException {
        float scale = 1;
        // decode full image
        Bitmap bitmapOriginal = BitmapFactory.decodeFile(path);
        if (bitmapOriginal.getWidth() > width) scale = bitmapOriginal.getWidth() / width;
        if (bitmapOriginal.getHeight() > height) {
            float tmp = bitmapOriginal.getHeight() / height;
            if (tmp > scale) scale = tmp;
        }

        // resize bitmap
        Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmapOriginal, (int) (bitmapOriginal.getWidth() / scale), (int) (bitmapOriginal.getHeight() / scale), true);
      /*  Log.i("WIDTH=", bitmapOriginal.getWidth() / scale+"");
	    Log.i("HEIGHT=", bitmapOriginal.getHeight() / scale+"");*/
        // save image
        //int size=bitmapsimplesize.getRowBytes() * bitmapsimplesize.getHeight();
        //int quality=100;
	   /* if(size>IMAGE_SIZE){
	    	quality=(int)((IMAGE_SIZE/size)*100);
	    	 Log.i("QUALITY=", quality+"");
	    }*/
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//	    bitmapsimplesize.compress(CompressFormat.JPEG,quality /*ignored for PNG*/, bos); 
        bitmapsimplesize.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        bitmapOriginal.recycle();
        bitmapsimplesize.recycle();
        return new ByteArrayInputStream(bitmapdata);
    }

    private static boolean uploadFile(String webUpload, String from, String to) throws Exception {
        boolean result = false;
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        //	DataInputStream inputStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;

        FileInputStream fileInputStream;
        fileInputStream = new FileInputStream(new File(from));
        URL url = new URL(webUpload + "?folder=" + to);
        //Toast.makeText(con, Configs.WEB_UPLOAD+"?folder="+to,Toast.LENGTH_LONG).show();
        Log.i("AAAAA", webUpload + "?folder=" + to);
        Log.i("BBBBB", from);
        connection = (HttpURLConnection) url.openConnection();

        // Allow Inputs & Outputs
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("folder", to);

        // Enable POST method
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + from + "\"" + lineEnd);
        outputStream.writeBytes(lineEnd);

        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        // Read file
        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            outputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

        // Responses from the server (code and message)
        Log.i("UPLOAD", connection.getResponseCode() + " --- " + connection.getResponseMessage());
        if (connection.getResponseMessage().equals("OK")) result = true;
        else result = false;


        fileInputStream.close();
        outputStream.flush();
        outputStream.close();
        return result;
    }

    private static boolean uploadImage(String webUpload, String from, String to, float width, float height) throws Exception {
        boolean result = false;
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        //	DataInputStream inputStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;
        ByteArrayInputStream fileInputStream = resizeImage(from, width, height);

        URL url = new URL(webUpload + "?folder=" + to);
        //Toast.makeText(con, Configs.WEB_UPLOAD+"?folder="+to,Toast.LENGTH_LONG).show();
        Log.i("AAAAA", webUpload + "?folder=" + to);
        Log.i("BBBBB", from);
        connection = (HttpURLConnection) url.openConnection();

        // Allow Inputs & Outputs
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("folder", to);

        // Enable POST method
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + from + "\"" + lineEnd);
        outputStream.writeBytes(lineEnd);

        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        // Read file
        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            outputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

        // Responses from the server (code and message)
        Log.i("UPLOAD", connection.getResponseCode() + "");
        if (connection.getResponseMessage().equals("OK")) result = true;
        else result = false;
        fileInputStream.close();
        outputStream.flush();
        outputStream.close();
        return result;

    }
}
