package mobile_application_development.know_your_government;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by devikabeniwal on 04/04/17.
 */

public class ListViewHolder extends RecyclerView.ViewHolder{
    public TextView name;
    public TextView designation;
    //public TextView party;

    public ListViewHolder(View view) {
        super(view);
        name = (TextView) view.findViewById(R.id.name);
        designation = (TextView) view.findViewById(R.id.designation);
    }
}
