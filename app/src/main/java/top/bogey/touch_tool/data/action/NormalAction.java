package top.bogey.touch_tool.data.action;

import android.content.Context;
import android.os.Parcel;

import androidx.annotation.StringRes;

import top.bogey.touch_tool.data.action.BaseAction;

public class NormalAction extends BaseAction {

    public NormalAction(Context context, @StringRes int titleId) {
        super(context, titleId);
        addPin(inPin);
        addPin(outPin);
    }

    public NormalAction(Parcel in) {
        super(in);
        inPin = addPin(pinsTmp.remove(0));
        outPin = addPin(pinsTmp.remove(0));
    }
}
