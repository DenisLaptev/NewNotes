package ua.a5.newnotes.adapter.notesListAdapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.notesDTO.BirthdayDTO;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class BirthdaysListAdapter extends RecyclerView.Adapter<BirthdaysListAdapter.BirthdaysViewHolder> {

    public interface BirthdayClickListener {
        void onClick(BirthdayDTO birthdayDTO);
    }

    //Локальный слушатель для адаптера.
    public interface ItemClickListener {
        void onClick(int position);
    }

    private Context context;
    //хранилище данных.
    private List<BirthdayDTO> birthdaysDTOList;
    private BirthdayClickListener birthdayClickListener;
    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onClick(int position) {
            //TODO
            birthdayClickListener.onClick(birthdaysDTOList.get(position));
        }
    };

    public BirthdaysListAdapter(Context context,
                                List<BirthdayDTO> birthdaysDTOList,
                                BirthdayClickListener birthdayClickListener
    ) {
        this.context = context;
        this.birthdaysDTOList = birthdaysDTOList;
        this.birthdayClickListener = birthdayClickListener;
    }

    @Override
    public BirthdaysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes_birthdays, parent, false);
        return new BirthdaysViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(BirthdaysViewHolder holder, int position) {
        BirthdayDTO item = birthdaysDTOList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return birthdaysDTOList.size();
    }

    public static class BirthdaysViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvName;
        TextView tvDate;

        ItemClickListener itemClickListener;

        public BirthdaysViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_birthdays);
            tvName = (TextView) itemView.findViewById(R.id.title_birthdays);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_birthdays);

            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }

    public void setBirthdaysDTOList(List<BirthdayDTO> birthdaysDTOList) {
        this.birthdaysDTOList = birthdaysDTOList;
    }
}
