package ua.a5.newnotes.adapter.eventsListAdapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.eventsDTO.EventsDTO;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.EventsViewHolder> {

    //хранилище данных.
    private List<EventsDTO> eventsData;

    public EventsListAdapter(List<EventsDTO> eventsData) {
        this.eventsData = eventsData;
    }

    @Override
    public EventsListAdapter.EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events, parent, false);
        return new EventsListAdapter.EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsListAdapter.EventsViewHolder holder, int position) {
        EventsDTO item = eventsData.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        holder.tvDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return eventsData.size();
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;

        public EventsViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_events);
            tvTitle = (TextView) itemView.findViewById(R.id.title_events);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description_events);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_events);
        }
    }

    public void setEventsData(List<EventsDTO> eventsData) {
        this.eventsData = eventsData;
    }
}
