package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.factory.OrderTableFactory;
import kitchenpos.factory.TableGroupFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    private OrderTables orderTables;

    @BeforeEach
    void setUp() {
        OrderTable orderTable1 = OrderTableFactory.builder()
            .id(1L)
            .numberOfGuests(0)
            .empty(true)
            .build();

        OrderTable orderTable2 = OrderTableFactory.builder()
            .id(2L)
            .numberOfGuests(0)
            .empty(true)
            .build();

        orderTables = new OrderTables(orderTable1, orderTable2);
    }

    @DisplayName("OrderTables 안의 객체들을 TableGroup 과 연결한다")
    @Test
    void connect() {
        // given
        TableGroup tableGroup = TableGroupFactory.builder()
            .id(1L)
            .orderTables(
                OrderTableFactory.builder().id(1L).build(),
                OrderTableFactory.builder().id(2L).build()
            )
            .build();

        // when
        orderTables.connect(tableGroup);

        // then
        assertThat(orderTables.toList()).extracting("tableGroupId")
            .allMatch(extractedId -> extractedId.equals(tableGroup.getId()));
        assertThat(tableGroup.getOrderTables()).isEqualTo(orderTables);
    }

    @DisplayName("OrderTables 안의 객체들을 TableGroup 과 연결 실패한다 - null 값을 가지고 있는 경우")
    @Test
    void connectFail_whenOrderTablesHasNullData() {
        // given
        orderTables = new OrderTables();
        TableGroup tableGroup = TableGroupFactory.builder()
            .id(1L)
            .orderTables(
                OrderTableFactory.builder().id(1L).build(),
                OrderTableFactory.builder().id(2L).build()
            )
            .build();

        // when
        ThrowingCallable throwingCallable = () -> orderTables.connect(tableGroup);

        // then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTables 안의 객체들을 TableGroup 과 연결 실패한다 - "
        + "tableGroup 이 가진 OrderTable 의 개수와 일치하지 않는 경우")
    @Test
    void connectFail_whenTableGroupHasDifferentSizeOfOrderTables() {
        // given
        TableGroup tableGroup = TableGroupFactory.builder()
            .id(1L)
            .orderTables(
                OrderTableFactory.builder().id(1L).build()
            )
            .build();

        // when
        ThrowingCallable throwingCallable = () -> orderTables.connect(tableGroup);

        // then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTables 안의 객체들을 TableGroup 과 연결 실패한다 - "
        + "가지고 있는 OrderTable 이 empty 인 상황이 아닌 경우")
    @Test
    void connectFail_whenOrderTableIsNotEmpty() {
        // given
        orderTables = new OrderTables(
            OrderTableFactory.builder().id(1L).empty(false).build(),
            OrderTableFactory.builder().id(2L).empty(false).build()
        );
        TableGroup tableGroup = TableGroupFactory.builder()
            .id(1L)
            .orderTables(
                OrderTableFactory.builder().id(1L).build(),
                OrderTableFactory.builder().id(2L).build()
            )
            .build();

        // when
        ThrowingCallable throwingCallable = () -> orderTables.connect(tableGroup);

        // then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTables 안의 객체들을 TableGroup 과 연결 실패한다 - "
        + "가지고 있는 OrderTable 의 tableGroupId 가 null 이 아닌 경우")
    @Test
    void connectFail_whenOrderTablesTableGroupIsNotNull() {
        // given
        orderTables = new OrderTables(
            OrderTableFactory.builder().id(1L).tableGroupId(100L).build(),
            OrderTableFactory.builder().id(2L).tableGroupId(100L).build()
        );
        TableGroup tableGroup = TableGroupFactory.builder()
            .id(1L)
            .orderTables(
                OrderTableFactory.builder().id(1L).build(),
                OrderTableFactory.builder().id(2L).build()
            )
            .build();

        // when
        ThrowingCallable throwingCallable = () -> orderTables.connect(tableGroup);

        // then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
