package mobile_application_development.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by devikabeniwal on 17/03/17.
 */

public class StockViewHolder extends RecyclerView.ViewHolder {
    public TextView symbol;
    public TextView companyName;
    public TextView lastTradePrice;
    public TextView priceChangeAmt;
    public TextView priceChangePercentage;
    public TextView change;


    public StockViewHolder(View view){
        super(view);
        symbol = (TextView) view.findViewById(R.id.symbol);
        companyName = (TextView) view.findViewById(R.id.company_name);
        lastTradePrice = (TextView) view.findViewById(R.id.last_trade_price);
        priceChangeAmt = (TextView) view.findViewById(R.id.amount);
        priceChangePercentage = (TextView) view.findViewById(R.id.percentage);
        change = (TextView) view.findViewById(R.id.change);
    }



}
