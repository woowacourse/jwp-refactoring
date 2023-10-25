package kitchenpos.supports;

import kitchenpos.application.dto.request.TableRequest;

public class OrderTableFixture {

    public static TableRequest createNotEmpty() {
        return new TableRequest(4, false);
    }

    public static TableRequest createEmpty() {
        return new TableRequest( true);
    }
}
