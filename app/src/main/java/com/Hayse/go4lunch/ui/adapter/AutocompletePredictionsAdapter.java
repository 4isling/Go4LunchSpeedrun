package com.Hayse.go4lunch.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.domain.entites.map_api.autocomplete.Prediction;

import java.util.List;

public class AutocompletePredictionsAdapter extends ListAdapter<Prediction, AutocompletePredictionsAdapter.ViewHolder> {
    List<Prediction> predictionList;
    private OnPredictionItemClickedListener onPredictionItemClickedListener;

    public AutocompletePredictionsAdapter() {
        super(new AutocompleteAdapterCallBack());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_autocomplete, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AutocompletePredictionsAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
        holder.itemView.setOnClickListener(v -> onPredictionItemClickedListener.onPredictionItemClicked(getItem(position)));
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ViewGroup layout;
        private final TextView predictionText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.autocomplete_item);
            predictionText = itemView.findViewById(R.id.prediction_name);
        }

        public void bind(@NonNull final Prediction item) {
            predictionText.setText(item.getDescription());
        }
    }

    private static class AutocompleteAdapterCallBack extends DiffUtil.ItemCallback<Prediction> {
        @Override
        public boolean areItemsTheSame(@NonNull Prediction oldItem, @NonNull Prediction newItem) {
            return oldItem.getPlaceId().equals(newItem.getPlaceId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Prediction oldItem, @NonNull Prediction newItem) {
            return oldItem.equals(newItem);
        }
    }

    public interface OnPredictionItemClickedListener {
        void onPredictionItemClicked(Prediction prediction);
    }
}
