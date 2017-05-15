package ua.a5.newnotes.adapter.notesListAdapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.notesDTO.IdeasDTO;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class IdeasListAdapter extends RecyclerView.Adapter<IdeasListAdapter.IdeasViewHolder> {

    //хранилище данных.
    private List<IdeasDTO> ideasData;

    public IdeasListAdapter(List<IdeasDTO> ideasData) {
        this.ideasData = ideasData;
    }

    @Override
    public IdeasListAdapter.IdeasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes_ideas, parent, false);
        return new IdeasListAdapter.IdeasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IdeasListAdapter.IdeasViewHolder holder, int position) {
        IdeasDTO item = ideasData.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        holder.tvDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return ideasData.size();
    }

    public static class IdeasViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;

        public IdeasViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_ideas);
            tvTitle = (TextView) itemView.findViewById(R.id.title_ideas);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description_ideas);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_ideas);
        }
    }

    public void setIdeasData(List<IdeasDTO> ideasData) {
        this.ideasData = ideasData;
    }
}
