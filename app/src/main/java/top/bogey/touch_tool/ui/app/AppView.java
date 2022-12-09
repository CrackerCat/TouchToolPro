package top.bogey.touch_tool.ui.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Map;

import top.bogey.touch_tool.data.TaskHelper;
import top.bogey.touch_tool.databinding.ViewAppBinding;
import top.bogey.touch_tool.utils.ResultCallback;
import top.bogey.touch_tool.utils.TextChangedListener;

public class AppView extends BottomSheetDialogFragment {
    public final static int SINGLE_MODE = 0;
    public final static int MULTI_MODE = 1;

    private final Map<CharSequence, List<CharSequence>> packages;
    private final int mode;
    private ResultCallback callback;

    private CharSequence searchText = "";
    private boolean showSystem = false;

    public AppView(Map<CharSequence, List<CharSequence>> packages, int mode, ResultCallback callback) {
        assert (mode == SINGLE_MODE || mode == MULTI_MODE);
        this.packages = packages;
        this.mode = mode;
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewAppBinding binding = ViewAppBinding.inflate(inflater, container, false);
        AppRecyclerViewAdapter adapter = new AppRecyclerViewAdapter(packages, result -> {
            if (mode == SINGLE_MODE) {
                if (callback != null) {
                    callback.onResult(result);
                    callback = null;
                    dismiss();
                }
            }
        });
        binding.appIconBox.setAdapter(adapter);
        adapter.refreshApps(TaskHelper.getInstance().findPackageList(requireContext(), showSystem, searchText, mode == MULTI_MODE));

        binding.exchangeButton.setOnClickListener(v -> {
            showSystem = !showSystem;
            adapter.refreshApps(TaskHelper.getInstance().findPackageList(requireContext(), showSystem, searchText, mode == MULTI_MODE));
        });

        binding.searchEdit.addTextChangedListener(new TextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                searchText = s;
                adapter.refreshApps(TaskHelper.getInstance().findPackageList(requireContext(), showSystem, searchText, mode == MULTI_MODE));
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (callback != null) callback.onResult(true);
    }
}