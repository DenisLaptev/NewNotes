package ua.a5.newnotes.adapter.notesListAdapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.notesDTO.DifferentDTO;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class DifferentListAdapter extends RecyclerView.Adapter<DifferentListAdapter.DifferentViewHolder> {

    //хранилище данных.
    private List<DifferentDTO> differentData;

    public DifferentListAdapter(List<DifferentDTO> differentData) {
        this.differentData = differentData;
    }

    @Override
    public DifferentListAdapter.DifferentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes_different, parent, false);
        return new DifferentListAdapter.DifferentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DifferentListAdapter.DifferentViewHolder holder, int position) {
        DifferentDTO item = differentData.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        holder.tvDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return differentData.size();
    }

    public static class DifferentViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;

        public DifferentViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_different);
            tvTitle = (TextView) itemView.findViewById(R.id.title_different);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description_different);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_different);
        }
    }

    public void setDifferentData(List<DifferentDTO> differentData) {
        this.differentData = differentData;
    }
}
