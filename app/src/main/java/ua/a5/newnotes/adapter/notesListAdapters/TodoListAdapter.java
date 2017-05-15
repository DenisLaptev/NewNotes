package ua.a5.newnotes.adapter.notesListAdapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.notesDTO.TodoDTO;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {

    //хранилище данных.
    private List<TodoDTO> todoData;

    public TodoListAdapter(List<TodoDTO> todoData) {
        this.todoData = todoData;
    }

    @Override
    public TodoListAdapter.TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes_todo, parent, false);
        return new TodoListAdapter.TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoListAdapter.TodoViewHolder holder, int position) {
        TodoDTO item = todoData.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.chbxTodo.setText(item.getTodo());
        holder.tvDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return todoData.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvTitle;
        CheckBox chbxTodo;
        TextView tvDate;

        public TodoViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_todo);
            tvTitle = (TextView) itemView.findViewById(R.id.title_todo);
            chbxTodo = (CheckBox)itemView.findViewById(R.id.chbx_todo);
            tvDate = (TextView)itemView.findViewById(R.id.tv_date_todo);
        }
    }

    public void setData(List<TodoDTO> todoData) {
        this.todoData = todoData;
    }
}