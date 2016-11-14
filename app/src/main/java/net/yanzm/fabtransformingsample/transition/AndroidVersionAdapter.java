package net.yanzm.fabtransformingsample.transition;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AndroidVersionAdapter extends RecyclerView.Adapter {

    private static final String[] VERSIONS = {
            "Cupcake", "Donuts", "Eclair", "Froyo", "Gingerbread", "Honeycomb",
            "IceCreamSandwich", "JellyBean", "KitKat", "Lollipop", "Marshmallow",
            "Nougat"
    };

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.create(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.textView.setText(VERSIONS[position % VERSIONS.length]);
    }

    @Override
    public int getItemCount() {
        return VERSIONS.length * 10;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private static final int LAYOUT_ID = android.R.layout.simple_list_item_1;

        @NonNull
        static ViewHolder create(@NonNull LayoutInflater inflater, ViewGroup parent) {
            return new ViewHolder(inflater.inflate(LAYOUT_ID, parent, false));
        }

        private final TextView textView;

        private ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
