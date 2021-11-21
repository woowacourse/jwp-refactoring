package kitchenpos.testtool.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.testtool.executor.MockMvcExecutor;
import kitchenpos.testtool.executor.RestAssuredExecutor;
import kitchenpos.testtool.executor.TestAdapter;
import kitchenpos.testtool.response.HttpResponse;
import kitchenpos.testtool.util.RequestDto;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

@Component
public class TestAdapterContainer {

    private final List<TestAdapter> testAdapters;

    public TestAdapterContainer(
            ObjectMapper objectMapper,
            WebApplicationContext context
    ) {

        this.testAdapters = Arrays.asList(
                new RestAssuredExecutor(objectMapper),
                new MockMvcExecutor(context, objectMapper)
        );
    }

    public HttpResponse execute(RequestDto requestDto) {
        return testAdapters.stream()
                .filter(testAdapter -> testAdapter.isAssignable(requestDto))
                .map(testAdapter -> testAdapter.execute(requestDto))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
