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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.CreateNoteTODOActivity;
import ua.a5.newnotes.dto.notesDTO.TodoDTO;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_DAY;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_DESCRIPTION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_MONTH;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_TITLE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_YEAR;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_NAME;
import static ua.a5.newnotes.R.id.delete_item;
import static ua.a5.newnotes.R.id.update_item;
import static ua.a5.newnotes.utils.Constants.isCardForUpdate;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {

    public static final String KEY_UPDATE_TODO = "key_update_todo";

    public interface TodoClickListener {
        void onClick(TodoDTO todoDTO);
    }

    //Локальный слушатель для адаптера.
    public interface ItemClickListener {
        void onClick(int position);
    }

    private Context context;
    //хранилище данных.
    private List<TodoDTO> todoDTOList;
    private TodoClickListener todoClickListener;
    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onClick(int position) {
            //TODO
            todoClickListener.onClick(todoDTOList.get(position));
        }
    };

    public TodoListAdapter(Context context,
                           List<TodoDTO> todoDTOList,
                           TodoClickListener todoClickListener
    ) {
        this.context = context;
        this.todoDTOList = todoDTOList;
        this.todoClickListener = todoClickListener;
    }

    @Override
    public TodoListAdapter.TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes_todo, parent, false);
        return new TodoListAdapter.TodoViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(final TodoListAdapter.TodoViewHolder holder, final int position) {
        final TodoDTO item = todoDTOList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.chbxTodo.setText("ВЫПОЛНЕНО");
        if(item.getIsDone()==0){
            holder.chbxTodo.setChecked(true);
        }else{
            holder.chbxTodo.setChecked(true);
        }

        holder.tvDate.setText(item.getDay() + "-" + item.getMonth() + "-" +item.getYear());

        holder.ivPictureTodoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteItem(position, todoDTOList);
                PopupMenu cardPopupMenu = new PopupMenu(context, holder.ivPictureTodoMenu);
                cardPopupMenu.getMenuInflater().inflate(R.menu.menu_card, cardPopupMenu.getMenu());

                cardPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem it) {

                        switch (it.getItemId()) {
                            case delete_item:
                                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                                deleteItem(position, todoDTOList);
                                break;

                            case update_item:
                                Toast.makeText(context, "update", Toast.LENGTH_SHORT).show();

                                isCardForUpdate = true;
                                Intent intent = new Intent(context, CreateNoteTODOActivity.class);
                                intent.putExtra(KEY_UPDATE_TODO, item);
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

    private void deleteItem(int position, List<TodoDTO> todoDTOList) {
        int currentPosition = position;
        //
        deleteItemFromTable(position, todoDTOList);
        notifyItemRemoved(currentPosition);
        todoDTOList.remove(currentPosition);
        notifyItemRemoved(currentPosition);
    }


    private void deleteItemFromTable(int position, List<TodoDTO> todoDTOList) {
        int currentPosition = position;

        //////////////////---------------------->
        //для работы с БД.
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NOTES_TODO_NAME,
                TABLE_NOTES_TODO_KEY_TITLE + " = ? AND "
                        + TABLE_NOTES_TODO_KEY_DAY + " = ? AND "
                        + TABLE_NOTES_TODO_KEY_MONTH + " = ? AND "
                        + TABLE_NOTES_TODO_KEY_YEAR + " = ? AND "
                        + TABLE_NOTES_TODO_KEY_DESCRIPTION + " = ? ",
                new String[]{
                        todoDTOList.get(currentPosition).getTitle(),
                        String.valueOf(todoDTOList.get(currentPosition).getDay()),
                        String.valueOf(todoDTOList.get(currentPosition).getMonth()),
                        String.valueOf(todoDTOList.get(currentPosition).getYear()),
                        String.valueOf(todoDTOList.get(currentPosition).getDescription())
                });

        //закрываем соединение с БД.
        dbHelper.close();
//////////////////---------------------->
    }

    @Override
    public int getItemCount() {
        return todoDTOList.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvTitle;
        CheckBox chbxTodo;
        TextView tvDate;

        ImageView ivPictureTodoMenu;

        ItemClickListener itemClickListener;

        public TodoViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_todo);
            tvTitle = (TextView) itemView.findViewById(R.id.title_todo);
            chbxTodo = (CheckBox)itemView.findViewById(R.id.chbx_todo);
            tvDate = (TextView)itemView.findViewById(R.id.tv_date_todo);
            ivPictureTodoMenu = (ImageView) itemView.findViewById(R.id.todo_card_menu);


            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }

    public void setData(List<TodoDTO> todoDTOList) {
        this.todoDTOList = todoDTOList;
    }


}
