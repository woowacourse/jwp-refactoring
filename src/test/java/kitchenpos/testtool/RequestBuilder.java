package kitchenpos.testtool;

import kitchenpos.testtool.request.RequestApi;
import kitchenpos.testtool.request.TestAdapterContainer;
import kitchenpos.testtool.util.TestTool;

public class RequestBuilder {

    private final TestAdapterContainer testAdapterContainer;
    private final TestTool testTool;

    public RequestBuilder(TestAdapterContainer testAdapterContainer, TestTool testTool) {
        this.testAdapterContainer = testAdapterContainer;
        this.testTool = testTool;
    }

    public RequestApi builder() {
        return new RequestApi(testTool, testAdapterContainer);
    }
}
