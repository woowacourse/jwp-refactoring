package kitchenpos.fixture;


import application.dto.OrderTableChangeEmptyRequest;
import application.dto.OrderTableChangeNumberOfGuestsRequest;
import application.dto.OrderTableCreateRequest;
import application.dto.OrderTableResponse;
import domain.GuestStatus;
import domain.OrderTable;

public class OrderTableFixture {

    public static GuestStatus 손님_정보(final int numberOfGuest, final boolean isEmpty) {
        return new GuestStatus(numberOfGuest, isEmpty);
    }

    public static OrderTable 주문_테이블(final GuestStatus guestStatus) {
        return new OrderTable(guestStatus);
    }

    public static OrderTableCreateRequest 주문_테이블_생성_요청(final int numberOfGuests, final boolean empty) {
        return new OrderTableCreateRequest(numberOfGuests, empty);
    }

    public static OrderTableResponse 주문_테이블_응답(final OrderTable orderTable) {
        return OrderTableResponse.from(orderTable);
    }

    public static OrderTableChangeEmptyRequest 주문_테이블_빈자리_변경_요청(final boolean isEmpty) {
        return new OrderTableChangeEmptyRequest(isEmpty);
    }

    public static OrderTableChangeNumberOfGuestsRequest 주문_태이블_손님_수_변경_요청(final int size) {
        return new OrderTableChangeNumberOfGuestsRequest(size);
    }
}
