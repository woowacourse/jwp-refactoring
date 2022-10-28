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
        return 주문_테이블()
            .빈_테이블(true)
            .build();
    }

    public static OrderTable 새로운_테이블(final Long tableGroupId) {
        return 주문_테이블()
            .테이블_그룹_아이디(tableGroupId)
            .빈_테이블(true)
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

    private static OrderTableFixture 주문_테이블() {
        return new OrderTableFixture();
    }

    private OrderTableFixture 테이블_그룹_아이디(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
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
