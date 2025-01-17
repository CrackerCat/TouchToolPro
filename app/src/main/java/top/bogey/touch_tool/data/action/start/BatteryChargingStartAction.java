package top.bogey.touch_tool.data.action.start;

import android.content.Context;
import android.os.Parcel;

import top.bogey.touch_tool.R;
import top.bogey.touch_tool.data.Task;
import top.bogey.touch_tool.data.WorldState;
import top.bogey.touch_tool.data.pin.Pin;
import top.bogey.touch_tool.data.pin.object.PinObject;
import top.bogey.touch_tool.data.pin.object.PinSpinner;
import top.bogey.touch_tool.data.action.state.BatteryChargingStateAction;

public class BatteryChargingStartAction extends StartAction {
    private final Pin<? extends PinObject> statePin;
    private transient int currState;

    public BatteryChargingStartAction(Context context) {
        super(context, R.string.action_battery_charging_start_title);
        statePin = addPin(new Pin<>(new PinSpinner(R.array.charging_state), context.getString(R.string.action_battery_charging_start_subtitle_state)));
    }

    public BatteryChargingStartAction(Parcel in) {
        super(in);
        statePin = addPin(pinsTmp.remove(0));
    }

    @Override
    public boolean checkReady(WorldState worldState, Task task) {
        int batteryState = worldState.getBatteryState();

        PinSpinner value = (PinSpinner) getPinValue(worldState, task, statePin);
        if (BatteryChargingStateAction.convertToChargingState(value.getIndex()) != batteryState)
            return false;

        // 当前已经是了，不再执行
        if (currState == batteryState) return false;
        currState = batteryState;
        return true;
    }
}
