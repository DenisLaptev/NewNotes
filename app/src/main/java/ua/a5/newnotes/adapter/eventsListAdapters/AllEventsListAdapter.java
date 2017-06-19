package ua.a5.newnotes.adapter.eventsListAdapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.CreateEventActivity;
import ua.a5.newnotes.dto.eventsDTO.EventDTO;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_BEGIN_DAY;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_BEGIN_MONTH;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_BEGIN_YEAR;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_DESCRIPTION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_TITLE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_NAME;
import static ua.a5.newnotes.R.id.delete_item;
import static ua.a5.newnotes.R.id.update_item;
import static ua.a5.newnotes.utils.Constants.KEY_UPDATE_EVENTS;
import static ua.a5.newnotes.utils.Constants.flagWhenItemDeletedAll;
import static ua.a5.newnotes.utils.Constants.flagWhenItemDeletedThisMonth;
import static ua.a5.newnotes.utils.Constants.flagWhenItemDeletedToday;
import static ua.a5.newnotes.utils.Constants.isCardForUpdate;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class AllEventsListAdapter extends RecyclerView.Adapter<AllEventsListAdapter.EventsViewHolder> {


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

    public AllEventsListAdapter(Context context,
                                List<EventDTO> eventsDTOList,
                                EventClickListener eventClickListener) {
        this.context = context;
        this.eventsDTOList = eventsDTOList;
        this.eventClickListener = eventClickListener;
    }

    @Override
    public AllEventsListAdapter.EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events_all, parent, false);
        return new AllEventsListAdapter.EventsViewHolder(view, itemClickListener);

    }

    @Override
    public void onBindViewHolder(final AllEventsListAdapter.EventsViewHolder holder, final int position) {

        final EventDTO item = eventsDTOList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        holder.tvDate.setText(item.getDay() + "-" + (item.getMonth() + 1) + "-" + item.getYear());
        holder.ivPictureEventMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //deleteItem(position, eventsDTOList);

                PopupMenu cardPopupMenu = new PopupMenu(context, holder.ivPictureEventMenu);
                cardPopupMenu.getMenuInflater().inflate(R.menu.menu_card, cardPopupMenu.getMenu());

                cardPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem it) {

                        switch (it.getItemId()) {
                            case delete_item:

                                //AlertDialog.Builder builder = new AlertDialog.Builder(StartMenuActivity.this, R.style.MyAlertDialogStyle);
                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                                builder.setTitle("Delete?");
                                builder.setMessage("Do You Really Want To Delete?");

                                //positive button.
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                                        deleteItem(position, eventsDTOList);
                                        notifyItemRemoved(position);
                                    }

                                });

                                //negative button.
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }

                                });
                                builder.show();

                                flagWhenItemDeletedToday = true;
                                flagWhenItemDeletedThisMonth = true;
                                flagWhenItemDeletedAll = true;
                                break;

                            case update_item:
                                isCardForUpdate = true;
                                Intent intent = new Intent(context, CreateEventActivity.class);
                                intent.putExtra(KEY_UPDATE_EVENTS, item);
                                context.startActivity(intent);
                                Toast.makeText(context, it.getTitle(), Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                break;
                        }
                        return true;
                    }
                });
                cardPopupMenu.show();
            }
        });
    }


    private void deleteItem(int position, List<EventDTO> eventsDTOList) {
        int currentPosition = position;
        //
        deleteItemFromTable(position, eventsDTOList);
        notifyItemRemoved(currentPosition);
        eventsDTOList.remove(currentPosition);
        notifyItemRemoved(currentPosition);
        //setEventsDTOList(AllEventsFragment.getInstance(context).getAllEventsList());

    }


    private void deleteItemFromTable(int position, List<EventDTO> eventsDTOList) {
        int currentPosition = position;

        //////////////////---------------------->
        //для работы с БД.
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_EVENTS_NAME,
                TABLE_EVENTS_KEY_TITLE + " = ? AND "
                        + TABLE_EVENTS_KEY_BEGIN_DAY + " = ? AND "
                        + TABLE_EVENTS_KEY_BEGIN_MONTH + " = ? AND "
                        + TABLE_EVENTS_KEY_BEGIN_YEAR + " = ? AND "
                        + TABLE_EVENTS_KEY_DESCRIPTION + " = ? ",
                new String[]{
                        eventsDTOList.get(currentPosition).getTitle(),
                        String.valueOf(eventsDTOList.get(currentPosition).getDay()),
                        String.valueOf(eventsDTOList.get(currentPosition).getMonth()),
                        String.valueOf(eventsDTOList.get(currentPosition).getYear()),
                        eventsDTOList.get(currentPosition).getDescription()
                });

        //закрываем соединение с БД.
        dbHelper.close();
//////////////////---------------------->
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
        ImageView ivPictureEventMenu;

        ItemClickListener itemClickListener;

        public EventsViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_events_all);
            tvTitle = (TextView) itemView.findViewById(R.id.title_events_all);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description_events_all);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_events_all);
            ivPictureEventMenu = (ImageView) itemView.findViewById(R.id.event_card_menu);

            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }

    public void setEventsDTOList() {


    }
}
