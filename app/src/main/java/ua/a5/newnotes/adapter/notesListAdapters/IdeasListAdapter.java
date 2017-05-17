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
import ua.a5.newnotes.dto.notesDTO.IdeaDTO;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class IdeasListAdapter extends RecyclerView.Adapter<IdeasListAdapter.IdeasViewHolder> {

    public interface IdeaClickListener {
        void onClick(IdeaDTO ideaDTO);
    }

    //Локальный слушатель для адаптера.
    public interface ItemClickListener {
        void onClick(int position);
    }

    private Context context;
    //хранилище данных.
    private List<IdeaDTO> ideasDTOList;
    private IdeaClickListener ideaClickListener;
    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onClick(int position) {
            //TODO
            ideaClickListener.onClick(ideasDTOList.get(position));
        }
    };

    public IdeasListAdapter(Context context,
                            List<IdeaDTO> ideasDTOList,
                            IdeaClickListener ideaClickListener
    ) {
        this.context = context;
        this.ideasDTOList = ideasDTOList;
        this.ideaClickListener = ideaClickListener;
    }

    @Override
    public IdeasListAdapter.IdeasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes_ideas, parent, false);
        return new IdeasListAdapter.IdeasViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(IdeasListAdapter.IdeasViewHolder holder, int position) {
        IdeaDTO item = ideasDTOList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        holder.tvDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return ideasDTOList.size();
    }

    public static class IdeasViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;

        ItemClickListener itemClickListener;

        public IdeasViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_ideas);
            tvTitle = (TextView) itemView.findViewById(R.id.title_ideas);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description_ideas);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_ideas);

            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }

    public void setIdeasDTOList(List<IdeaDTO> ideasDTOList) {
        this.ideasDTOList = ideasDTOList;
    }
}
