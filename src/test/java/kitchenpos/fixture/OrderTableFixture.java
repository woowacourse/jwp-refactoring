package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 테이블_그룹이_있는_주문_테이블_생성(
            TableGroup savedTableGroup,
            int numberOfGuests,
            boolean empty
    ) {
        return new OrderTable(savedTableGroup.getId(), numberOfGuests, empty);
    }

    public static OrderTable 테이블_그룹이_없는_주문_테이블_생성(
            int numberOfGuests,
            boolean empty
    ) {
        return new OrderTable(numberOfGuests, empty);
    }

}
