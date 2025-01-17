package top.bogey.touch_tool.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.UUID;

import top.bogey.touch_tool.R;
import top.bogey.touch_tool.data.action.BaseAction;
import top.bogey.touch_tool.data.action.start.StartAction;

public class Task implements Parcelable {
    private final String id;
    private final LinkedHashSet<BaseAction> actions = new LinkedHashSet<>();

    private final long createTime;
    private String tag;

    private String title;

    public Task() {
        id = UUID.randomUUID().toString();
        createTime = System.currentTimeMillis();
    }

    protected Task(Parcel in) {
        id = in.readString();
        ArrayList<BaseAction> actionArrayList = new ArrayList<>();
        in.readTypedList(actionArrayList, BaseAction.CREATOR);
        actions.addAll(actionArrayList);
        createTime = in.readLong();
        tag = in.readString();
        title = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public BaseAction getActionById(String id) {
        for (BaseAction action : actions) {
            if (action.getId().equals(id)) return action;
        }
        return null;
    }

    public ArrayList<StartAction> getStartActions(Class<? extends StartAction> startActionClass) {
        ArrayList<StartAction> startActions = new ArrayList<>();
        for (BaseAction action : actions) {
            if (startActionClass.isInstance(action)) {
                startActions.add((StartAction) action);
            }
        }
        return startActions;
    }

    public ArrayList<BaseAction> getActionsByClass(Class<? extends BaseAction> actionClass) {
        ArrayList<BaseAction> actions = new ArrayList<>();
        for (BaseAction action : this.actions) {
            if (actionClass.isInstance(action)) {
                actions.add(action);
            }
        }
        return actions;
    }

    public void addAction(BaseAction action) {
        actions.add(action);
    }

    public void removeAction(BaseAction action) {
        for (BaseAction baseAction : actions) {
            if (baseAction.getId().equals(action.getId())) {
                actions.remove(baseAction);
                break;
            }
        }
    }

    public String getTaskDes(Context context) {
        StringBuilder builder = new StringBuilder();
        for (StartAction startAction : getStartActions(StartAction.class)) {
            CharSequence title = startAction.getTitle();
            if (title == null) continue;
            builder.append(title);
            builder.append("(");
            if (startAction.isEnable()) {
                builder.append(context.getString(R.string.action_start_subtitle_enable_true));
            } else {
                builder.append(context.getString(R.string.action_start_subtitle_enable_false));
            }
            builder.append(")");
            builder.append("\n");
        }
        return builder.toString().trim();
    }

    public String getId() {
        return id;
    }

    public LinkedHashSet<BaseAction> getActions() {
        return actions;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeTypedList(new ArrayList<>(actions));
        dest.writeLong(createTime);
        dest.writeString(tag);
        dest.writeString(title);
    }
}
