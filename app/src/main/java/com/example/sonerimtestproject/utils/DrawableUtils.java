package com.example.sonerimtestproject.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.example.sonerimtestproject.model.GeoPointModel;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import org.osmdroid.util.GeoPoint;

import java.util.Objects;

public class DrawableUtils {

    public static void setColorToVectorMasterView(@NonNull VectorMasterView view,
                                                  @NonNull String path, int color) {
        PathModel outline = view.getPathModelByName(path);
        if (outline != null) outline.setFillColor(color);
        view.update();
    }

    public static Drawable getDrawableFromVectorWithCustomColor(
            Context context, @DrawableRes int drawResId,
            String pathName, @ColorRes int colorResId) {
        VectorMasterDrawable vectorMasterDrawable = new VectorMasterDrawable(context, drawResId);
        PathModel pathModel = vectorMasterDrawable.getPathModelByName(pathName);
        if (pathModel != null) pathModel.setFillColor(context.getResources().getColor(colorResId));
        return vectorMasterDrawable;
    }

    public static Drawable getDrawable(Context context, @DrawableRes int drawRes,
                                       @ColorRes int colorRes, int maxDpSize) {
        Drawable drawable = context.getDrawable(drawRes);
        int color = context.getResources().getColor(colorRes);
        Bitmap bitmap = ((BitmapDrawable) Objects.requireNonNull(drawable)).getBitmap();
        int newWith, newHeight;
        float ratio;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > height) {
            ratio = (float) width / (float) height;
            newWith = convertDpToPixel(maxDpSize);
            newHeight = (int) (newWith / ratio);
        } else {
            ratio = (float) height / (float) width;
            newHeight = convertDpToPixel(maxDpSize);
            newWith = (int) (newHeight / ratio);
        }

        Drawable result = new BitmapDrawable(
                context.getResources(),
                Bitmap.createScaledBitmap(bitmap, newWith, newHeight, true));
        result.setTint(color);
        return result;
    }


    public static int convertDpToPixel(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static GeoPoint convertPointModelToPointGeo(GeoPointModel geoPointModel) {
        return new GeoPoint(geoPointModel.getLatitude(), geoPointModel.getLongitude());
    }

}