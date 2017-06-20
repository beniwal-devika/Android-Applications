package mobile_application_development.know_your_government;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by devikabeniwal on 04/04/17.
 */

public class SenateListAdapter extends RecyclerView.Adapter<ListViewHolder>{

    private List<SenateList> senateList;
    private MainActivity mainAct;

    public SenateListAdapter(List<SenateList> list, MainActivity ma){
        this.senateList = list;
        this.mainAct = ma;
    }

    public void updateList(List<SenateList> list) {
        this.senateList.clear();
        this.senateList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view,parent,false);
        itemView.setOnClickListener(mainAct);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        SenateList senatelist = senateList.get(position);
        String party = senatelist.getParty();

        holder.designation.setText(String.valueOf(senatelist.getDesignation())+" ("+party+")");
        holder.name.setText(String.valueOf(senatelist.getName()));
    }

    @Override
    public int getItemCount() {
        return senateList.size();
    }
}
