package top.bogey.touch_tool.data;

import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.Future;

import top.bogey.touch_tool.data.action.start.StartAction;
import top.bogey.touch_tool.utils.TaskRunningCallback;

public class TaskRunnable implements Runnable {
    private final Task task;
    private final StartAction startAction;

    private final HashSet<TaskRunningCallback> callbacks = new HashSet<>();
    private int progress = 0;

    private Future<?> future;

    public TaskRunnable(Task task, StartAction startAction) {
        this.task = task;
        this.startAction = startAction;
    }

    public void stop() {
        future.cancel(true);
    }

    @Override
    public void run() {
        boolean result = false;
        try {
            callbacks.stream().filter(Objects::nonNull).forEach(taskRunningCallback -> taskRunningCallback.onStart(this));
            result = startAction.doAction(WorldState.getInstance(), this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boolean finalResult = result;
            callbacks.stream().filter(Objects::nonNull).forEach(taskRunningCallback -> taskRunningCallback.onEnd(this, finalResult));
        }
    }

    public void addCallback(TaskRunningCallback callback) {
        callbacks.add(callback);
    }

    public void removeCallback(TaskRunningCallback callback) {
        callbacks.remove(callback);
    }

    public void addProgress() {
        progress++;
        callbacks.stream().filter(Objects::nonNull).forEach(taskRunningCallback -> taskRunningCallback.onProgress(this, progress));
    }

    public Task getTask() {
        return task;
    }

    public StartAction getStartAction() {
        return startAction;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }
}
