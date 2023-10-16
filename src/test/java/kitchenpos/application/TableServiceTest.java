package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.Fixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableServiceTest extends ServiceBaseTest {

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = Fixture.orderTable(null, 3, false);
    }

    @Test
    @DisplayName("비어있는 주문 테이블을 생성할 수 있다.")
    void create() {
        //given
        final OrderTable orderTable = Fixture.orderTable(null, 3, true);

        //when
        final OrderTable createdOrderTable = tableService.create(orderTable);

        //then
        assertAll(
                () -> assertThat(createdOrderTable).isNotNull(),
                () -> assertThat(createdOrderTable.isEmpty()).isTrue(),
                () -> assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests())
        );
    }

    @Test
    @DisplayName("주문 테이블을 조회할 수 있다.")
    void list() {
        //given
        tableService.create(Fixture.orderTable(null, 3, false));
        tableService.create(Fixture.orderTable(null, 3, false));

        //when
        final List<OrderTable> orderTables = tableService.list();

        //then
        assertThat(orderTables).hasSize(2);
    }

    @Test
    @DisplayName("주문 테이블을 빈 상태로 변경할 수 있다.")
    void changeEmpty() {
        //given
        final OrderTable orderTable = tableService.create(Fixture.orderTable(null, 3, false));
        orderTable.setEmpty(true);

        //when
        final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        //then
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블은 존재해야한다.")
    void changeEmptyValidOrderTable() {
        //given
        final OrderTable orderTable = Fixture.orderTable(null, 3, false);
        orderTable.setEmpty(true);

        //when&then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈상태로 변경시 orderTable의 테이블 그룹 아이디가 null이 아니면 안된다.")
    void changeEmptyValidTableGroupId() {
        //given
        final OrderTable orderTable1 = Fixture.orderTable(null, 10, true);
        final OrderTable orderTable2 = Fixture.orderTable(null, 5, true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
        final TableGroup tableGroup = Fixture.orderTableGroup(LocalDateTime.now(), List.of(savedOrderTable1, savedOrderTable2));
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        final OrderTable savedOrderTable = orderTableDao.save(Fixture.orderTable(savedTableGroup.getId(), 10, true));

        //when&then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "주문 테이블이 요리, 식사 상태이면 안된다.")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void changeEmptyValidStatus(final OrderStatus orderStatus) {
        //given
        final OrderTable orderTable = tableService.create(Fixture.orderTable(null, 10, true));
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        final Order order = Fixture.order(null, savedOrderTable.getId(), LocalDateTime.now(), null);
        order.setOrderStatus(orderStatus.name());
        orderDao.save(order);

        //when&then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        //given
        orderTable.setNumberOfGuests(10);
        final OrderTable savedOrderTable = tableService.create(orderTable);

        final OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @Test
    @DisplayName("주문 테이블의 손님 수는 0 이상이어야 한다.")
    void changeNumberOfGuestsOverZero() {
        //given
        orderTable.setNumberOfGuests(-1);
        tableService.create(orderTable);

        //when&then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블은 존재해야 한다.")
    void changeNumberOfGuestValidOrderTable() {
        //given
        orderTable.setNumberOfGuests(0);
        tableService.create(orderTable);

        //when&then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게스트 숫자 변경 시 OrderTable은 비어있으면 안된다.")
    void changeNumberOfGuestValidEmptySavedOrderTable() {
        //given
        final OrderTable orderTable = tableService.create(Fixture.orderTable(null, 10, true));
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        this.orderTable.setNumberOfGuests(0);

        //when&then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
