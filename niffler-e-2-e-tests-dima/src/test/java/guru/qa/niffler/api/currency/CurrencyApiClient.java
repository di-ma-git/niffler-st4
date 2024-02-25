package guru.qa.niffler.api.currency;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CurrencyJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public class CurrencyApiClient extends RestClient {

    CurrencyApi currencyApi;

    public CurrencyApiClient(@Nonnull String baseUri) {
        super(Config.getInstance().frontUrl());
        this.currencyApi = retrofit.create(CurrencyApi.class);
    }

    @Step("Get all currencies")
    public List<CurrencyJson> getAllCurrencies() throws IOException {
        return currencyApi.getAllCurrencies().execute().body();
    }
}
