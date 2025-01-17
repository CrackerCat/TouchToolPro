package top.bogey.touch_tool.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.bogey.touch_tool.R;
import top.bogey.touch_tool.data.TaskRepository;
import top.bogey.touch_tool.databinding.ViewTaskTabItemBinding;
import top.bogey.touch_tool.utils.SettingSave;

public class TagRecyclerViewAdapter extends RecyclerView.Adapter<TagRecyclerViewAdapter.ViewHolder> {

    private final HomeView parent;
    private final List<String> tags;
    private final String ALL;
    private final String NO;

    public TagRecyclerViewAdapter(HomeView taskView) {
        parent = taskView;
        tags = SettingSave.getInstance().getTags(taskView.requireContext());
        ALL = parent.getString(R.string.tag_all);
        NO = parent.getString(R.string.tag_no);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ViewTaskTabItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.refreshView(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public void addTag(String tag) {
        tags.add(tags.size() - 1, tag);
        notifyItemInserted(tags.indexOf(tag));
        parent.addTab(tag);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewTaskTabItemBinding binding;
        private final Context context;

        public ViewHolder(ViewTaskTabItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();

            binding.getRoot().setOnClickListener(v -> parent.selectTab(getAdapterPosition()));

            binding.deleteButton.setOnClickListener(v -> {
                int index = getAdapterPosition();
                TaskRepository.getInstance().removeTag(tags.remove(index));
                parent.removeTab(index);
                notifyItemRemoved(index);
            });
        }

        public void refreshView(String tag) {
            binding.appName.setText(tag);
            binding.deleteButton.setVisibility((tag.equals(ALL) || tag.equals(NO)) ? View.INVISIBLE : View.VISIBLE);
        }
    }
}
