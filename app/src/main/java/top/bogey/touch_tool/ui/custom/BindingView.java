package top.bogey.touch_tool.ui.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressLint("ViewConstructor")
public class BindingView<T extends ViewBinding> extends FrameLayout {
    protected T binding;

    public BindingView(@NonNull Context context, Class<T> tClass) {
        this(context, null, tClass);
    }

    public BindingView(@NonNull Context context, @Nullable AttributeSet attrs, Class<T> tClass) {
        super(context, attrs, 0);
        try {
            Method inflate = tClass.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            binding = (T) inflate.invoke(null, LayoutInflater.from(context), this, true);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
