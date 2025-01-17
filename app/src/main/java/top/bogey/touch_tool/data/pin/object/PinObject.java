package top.bogey.touch_tool.data.pin.object;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import top.bogey.touch_tool.utils.DisplayUtils;

public class PinObject implements Parcelable {
    private final String cls;

    public PinObject() {
        cls = getClass().getName();
    }

    public PinObject(Parcel in) {
        cls = getClass().getName();
    }

    public static final Creator<PinObject> CREATOR = new Creator<PinObject>() {
        @Override
        public PinObject createFromParcel(Parcel in) {
            String cls = in.readString();
            try {
                Class<?> aClass = Class.forName(cls);
                Constructor<?> constructor = aClass.getConstructor(Parcel.class);
                Object o = constructor.newInstance(in);
                return (PinObject) o;
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public PinObject[] newArray(int size) {
            return new PinObject[size];
        }
    };

    public int getPinColor(Context context) {
        return DisplayUtils.getAttrColor(context, com.google.android.material.R.attr.colorPrimaryInverse, 0);
    }

    public ShapeAppearanceModel getPinStyle(Context context) {
        int cornerSize = DisplayUtils.dp2px(context, 6);
        return ShapeAppearanceModel.builder()
                .setTopLeftCorner(CornerFamily.ROUNDED, cornerSize)
                .setTopRightCorner(CornerFamily.ROUNDED, cornerSize)
                .setBottomLeftCorner(CornerFamily.ROUNDED, cornerSize)
                .setBottomRightCorner(CornerFamily.ROUNDED, cornerSize)
                .build();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(cls);
    }
}
