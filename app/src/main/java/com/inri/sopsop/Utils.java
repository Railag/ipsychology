package com.inri.sopsop;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Railag on 03.06.2016.
 */
public class Utils {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static float dp2px(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    public static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(App.PREFS, Context.MODE_PRIVATE);
    }

    public static void hideKeyboard(@Nullable Activity act) {
        if (act != null && act.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        }
    }

    private final static double NANO = 1000000000;

    public static double calcTime(long startTime) {
        long currTime = System.nanoTime();
        long diff = currTime - startTime;

        return diff / NANO;
    }

    public enum PdfFormat {
        A2Portrait,
        A4Landscape
    }

    public static boolean canWrite(Context context) {
        return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void savePdf(Context context, View content, String fileName, PdfFormat format) {
        PrintAttributes.MediaSize mediaSize = null;
        switch (format) {
            case A2Portrait:
                mediaSize = PrintAttributes.MediaSize.ISO_A2.asPortrait();
                break;
            case A4Landscape:
            default:
                mediaSize = PrintAttributes.MediaSize.ISO_A4.asLandscape();
                break;
        }

        PrintAttributes attributes = new PrintAttributes.Builder()
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMediaSize(mediaSize)
                .setResolution(new PrintAttributes.Resolution(fileName, fileName, 300, 300))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        PrintedPdfDocument document = new PrintedPdfDocument(context,
                attributes);

        PdfDocument.Page page = document.startPage(0);

        content.draw(page.getCanvas());

        document.finishPage(page);

        if (!canWriteOnExternalStorage() || !canWrite(context)) {
            return;
        }

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + context.getString(R.string.app_name).toLowerCase());
        dir.mkdir();

        File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" +
                context.getString(R.string.app_name).toLowerCase() + "/" + fileName + ".pdf");

        String name = newFile.getAbsolutePath();

        try {
            OutputStream stream =
                    new FileOutputStream(newFile, true);
            document.writeTo(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();

        Toast.makeText(context, "Файл сохранен как " + name, Toast.LENGTH_SHORT).show();
    }

    public static boolean canWriteOnExternalStorage() {
        // get the state of your external storage
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // if storage is mounted return true
            return true;
        }
        return false;
    }
}
