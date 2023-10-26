package kitchenpos.fixture;

import java.util.List;
import kitchenpos.application.dto.request.TableCreateRequest;
import kitchenpos.application.dto.request.TableUpdateEmptyRequest;
import kitchenpos.application.dto.request.TableUpdateNumberOfGuestsRequest;
import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 주문테이블_N명(final int numberOfGuests) {
        return new OrderTable(numberOfGuests, false);
    }

    public static OrderTable 빈테이블_1명() {
        return new OrderTable(1, true);
    }

    public static OrderTable 빈테이블_1명_단체지정() {
        return new OrderTable(TableGroupFixture.단체지정_여러_테이블(List.of(빈테이블_1명(), 빈테이블_1명())).getId(), 1, true);
    }

    public static OrderTable 주문테이블_INVALID() {
        return new OrderTable(999999L, null, 2, false);
    }

    public static TableCreateRequest 테이블요청_생성(final OrderTable orderTable) {
        return new TableCreateRequest(orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static TableUpdateNumberOfGuestsRequest 테이블요청_손님수_수정_생성(final int numberOfGuests) {
        return new TableUpdateNumberOfGuestsRequest(numberOfGuests);
    }

    public static TableUpdateEmptyRequest 테이블요청_empty_수정_생성(final boolean empty) {
        return new TableUpdateEmptyRequest(empty);
    }
}
