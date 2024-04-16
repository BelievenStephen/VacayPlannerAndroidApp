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
import com.example.d308_mobile_app.entities.Vacation;
import java.util.List;

// Adapter for displaying vacation data in a RecyclerView
public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;
    private List<Vacation> mVacations;

    // Constructor initializing the inflater and context
    public VacationAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    // ViewHolder that contains the UI components
    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.textView2);

            // Set an onClickListener to handle user interaction with the vacation item
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    final Vacation current = mVacations.get(position);
                    Intent intent = new Intent(context, VacationDetails.class);
                    intent.putExtra("id", current.getVacationId());
                    intent.putExtra("title", current.getVacationTitle());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_vacation, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        if (mVacations != null) {
            Vacation current = mVacations.get(position);
            holder.vacationItemView.setText(current.getVacationTitle());
        } else {
            holder.vacationItemView.setText("No vacation title");
        }
    }

    @Override
    public int getItemCount() {
        return (mVacations != null) ? mVacations.size() : 0;
    }

    // Method to update the data in the RecyclerView
    public void setVacations(List<Vacation> vacations) {
        mVacations = vacations;
        notifyDataSetChanged();
    }
}
