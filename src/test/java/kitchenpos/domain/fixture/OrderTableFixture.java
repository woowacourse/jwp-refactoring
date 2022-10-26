package kitchenpos.domain.fixture;

import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableFixture() {
    }

    public static OrderTable 새로운_테이블() {
        return 주문_테이블().build();
    }

    public static OrderTable 새로운_테이블(final Long tableGroupId) {
        return 주문_테이블()
            .테이블_그룹_아이디(tableGroupId)
            .build();
    }

    public static OrderTable 비어있는_테이블() {
        return 주문_테이블()
            .빈_테이블(true)
            .build();
    }

    public static OrderTable 비어있지_않는_테이블() {
        return 주문_테이블()
            .빈_테이블(false)
            .build();
    }

    public static OrderTable 테이블의_손님의_수는(final int numberOfGuests) {
        return 주문_테이블()
            .손님의_수(numberOfGuests)
            .build();
    }

    private static OrderTableFixture 주문_테이블() {
        return new OrderTableFixture();
    }

    private OrderTableFixture 테이블_그룹_아이디(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        return this;
    }

    private OrderTableFixture 손님의_수(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    private OrderTableFixture 빈_테이블(final boolean empty) {
        this.empty = empty;
        return this;
    }

    private OrderTable build() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
