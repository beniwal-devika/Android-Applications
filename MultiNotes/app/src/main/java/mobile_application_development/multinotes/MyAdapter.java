package mobile_application_development.multinotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by devikabeniwal on 24/02/17.
 */

public class MyAdapter extends RecyclerView.Adapter<NotesViewHolder>  {
    private List<Notes> notesList;
    private MainActivity mainActivity;

    public MyAdapter(List<Notes> notesList, MainActivity main) {
        this.notesList = notesList;
        this.mainActivity = main;
    }

    public void updateList(List<Notes> list) {
        this.notesList.clear();
        this.notesList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        holder.titleText.setText(this.notesList.get(position).getTitle());
        holder.datetimeText.setText(this.notesList.get(position).getDatetime());

        String YourString = this.notesList.get(position).getNotes();
        if(YourString.length()>80){
            YourString  =  YourString.substring(0,79)+"...";
        }
        holder.notesText.setText(YourString);
    }

    @Override
    public int getItemCount() {
        return this.notesList.size();
    }
}
