package kitchenpos;

import kitchenpos.testtool.RequestBuilder;
import kitchenpos.testtool.request.RequestApi;

public class DefaultFixture {

    private final RequestBuilder requestBuilder;

    public DefaultFixture(RequestBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    protected RequestApi request() {
        return requestBuilder.builder();
    }
}
