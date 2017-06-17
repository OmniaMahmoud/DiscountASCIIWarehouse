package networkManager;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Lenovo-pc on 27/02/2017.
 */

public interface ApiNetworkInterface {

    @GET("/api/search?limit=4")
    io.reactivex.Observable<String> getItem(@Query("skip") int skip, @Query("onlyInStock") int onlyInStock);

    @GET("/api/search?limit=4")
    io.reactivex.Observable<String> getItem(@Query("skip") int skip, @Query("q") String q, @Query("onlyInStock") int onlyInStock);
}
