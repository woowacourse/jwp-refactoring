package kitchenpos.support.fixture;

import static java.time.LocalDateTime.now;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.presentation.dto.request.OrderTableRequest;

public abstract class OrderTableFixture {

    public static final OrderTable 정상_주문테이블 = new OrderTable(0, true, null);

    public static final OrderTableRequest 주문테이블_요청 = new OrderTableRequest(0, true);
}
