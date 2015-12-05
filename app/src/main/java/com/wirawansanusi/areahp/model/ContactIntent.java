package com.wirawansanusi.areahp.model;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

/**
 * Created by wirawansanusi on 12/5/15.
 */
public class ContactIntent {

    public static void showDirection(Context context, String location) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+ location));
        context.startActivity(intent);
    }

    public static void callNumber(Context context, String number) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                context.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            context.startActivity(intent);
        }
    }

    public static void openBBM(Context context, String pin) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://www.pin.bbm.com/"+ pin));
        context.startActivity(intent);
    }

    public static void composeEmail(Context context, String address, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + address)); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

}
