package item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mobile.ucis.discountasciiwarehouse.R;

/**
 * Created by Lenovo-pc on 03/03/2017.
 */
public class ItemCustomAdaptor extends ArrayAdapter<ItemModel> {

    Context c;
    List<ItemModel> displayedItems;

    ItemCustomAdaptor(Context context, List<ItemModel> displayedItems) {
        super(context, R.layout.adaptor_custom_view, displayedItems);
        this.c = context;
        this.displayedItems = displayedItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adaptor_custom_view, parent, false);
        }
        TextView face = (TextView) convertView.findViewById(R.id.face);
        face.setText(displayedItems.get(position).getFace());
        TextView price = (TextView) convertView.findViewById(R.id.price);
        double fprice = displayedItems.get(position).getPrice() / 100.00;
        price.setText(fprice + "$");
        TextView stock = (TextView) convertView.findViewById(R.id.stock);
        stock.setText("(Only " + displayedItems.get(position).getStock() + " more in stock!)");
        return convertView;
    }
}