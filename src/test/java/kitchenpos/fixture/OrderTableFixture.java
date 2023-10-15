package kitchenpos.fixture;

import kitchenpos.application.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.table.dto.OrderTableChangeNumberOfGuestRequest;
import kitchenpos.application.table.dto.OrderTableCreateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 주문_테이블_생성(final TableGroup tableGroup,
                                       final int numberOfGuests,
                                       final boolean isEmpty) {
        return new OrderTable(null, tableGroup, numberOfGuests, isEmpty);
    }

    public static OrderTableCreateRequest 주문_테이블_생성_요청(final OrderTable orderTable) {
        return new OrderTableCreateRequest(
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public static OrderTableChangeEmptyRequest 주문_테이블_상태_업데이트_요청(final OrderTable orderTable) {
        return new OrderTableChangeEmptyRequest(orderTable.isEmpty());
    }

    public static OrderTableChangeNumberOfGuestRequest 주문_테이블_손님_수_업데이트_요청(final OrderTable orderTable) {
        return new OrderTableChangeNumberOfGuestRequest(orderTable.getNumberOfGuests());
    }
}
