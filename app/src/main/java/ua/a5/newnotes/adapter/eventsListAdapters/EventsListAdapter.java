package ua.a5.newnotes.adapter.eventsListAdapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.eventsDTO.EventDTO;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.EventsViewHolder> {

    public interface EventClickListener {
        void onClick(EventDTO eventDTO);
    }

    //Локальный слушатель для адаптера.
    public interface ItemClickListener {
        void onClick(int position);
    }


    private Context context;
    //хранилище данных.
    private List<EventDTO> eventsDTOList;
    private EventClickListener eventClickListener;
    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onClick(int position) {
            //TODO
            eventClickListener.onClick(eventsDTOList.get(position));
        }
    };

    public EventsListAdapter(Context context,
                             List<EventDTO> eventsDTOList,
                             EventClickListener eventClickListener) {
        this.context = context;
        this.eventsDTOList = eventsDTOList;
        this.eventClickListener = eventClickListener;
    }

    @Override
    public EventsListAdapter.EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events, parent, false);
        return new EventsListAdapter.EventsViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(EventsListAdapter.EventsViewHolder holder, int position) {

        EventDTO item = eventsDTOList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        holder.tvDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return eventsDTOList.size();
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;

        ItemClickListener itemClickListener;

        public EventsViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_events);
            tvTitle = (TextView) itemView.findViewById(R.id.title_events);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description_events);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_events);

            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }

    public void setEventsDTOList(List<EventDTO> eventsDTOList) {
        this.eventsDTOList = eventsDTOList;
    }
}
