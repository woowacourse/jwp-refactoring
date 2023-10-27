package kitchenpos.support.fixture.dto;

import kitchenpos.ordertable.application.dto.OrderTableChangeEmptyRequest;

public abstract class OrderTableChangeEmptyRequestFixture {

    public static OrderTableChangeEmptyRequest orderTableChangeEmptyRequest(final boolean empty) {
        return new OrderTableChangeEmptyRequest(empty);
    }
}
