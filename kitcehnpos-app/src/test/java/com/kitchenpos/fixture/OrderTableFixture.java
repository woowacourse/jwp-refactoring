package com.kitchenpos.fixture;

import com.kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import com.kitchenpos.application.dto.OrderTableChangeNumberOfGuestRequest;
import com.kitchenpos.application.dto.OrderTableCreateRequest;
import com.kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 주문_테이블_생성(final int numberOfGuests,
                                       final boolean isEmpty) {
        return new OrderTable(numberOfGuests, isEmpty);
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
