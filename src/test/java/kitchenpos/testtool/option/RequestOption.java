package kitchenpos.testtool.option;

import kitchenpos.testtool.request.TestAdapterContainer;
import kitchenpos.testtool.response.HttpResponse;
import kitchenpos.testtool.util.RequestDto;
import kitchenpos.testtool.util.TestTool;
import org.springframework.http.HttpMethod;

public class RequestOption {

    private final RequestDto requestDto;
    private final TestAdapterContainer testAdapterContainer;

    public RequestOption(
            HttpMethod httpMethod,
            String url,
            TestTool testTool,
            TestAdapterContainer testAdapterContainer,
            Object data,
            Object... pathVariables
    ) {
        this.requestDto = new RequestDto(httpMethod, url, testTool, data, pathVariables);
        this.testAdapterContainer = testAdapterContainer;
    }

    public HttpResponse build() {
        return testAdapterContainer.execute(requestDto);
    }
}
