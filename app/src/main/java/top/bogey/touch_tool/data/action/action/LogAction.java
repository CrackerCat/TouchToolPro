package top.bogey.touch_tool.data.action.action;

import android.content.Context;
import android.os.Parcel;

import top.bogey.touch_tool.MainAccessibilityService;
import top.bogey.touch_tool.MainApplication;
import top.bogey.touch_tool.R;
import top.bogey.touch_tool.data.TaskRepository;
import top.bogey.touch_tool.data.TaskRunnable;
import top.bogey.touch_tool.data.WorldState;
import top.bogey.touch_tool.data.action.NormalAction;
import top.bogey.touch_tool.data.pin.Pin;
import top.bogey.touch_tool.data.pin.object.PinBoolean;
import top.bogey.touch_tool.data.pin.object.PinObject;
import top.bogey.touch_tool.data.pin.object.PinString;

public class LogAction extends NormalAction {
    private final Pin<? extends PinObject> textPin;
    private final Pin<? extends PinObject> toastPin;

    public LogAction(Context context) {
        super(context, R.string.action_log_action_title);
        textPin = addPin(new Pin<>(new PinString(), context.getString(R.string.action_log_action_subtitle_tips)));
        toastPin = addPin(new Pin<>(new PinBoolean(false), context.getString(R.string.action_log_action_subtitle_toast)));
    }

    public LogAction(Parcel in) {
        super(in);
        textPin = addPin(pinsTmp.remove(0));
        toastPin = addPin(pinsTmp.remove(0));
    }

    @Override
    protected void doAction(WorldState worldState, TaskRunnable runnable, Pin<? extends PinObject> pin) {
        PinString pinString = (PinString) getPinValue(worldState, runnable.getTask(), textPin);

        MainAccessibilityService service = MainApplication.getService();
        TaskRepository.getInstance().addLog(runnable.getTask(), runnable.getStartAction().getTitle().toString(), pinString.getValue());

        PinBoolean showToast = (PinBoolean) getPinValue(worldState, runnable.getTask(), toastPin);
        if (showToast.getValue()) {
            service.showToast(pinString.getValue());
        }
        super.doAction(worldState, runnable, outPin);
    }
}
