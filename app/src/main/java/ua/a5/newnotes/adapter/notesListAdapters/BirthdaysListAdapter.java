package ua.a5.newnotes.adapter.notesListAdapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.notesDTO.BirthdaysDTO;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class BirthdaysListAdapter extends RecyclerView.Adapter<BirthdaysListAdapter.BirthdaysViewHolder> {

    //хранилище данных.
    private List<BirthdaysDTO> birthdaysData;

    public BirthdaysListAdapter(List<BirthdaysDTO> birthdaysData) {
        this.birthdaysData = birthdaysData;
    }

    @Override
    public BirthdaysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes_birthdays, parent, false);
        return new BirthdaysViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BirthdaysViewHolder holder, int position) {
        BirthdaysDTO item = birthdaysData.get(position);
        holder.tvName.setText(item.getName());
        holder.tvDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return birthdaysData.size();
    }

    public static class BirthdaysViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvName;
        TextView tvDate;

        public BirthdaysViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_birthdays);
            tvName = (TextView) itemView.findViewById(R.id.title_birthdays);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_birthdays);
        }
    }

    public void setBirthdaysData(List<BirthdaysDTO> birthdaysData) {
        this.birthdaysData = birthdaysData;
    }
}
