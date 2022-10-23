package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    TableService sut;

    @Test
    @DisplayName("주문 테이블을 생성한다")
    void createOrderTable() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(1);

        // when
        OrderTable savedOrderTable = sut.create(orderTable);

        // then
        assertThat(savedOrderTable).isNotNull();
        assertThat(savedOrderTable.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다")
    void listOrderTables() {
        List<OrderTable> expected = orderTableDao.findAll();

        List<OrderTable> actual = sut.list();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("입력받은 id에 해당하는 주문 테이블이 존재하지 않는 경우, 주문 테이블 상태를 변경할 수 없다")
    void throwException_InNonExistOrderTable() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        // when && then
        assertThatThrownBy(() -> sut.changeEmpty(0L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블에 속한 주문 테이블은 주문 테이블 상태를 변경할 수 없다")
    void cannotChangeEmpty_ForOrderTableInTableGroup() {
        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(0);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        orderTable2.setNumberOfGuests(0);

        OrderTable savedOrderTable1 = sut.create(orderTable1);
        OrderTable savedOrderTable2 = sut.create(orderTable2);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));
        tableGroupService.create(tableGroup);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        // when && then
        assertThatThrownBy(() -> sut.changeEmpty(savedOrderTable1.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COOKING"})
    @DisplayName("주문 상태가 MEAL 혹은 COOKING이면 주문 테이블 상태를 변경할 수 없다")
    void cannotChangeEmpty_WhenOrderStatus_MEAL_or_COOKING(OrderStatus orderStatus) {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
        Long orderTableId = sut.create(orderTable).getId();

        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(true);

        // when && when
        assertThatThrownBy(() -> sut.changeEmpty(orderTableId, newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 상태를 변경한다")
    void changeEmpty() {
        // given
        final int NUMBER_OF_GUEST = 0;
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(NUMBER_OF_GUEST);
        Long orderTableId = sut.create(orderTable).getId();

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(false);

        // when
        OrderTable savedOrderTable = sut.changeEmpty(orderTableId, newOrderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getTableGroupId()).isNull();
        assertThat(savedOrderTable.isEmpty()).isFalse();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(NUMBER_OF_GUEST);
    }

    @Test
    @DisplayName("입력된 손님 수가 음수이면 손님 수를 변경할 수 없다")
    void cannotChangeNumberOfGuest_WhenItIsNegative() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
        Long orderTableId = sut.create(orderTable).getId();

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(-1);

        // when && then
        assertThatThrownBy(() -> sut.changeNumberOfGuests(orderTableId, newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블의 손님 수를 변경할 수 없다")
    void cannotChangeNumberOfGuest_ThatDoesNotExist() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);

        assertThatThrownBy(() -> sut.changeNumberOfGuests(0L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 상태의 주문 테이블의 손님 수를 변경할 수 없다")
    void cannotChangeNumberOfGuest_WhenOrderTableIsEmpty() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
        Long orderTableId = sut.create(orderTable).getId();

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(1);

        // when && then
        assertThatThrownBy(() -> sut.changeNumberOfGuests(orderTableId, newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다")
    void changeNumberOfGuest() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(1);
        Long orderTableId = sut.create(orderTable).getId();

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(2);

        // when
        OrderTable changedOrderTable = sut.changeNumberOfGuests(orderTableId, newOrderTable);

        // then
        assertThat(changedOrderTable.getId()).isNotNull();
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(2);
    }
}
