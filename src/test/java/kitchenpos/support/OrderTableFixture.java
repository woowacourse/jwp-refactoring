package kitchenpos.support;

import static java.time.LocalDateTime.now;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;

public abstract class OrderTableFixture {

    public static final OrderTable 정상_주문테이블 = new OrderTable(0, true, null);

    public static final OrderTableRequest 주문테이블_요청 = new OrderTableRequest(0, true);
}
