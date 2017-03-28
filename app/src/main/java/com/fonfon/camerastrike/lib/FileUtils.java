package com.fonfon.camerastrike.lib;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtils {

    private static final String DiR = "/CameraStrike";

    private static File getLocalDir() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                DiR);
    }

    private static String getNewFilePath(String filename) {
        return getLocalDir().getAbsolutePath() + "/" + filename + ".png";
    }

    public static File saveImageFile(byte[] data, String filename) {
        File dirDest = getLocalDir();
        File file;
        if (dirDest.exists()) {
            file = new File(getNewFilePath(filename));
        } else {
            if (dirDest.mkdir()) {
                file = new File(getNewFilePath(filename));
            } else {
                file = null;
            }
        }
        OutputStream os = null;
        if (file != null) {
            try {
                os = new FileOutputStream(file);
                os.write(data);
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return file;
        }
        return null;
    }

}
