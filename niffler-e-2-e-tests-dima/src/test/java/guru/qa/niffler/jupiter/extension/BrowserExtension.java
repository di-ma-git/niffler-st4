package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

public class BrowserExtension implements AfterEachCallback, TestExecutionExceptionHandler {

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        if (isWebTest()) {
            Selenide.closeWebDriver();
        }

    }

    @Override
    public void handleTestExecutionException(ExtensionContext extensionContext, Throwable throwable) throws Throwable {
        if (isWebTest()) {
            Allure.addAttachment(
                    "Screen after test",
                    new ByteArrayInputStream(
                            ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES)
                    )
            );
            throw throwable;
        }
    }

    private boolean isWebTest() {
        return WebDriverRunner.hasWebDriverStarted();
    }

}
