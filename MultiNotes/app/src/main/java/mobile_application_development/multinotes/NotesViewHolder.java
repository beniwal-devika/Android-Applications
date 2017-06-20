package mobile_application_development.multinotes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by devikabeniwal on 24/02/17.
 */

public class NotesViewHolder extends RecyclerView.ViewHolder{
    protected TextView titleText;
    protected TextView notesText;
    protected TextView datetimeText;


    public NotesViewHolder(View itemview) {
        super(itemview);
        titleText = (TextView) itemView.findViewById(R.id.title);
        datetimeText = (TextView) itemView.findViewById(R.id.datetime);
        notesText = (TextView) itemView.findViewById(R.id.notes);


    }
}
