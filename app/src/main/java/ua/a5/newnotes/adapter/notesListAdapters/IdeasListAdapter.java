package ua.a5.newnotes.adapter.notesListAdapters;

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
import ua.a5.newnotes.activities.CreateNoteIdeasActivity;
import ua.a5.newnotes.dto.notesDTO.IdeaDTO;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_IDEAS_KEY_DATE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_IDEAS_KEY_DESCRIPTION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_IDEAS_KEY_TITLE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_IDEAS_NAME;
import static ua.a5.newnotes.R.id.delete_item;
import static ua.a5.newnotes.R.id.update_item;
import static ua.a5.newnotes.utils.Constants.KEY_UPDATE_IDEAS;
import static ua.a5.newnotes.utils.Constants.isCardForUpdate;

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
    public void onBindViewHolder(final IdeasListAdapter.IdeasViewHolder holder, final int position) {
        final IdeaDTO item = ideasDTOList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        holder.tvDate.setText(item.getDate());
        holder.ivPictureIdeaMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteItem(position, ideasDTOList);
                PopupMenu cardPopupMenu = new PopupMenu(context, holder.ivPictureIdeaMenu);
                cardPopupMenu.getMenuInflater().inflate(R.menu.menu_card, cardPopupMenu.getMenu());

                cardPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem it) {

                        switch (it.getItemId()) {
                            case delete_item:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                                builder.setTitle("Delete?");
                                builder.setMessage("Do You Really Want To Delete?");

                                //positive button.
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteItem(position, ideasDTOList);
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
                                break;

                            case update_item:
                                isCardForUpdate = true;
                                Intent intent = new Intent(context, CreateNoteIdeasActivity.class);
                                intent.putExtra(KEY_UPDATE_IDEAS, item);
                                context.startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });
                cardPopupMenu.show();
            }
        });
    }

    private void deleteItem(int position, List<IdeaDTO> ideasDTOList) {
        int currentPosition = position;
        deleteItemFromTable(position, ideasDTOList);
        notifyItemRemoved(currentPosition);
        ideasDTOList.remove(currentPosition);
        notifyItemRemoved(currentPosition);
    }


    private void deleteItemFromTable(int position, List<IdeaDTO> ideasDTOList) {
        int currentPosition = position;

//////////////////---------------------->
        //для работы с БД.
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NOTES_IDEAS_NAME,
                TABLE_NOTES_IDEAS_KEY_TITLE + " = ? AND "
                        + TABLE_NOTES_IDEAS_KEY_DATE + " = ? AND "
                        + TABLE_NOTES_IDEAS_KEY_DESCRIPTION + " = ? ",
                new String[]{
                        ideasDTOList.get(currentPosition).getTitle(),
                        String.valueOf(ideasDTOList.get(currentPosition).getDate()),
                        String.valueOf(ideasDTOList.get(currentPosition).getDescription())
                });

        //закрываем соединение с БД.
        dbHelper.close();
//////////////////---------------------->
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
        ImageView ivPictureIdeaMenu;

        ItemClickListener itemClickListener;

        public IdeasViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_ideas);
            tvTitle = (TextView) itemView.findViewById(R.id.title_ideas);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description_ideas);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_ideas);
            ivPictureIdeaMenu = (ImageView) itemView.findViewById(R.id.ideas_card_menu);


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
