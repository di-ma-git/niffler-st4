package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.SpendJsonConverter;
import guru.qa.niffler.jupiter.annotation.AllureIdParam;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ParametrizedTestExample {

    @CsvSource({
            "1001", "Dima",
            "1002", "Stas",
            "1003", "Artem",
    })
    @ParameterizedTest
    void paramTest(@AllureIdParam String allureId, String name) {
    }

    @ValueSource(strings = {
            "spend0.json", "spend1.json"
    })
    @ParameterizedTest
    void spendRestTest(@ConvertWith(SpendJsonConverter.class) SpendJson spend) {
        System.out.println("");
    }

}
