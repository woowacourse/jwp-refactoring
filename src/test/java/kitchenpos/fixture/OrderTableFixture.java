package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableChangeEmptyDto;
import kitchenpos.dto.TableChangeNumberOfGuestsDto;
import kitchenpos.dto.TableCreateDto;

public class OrderTableFixture {

    public static TableCreateDto 주문테이블_생성_요청(final int numberOfGuests, final boolean empty) {
        return new TableCreateDto(numberOfGuests, empty);
    }

    public static TableChangeEmptyDto 주문테이블_EMPTY_변경_요청(final boolean empty) {
        return new TableChangeEmptyDto(empty);
    }

    public static TableChangeNumberOfGuestsDto 주문테이블_손님수_변경_요청(final int numberOfGuests) {
        return new TableChangeNumberOfGuestsDto(numberOfGuests);
    }

    public static OrderTable 주문테이블() {
        return new OrderTable();
    }

    public static OrderTable 빈테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.changeEmpty(true);
        return orderTable;
    }

    public static OrderTable 비지않은_테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.changeEmpty(false);
        return orderTable;
    }

    public static OrderTable 주문테이블(final int numbersOfGuest) {
        OrderTable orderTable = new OrderTable();
        orderTable.changeNumberOfGuests(numbersOfGuest);
        return orderTable;
    }

    public static OrderTable 주문테이블(final int numberOfGuests, final boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }
}
