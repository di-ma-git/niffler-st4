package guru.qa.niffler.api.spend;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

public interface SpendApi {

    @GET("/spends")
    Call<List<SpendJson>> getSpends(
            @Query("username") String username,
            @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
            @Query("from") @Nullable Date from,
            @Query("to") @Nullable Date to
    );

    @POST("/addSpend")
    Call<SpendJson> addSpend(@Body SpendJson spend);

    @GET("/statistic")
    Call<List<StatisticJson>> getStatistic(
            @Query("username") String username,
            @Query("userCurrency") CurrencyValues userCurrency,
            @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
            @Query("from") @Nullable Date from,
            @Query("to") @Nullable Date to
    );

    @PATCH("/editSpend")
    Call<SpendJson> editSpend(@Body SpendJson spend);

    @DELETE("/deleteSpends")
    Call<Void> deleteSpends(
            @Query("username") String username,
            @Query("ids") List<String> ids
    );


}
