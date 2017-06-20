package mobile_application_development.stockwatch;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by devikabeniwal on 17/03/17.
 */

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder>{

    private List<Stock> stockList;
    private MainActivity mainAct;


    public StockAdapter(List<Stock> list, MainActivity ma){
        this.stockList = list;
        this.mainAct = ma;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_view,parent,false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {
        Stock stock = stockList.get(position);
        if(stock.getChange() > 0){
            holder.lastTradePrice.setText(String.valueOf(stock.getPrice()));
            holder.change.setText("▲ ");
            holder.priceChangeAmt.setText(String.valueOf(stock.getChange()));
            holder.symbol.setText(stock.getSymbol());
            holder.companyName.setText(stock.getCompanyName());
            holder.priceChangePercentage.setText("("+String.valueOf(stock.getPercentage())+ " %)");
            holder.symbol.setTextColor(Color.GREEN);
            holder.companyName.setTextColor(Color.GREEN);
            holder.change.setTextColor(Color.GREEN);
            holder.lastTradePrice.setTextColor(Color.GREEN);
            holder.priceChangeAmt.setTextColor(Color.GREEN);
            holder.priceChangePercentage.setTextColor(Color.GREEN);

        }else{
            holder.lastTradePrice.setText(String.valueOf(stock.getPrice()));
            holder.change.setText("▼ ");
            holder.priceChangeAmt.setText(String.valueOf(stock.getChange()));
            holder.symbol.setText(stock.getSymbol());
            holder.companyName.setText(stock.getCompanyName());
            holder.priceChangePercentage.setText("("+String.valueOf(stock.getPercentage())+ " %)");
            holder.symbol.setTextColor(Color.RED);
            holder.companyName.setTextColor(Color.RED);
            holder.change.setTextColor(Color.RED);
            holder.lastTradePrice.setTextColor(Color.RED);
            holder.priceChangeAmt.setTextColor(Color.RED);
            holder.priceChangePercentage.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
