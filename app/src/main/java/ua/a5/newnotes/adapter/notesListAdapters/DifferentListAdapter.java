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
import ua.a5.newnotes.dto.notesDTO.DifferentDTO;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class DifferentListAdapter extends RecyclerView.Adapter<DifferentListAdapter.DifferentViewHolder> {

    public interface DifferentClickListener {
        void onClick(DifferentDTO differentDTO);
    }

    //Локальный слушатель для адаптера.
    public interface ItemClickListener {
        void onClick(int position);
    }

    private Context context;
    //хранилище данных.
    private List<DifferentDTO> differentDTOList;
    private DifferentClickListener differentClickListener;
    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onClick(int position) {
            //TODO
            differentClickListener.onClick(differentDTOList.get(position));
        }
    };


    public DifferentListAdapter(Context context,
                                List<DifferentDTO> differentDTOList,
                                DifferentClickListener differentClickListener
    ) {
        this.context = context;
        this.differentDTOList = differentDTOList;
        this.differentClickListener = differentClickListener;
    }

    @Override
    public DifferentListAdapter.DifferentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes_different, parent, false);
        return new DifferentListAdapter.DifferentViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(DifferentListAdapter.DifferentViewHolder holder, int position) {
        DifferentDTO item = differentDTOList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        holder.tvDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return differentDTOList.size();
    }

    public static class DifferentViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;

        ItemClickListener itemClickListener;

        public DifferentViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_different);
            tvTitle = (TextView) itemView.findViewById(R.id.title_different);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description_different);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_different);

            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }

    public void setDifferentDTOList(List<DifferentDTO> differentDTOList) {
        this.differentDTOList = differentDTOList;
    }
}
