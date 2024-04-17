package com.example.d308_mobile_app.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308_mobile_app.R;
import com.example.d308_mobile_app.entities.Excursion;

import java.util.List;

// Adapter for displaying excursion data in a RecyclerView
public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private final LayoutInflater mInflater;
    // Context is used to inflate the layout and initiate activities
    private final Context context;
    // List of Excursion entities to be displayed
    private List<Excursion> mExcursions;

    // ViewHolder class for Excursion items
    static class ExcursionViewHolder extends RecyclerView.ViewHolder {
        // TextView to display the title of the Excursion
        final TextView excursionItemView;

        // Constructor for the ViewHolder, to set up the TextView and click listener
        ExcursionViewHolder(View itemView, final Context context, final List<Excursion> mExcursions) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView3);
            // Set click listener for each item in the RecyclerView
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Excursion current = mExcursions.get(position);
                // Start ExcursionDetails activity and pass required data
                Intent intent = new Intent(context, ExcursionDetails.class);
                intent.putExtra("id", current.getExcursionID());
                intent.putExtra("title", current.getExcursionTitle());
                intent.putExtra("vacationID", current.getVacationID());
                intent.putExtra("excursionDate", current.getExcursionDate());
                context.startActivity(intent);
            });
        }
    }

    // Constructor for the ExcursionAdapter
    public ExcursionAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout for each item in the RecyclerView
        View itemView = mInflater.inflate(R.layout.item_excursion, parent, false);
        return new ExcursionViewHolder(itemView, context, mExcursions);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        // Bind data to the TextView for each item
        if (mExcursions != null && mExcursions.size() > position) {
            Excursion current = mExcursions.get(position);
            holder.excursionItemView.setText(current.getExcursionTitle());
        } else {
            // Show default text if no excursions are available
            holder.excursionItemView.setText("No excursion title available");
        }
    }


    // Setter for the list of excursions
    public void setExcursions(List<Excursion> excursions) {
        mExcursions = excursions;
        // Notify the adapter to reflect the new set of excursions
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        // Return the count of excursions if not null, otherwise 0
        return (mExcursions != null) ? mExcursions.size() : 0;
    }
}
