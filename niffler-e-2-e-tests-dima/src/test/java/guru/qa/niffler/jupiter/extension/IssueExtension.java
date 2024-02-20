package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.GhApi;
import guru.qa.niffler.api.GhApiClient;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class IssueExtension implements ExecutionCondition {

    private final GhApiClient ghApiClient = new GhApiClient();

    @SneakyThrows
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
        DisabledByIssue disabledByIssue = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(), DisabledByIssue.class
        ).orElse(
                AnnotationSupport.findAnnotation(
                        extensionContext.getRequiredTestClass(), DisabledByIssue.class
                ).orElse(null)
        );

        if (disabledByIssue != null) {
            return "open".equals(ghApiClient.getIssueState(disabledByIssue.value()))
                    ? ConditionEvaluationResult.disabled("Disabled by issue")
                    : ConditionEvaluationResult.enabled("Issue was closed");
        }

        return ConditionEvaluationResult.enabled("Annotation not found");
    }
}
