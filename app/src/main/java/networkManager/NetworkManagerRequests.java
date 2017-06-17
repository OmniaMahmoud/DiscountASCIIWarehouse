package networkManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;


/**
 * Created by Lenovo-pc on 27/02/2017.
 */

public class NetworkManagerRequests {

    private static final String baseUrl = "http://74.50.59.155:5000/";
    private static Retrofit retrofit = null;
    private static Context context;

    public NetworkManagerRequests(Context context) {
        this.context = context;
    }

    public ApiNetworkInterface getApiNetworkInterface() {

        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache).addNetworkInterceptor(
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request originalRequest = chain.request();
                        String cacheHeaderValue;
                        if (checkConnection(context)) {
                            int maxAge = 60 * 60 ;
                            cacheHeaderValue = "public, max-age=" + maxAge;
                        } else {
                            int maxStale = 60 * 60;
                            cacheHeaderValue = "public, only-if-cached, max-stale=" + maxStale;
                        }
                        Request request = originalRequest.newBuilder().build();
                        Response response = chain.proceed(request);
                        return response.newBuilder()
                                .removeHeader("Pragma")
                                .removeHeader("Cache-Control")
                                .header("Cache-Control", cacheHeaderValue)
                                .build();
                    }
                }
        ).build();

        if (retrofit == null) {

            retrofit = new Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(new CustomConverter()).build();
        }

        return retrofit.create(ApiNetworkInterface.class);
    }

    public boolean checkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}

