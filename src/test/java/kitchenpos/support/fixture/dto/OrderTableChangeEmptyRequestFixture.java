package kitchenpos.support.fixture.dto;

import kitchenpos.application.dto.ordertable.OrderTableChangeEmptyRequest;

public abstract class OrderTableChangeEmptyRequestFixture {

    public static OrderTableChangeEmptyRequest orderTableChangeEmptyRequest(final boolean empty) {
        return new OrderTableChangeEmptyRequest(empty);
    }
}
