package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;

public class OrderTableFixture {

    public OrderTableCreateRequest 주문_테이블_생성_요청(int numberOfGuests, boolean empty) {
        return new OrderTableCreateRequest(numberOfGuests, empty);
    }

    public OrderTable 주문_테이블_생성(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return null;
    }

    public List<OrderTable> 주문_테이블_리스트_생성(OrderTable... orderTable) {
        return Arrays.asList(orderTable);
    }
}
