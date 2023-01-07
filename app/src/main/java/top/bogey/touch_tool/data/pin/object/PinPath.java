package top.bogey.touch_tool.data.pin.object;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.bogey.touch_tool.ui.setting.SettingSave;
import top.bogey.touch_tool.utils.DisplayUtils;
import top.bogey.touch_tool.utils.easy_float.FloatGravity;

public class PinPath extends PinValue {
    private final ArrayList<TouchPath> paths = new ArrayList<>();
    private final int screen;
    private final FloatGravity gravity;
    private final Point offset;

    public PinPath() {
        super();
        screen = 1080;
        gravity = FloatGravity.TOP_LEFT;
        offset = new Point();
    }

    public PinPath(Parcel in) {
        in.readTypedList(paths, TouchPath.CREATOR);
        screen = in.readInt();
        gravity = FloatGravity.valueOf(in.readString());
        offset = in.readParcelable(Point.class.getClassLoader());
    }

    public PinPath(ArrayList<TouchPath> paths, int screen, FloatGravity gravity, Point offset) {
        this.paths.addAll(paths);
        this.screen = screen;
        this.gravity = gravity;
        this.offset = offset;
    }

    public List<Path> getRealPaths(Context context, boolean fixed) {
        List<Path> paths = new ArrayList<>();
        List<TouchPath> touchPaths = getPaths(context);
        for (TouchPath touchPath : touchPaths) {
            paths.add(touchPath.getPath(fixed));
        }
        return paths;
    }

    public List<TouchPath> getPaths(Context context) {
        Point start = getStartScreenPoint(context);
        int width = DisplayUtils.getScreen(context);
        float scale = width * 1f / screen;

        List<TouchPath> pathList = new ArrayList<>();
        for (TouchPath path : paths) {
            pathList.add(new TouchPath(path.points, start, scale));
        }
        return pathList;
    }

    private Point getStartScreenPoint(Context context) {
        int width = DisplayUtils.getScreen(context);
        float scale = width * 1f / screen;
        Point start = new Point((int) (offset.x * scale), (int) (offset.y * scale));
        Point size = DisplayUtils.getScreenSize(context);
        switch (gravity) {
            case TOP_LEFT:
                break;
            case TOP_RIGHT:
                start.x = start.x + size.x;
                break;
            case BOTTOM_LEFT:
                start.y = start.y + size.y;
                break;
            case BOTTOM_RIGHT:
                start.x = start.x + size.x;
                start.y = start.y + size.y;
                break;
        }
        return start;
    }

    public FloatGravity getGravity() {
        return gravity;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return paths.toString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(paths);
        dest.writeInt(screen);
        dest.writeString(gravity.name());
        dest.writeParcelable(offset, flags);
    }


    public static class TouchPath implements Parcelable {
        private transient int pointerId = -1;
        private List<Point> points = new ArrayList<>();

        public TouchPath() {
        }

        public TouchPath(Parcel in) {
            points = in.createTypedArrayList(Point.CREATOR);
        }

        public TouchPath(List<Point> points, Point offset, float scale) {
            for (Point point : points) {
                this.points.add(new Point((int) (point.x * scale) + offset.x, (int) (point.y * scale) + offset.y));
            }
        }

        public void addPoint(int x, int y) {
            if (points.size() != 0) {
                Point point = points.get(points.size() - 1);
                if (point.x == x && point.y == y) return;
            }
            points.add(new Point(x, y));
        }

        public void offset(int x, int y) {
            points.forEach(point -> point.set(point.x + x, point.y + y));
        }

        public void toLine(){
            if (points.size() > 2) {
                setPoints(Arrays.asList(points.get(0), points.get(points.size() - 1)));
            }
        }

        public static final Creator<TouchPath> CREATOR = new Creator<TouchPath>() {
            @Override
            public TouchPath createFromParcel(Parcel in) {
                return new TouchPath(in);
            }

            @Override
            public TouchPath[] newArray(int size) {
                return new TouchPath[size];
            }
        };

        public Path getPath(boolean fixed) {
            Path tmp = null;
            for (Point point : points) {
                int x = SettingSave.getInstance().getTouchOffset(fixed, point.x);
                int y = SettingSave.getInstance().getTouchOffset(fixed, point.y);
                if (tmp == null) {
                    tmp = new Path();
                    tmp.moveTo(Math.max(x, 0), Math.max(y, 0));
                } else {
                    tmp.lineTo(Math.max(x, 0), Math.max(y, 0));
                }
            }
            return tmp;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeTypedList(points);
        }

        public int getPointerId() {
            return pointerId;
        }

        public void setPointerId(int pointerId) {
            this.pointerId = pointerId;
        }

        public List<Point> getPoints() {
            return points;
        }

        public void setPoints(List<Point> points) {
            this.points = points;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TouchPath path = (TouchPath) o;

            return points.equals(path.points);
        }

        @Override
        public int hashCode() {
            return points.hashCode();
        }
    }
}