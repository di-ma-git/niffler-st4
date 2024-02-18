package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.spend.DatabaseSpendExtension;
import guru.qa.niffler.jupiter.extension.spend.Hw9SpendExtension;
import guru.qa.niffler.jupiter.extension.spend.RestSpendExtension;
import guru.qa.niffler.jupiter.extension.spend.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({DatabaseSpendExtension.class})
//@ExtendWith({RestSpendExtension.class})
public @interface Hw9GenerateSpend {

    String username();

    String description();

    double amount();

    CurrencyValues currency();

    String category();
}
