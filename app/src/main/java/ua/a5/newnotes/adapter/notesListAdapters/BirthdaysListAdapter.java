package ua.a5.newnotes.adapter.notesListAdapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import ua.a5.newnotes.activities.CreateNoteBirthdaysActivity;
import ua.a5.newnotes.dto.notesDTO.BirthdayDTO;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_KEY_DAY;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_KEY_MONTH;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_KEY_NAME;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_KEY_YEAR;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_NAME;
import static ua.a5.newnotes.R.id.delete_item;
import static ua.a5.newnotes.R.id.update_item;
import static ua.a5.newnotes.utils.Constants.isCardForUpdate;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class BirthdaysListAdapter extends RecyclerView.Adapter<BirthdaysListAdapter.BirthdaysViewHolder> {

    public static final String KEY_UPDATE_BIRTHDAYS = "key_update_birthdays";

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
    public void onBindViewHolder(final BirthdaysViewHolder holder, final int position) {
        final BirthdayDTO item = birthdaysDTOList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvDate.setText(item.getDay() + " " + item.getStringMonth());
        holder.ivPictureBirthdayMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteItem(position, birthdaysDTOList);

                PopupMenu cardPopupMenu = new PopupMenu(context, holder.ivPictureBirthdayMenu);
                cardPopupMenu.getMenuInflater().inflate(R.menu.menu_card, cardPopupMenu.getMenu());

                cardPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem it) {

                        switch (it.getItemId()) {
                            case delete_item:
                                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                                deleteItem(position, birthdaysDTOList);
                                break;

                            case update_item:
                                Toast.makeText(context, "update", Toast.LENGTH_SHORT).show();

                                isCardForUpdate = true;
                                Intent intent = new Intent(context, CreateNoteBirthdaysActivity.class);
                                intent.putExtra(KEY_UPDATE_BIRTHDAYS, item);
                                context.startActivity(intent);
                                Toast.makeText(context, it.getTitle(), Toast.LENGTH_SHORT).show();


                                break;
                        }
                        return true;
                    }
                });
                cardPopupMenu.show();
            }
        });
    }


    private void deleteItem(int position, List<BirthdayDTO> birthdaysDTOList) {
        int currentPosition = position;
        //
        deleteItemFromTable(position, birthdaysDTOList);
        notifyItemRemoved(currentPosition);
        birthdaysDTOList.remove(currentPosition);
        notifyItemRemoved(currentPosition);
    }


    private void deleteItemFromTable(int position, List<BirthdayDTO> birthdaysDTOList) {
        int currentPosition = position;

        //////////////////---------------------->
        //для работы с БД.
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NOTES_BIRTHDAYS_NAME,
                TABLE_NOTES_BIRTHDAYS_KEY_NAME + " = ? AND "
                        + TABLE_NOTES_BIRTHDAYS_KEY_DAY + " = ? AND "
                        + TABLE_NOTES_BIRTHDAYS_KEY_MONTH + " = ? AND "
                        + TABLE_NOTES_BIRTHDAYS_KEY_YEAR + " = ? ",
                new String[]{
                        birthdaysDTOList.get(currentPosition).getName(),
                        String.valueOf(birthdaysDTOList.get(currentPosition).getDay()),
                        String.valueOf(birthdaysDTOList.get(currentPosition).getMonth()),
                        String.valueOf(birthdaysDTOList.get(currentPosition).getYear())
                });

        //закрываем соединение с БД.
        dbHelper.close();
//////////////////---------------------->
    }

    @Override
    public int getItemCount() {
        return birthdaysDTOList.size();
    }

    public static class BirthdaysViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvName;
        TextView tvDate;
        ImageView ivPictureBirthdayMenu;

        ItemClickListener itemClickListener;

        public BirthdaysViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_birthdays);
            tvName = (TextView) itemView.findViewById(R.id.title_birthdays);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_birthdays);
            ivPictureBirthdayMenu = (ImageView) itemView.findViewById(R.id.birthdays_card_menu);

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
