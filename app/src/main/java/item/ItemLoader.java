package item;

import android.content.Context;
import android.util.Log;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import networkManager.ApiNetworkInterface;
import networkManager.NetworkManagerRequests;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Lenovo-pc on 03/03/2017.
 */
public class ItemLoader {

    private GridView allItemsGrid;
    private Context context;
    private List<ItemModel> allItemsList = new ArrayList<>();

    public ItemLoader(Context context, GridView allItemsGrid) {
        this.context = context;
        this.allItemsGrid = allItemsGrid;
    }

    //this method is used for loading home items and loading items currently in the stock without search tags
    public void loadHomeItems(int next, int stock, boolean clearFlag) {
        ApiNetworkInterface networkInterface = new NetworkManagerRequests(context).getApiNetworkInterface();
        if (clearFlag) {
            allItemsList.clear();
        }

        networkInterface.getItem(next, stock).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                addResponseToList(value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                ItemCustomAdaptor adapter = new ItemCustomAdaptor(context, allItemsList);
                allItemsGrid.setAdapter(adapter);
            }
        });
    }

    //this method is used for loading items and loading items currently in the stock with search by tags
    public void loadItemsSearch(int next, int stock, String tags, boolean clearFlag) {
        ApiNetworkInterface networkInterface = new NetworkManagerRequests(context).getApiNetworkInterface();
        if (clearFlag) {
            allItemsList.clear();
        }
        networkInterface.getItem(next, tags, stock).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                allItemsList.clear();
                addResponseToList(value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                ItemCustomAdaptor adapter = new ItemCustomAdaptor(context, allItemsList);
                allItemsGrid.setAdapter(adapter);
            }
        });

    }

    public void addResponseToList(String response) {
        JSONArray responseArr;

        try {
            responseArr = new JSONArray(response);
            for (int i = 0; i < responseArr.length(); i++) {
                ItemModel model = new ItemModel();
                model.setId(responseArr.getJSONObject(i).getString("id"));
                model.setFace(responseArr.getJSONObject(i).getString("face"));
                model.setPrice(responseArr.getJSONObject(i).getInt("price"));
                model.setSize(responseArr.getJSONObject(i).getInt("size"));
                model.setStock(responseArr.getJSONObject(i).getInt("stock"));
                model.setType(responseArr.getJSONObject(i).getString("type"));
                allItemsList.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
