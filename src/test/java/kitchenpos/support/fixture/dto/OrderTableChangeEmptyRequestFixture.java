package kitchenpos.support.fixture.dto;

import kitchenpos.ui.dto.OrderTableChangeEmptyRequest;

public class OrderTableChangeEmptyRequestFixture {

    public static OrderTableChangeEmptyRequest orderTableChangeEmptyRequest(final boolean empty) {
        return new OrderTableChangeEmptyRequest(empty);
    }
}
