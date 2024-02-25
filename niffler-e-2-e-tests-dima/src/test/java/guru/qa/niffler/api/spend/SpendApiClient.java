package guru.qa.niffler.api.spend;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import io.qameta.allure.Step;
import retrofit2.http.Query;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SpendApiClient extends RestClient {

    private final SpendApi spendApi;

    public SpendApiClient(@Nonnull String baseUri) {
        super(Config.getInstance().frontUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }


    @Step("Get all {username}'s spends")
    public List<SpendJson> getSpends(
            String username,
            @Nullable CurrencyValues filterCurrency,
            @Nullable Date from,
            @Nullable Date to
    ) throws IOException {
        return spendApi.getSpends(username, filterCurrency, from, to).execute().body();
    }

    @Step("Add new spend {spend}")
    public SpendJson addSpend(SpendJson spend) throws IOException {
        return spendApi.addSpend(spend).execute().body();
    }

    @Step("Get statistic {username}")
    public List<StatisticJson> getStatistic(
            String username,
            CurrencyValues userCurrency,
            @Nullable CurrencyValues filterCurrency,
            @Nullable Date from,
            @Nullable Date to
    ) throws IOException {
        return spendApi.getStatistic(username, userCurrency, filterCurrency, from, to).execute().body();
    }

    @Step("Edit spend {spend}")
    public SpendJson editSpend(SpendJson spend) throws IOException {
        return spendApi.editSpend(spend).execute().body();
    }

    @Step("Delete {ids} spends for {username}")
    void deleteSpends(String username, List<String> ids) {
        spendApi.deleteSpends(username, ids);
    }
}
