package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFactory.createChangeOrderTableRequest;
import static kitchenpos.fixture.OrderTableFactory.createEmptyTable;
import static kitchenpos.fixture.OrderTableFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.TableGroupFactory;
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
    OrderRepository orderRepository;

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    TableService sut;

    @Test
    @DisplayName("주문 테이블을 생성한다")
    void testCreateOrderTable() {
        OrderTable savedOrderTable = sut.create(1, false);

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
        final long NON_EXIST_ID = 0L;
        OrderTable orderTable = createChangeOrderTableRequest(true);

        // when && then
        assertThatThrownBy(() -> sut.changeEmpty(NON_EXIST_ID, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 존재하지 않습니다");
    }

    @Test
    @DisplayName("단체 테이블에 속한 주문 테이블은 주문 테이블 상태를 변경할 수 없다")
    void cannotChangeEmpty_ForOrderTableInTableGroup() {
        OrderTable savedOrderTable1 = orderTableDao.save(createEmptyTable());
        OrderTable savedOrderTable2 = orderTableDao.save(createEmptyTable());
        TableGroup tableGroup = TableGroupFactory.createTableGroup(List.of(savedOrderTable1, savedOrderTable2));
        tableGroupService.create(tableGroup);

        OrderTable orderTable = createChangeOrderTableRequest(true);

        // when && then
        assertThatThrownBy(() -> sut.changeEmpty(savedOrderTable1.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단체 테이블에 속해있습니다");
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COOKING"})
    @DisplayName("주문 상태가 MEAL 혹은 COOKING이면 주문 테이블 상태를 변경할 수 없다")
    void cannotChangeEmpty_WhenOrderStatus_MEAL_or_COOKING(OrderStatus orderStatus) {
        // given
        Long orderTableId = orderTableDao.save(createOrderTable(1, false)).getId();

//        Order order = new Order();
//        order.setOrderTableId(orderTableId);
//        order.setOrderStatus(orderStatus.name());
//        order.setOrderedTime(LocalDateTime.now());
//        orderDao.save(order);

        OrderTable changeRequest = createChangeOrderTableRequest(true);

        // when && when
        assertThatThrownBy(() -> sut.changeEmpty(orderTableId, changeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("요리 중 혹은 식사 중인 테이블입니다");
    }

    @Test
    @DisplayName("주문 테이블 상태를 변경한다")
    void changeEmpty() {
        // given
        final int NUMBER_OF_GUEST = 0;
        Long orderTableId = orderTableDao.save(createOrderTable(NUMBER_OF_GUEST, true)).getId();
        OrderTable changeRequest = createChangeOrderTableRequest(false);

        // when
        OrderTable savedOrderTable = sut.changeEmpty(orderTableId, changeRequest);

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
        Long orderTableId = orderTableDao.save(createOrderTable(1, false)).getId();

        OrderTable changeRequest = createChangeOrderTableRequest(-1);

        // when && then
        assertThatThrownBy(() -> sut.changeNumberOfGuests(orderTableId, changeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("손님 수는 음수일 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블의 손님 수를 변경할 수 없다")
    void cannotChangeNumberOfGuest_ThatDoesNotExist() {
        final long NON_EXIST_ID = 0L;
        OrderTable changeRequest = createChangeOrderTableRequest(1);

        assertThatThrownBy(() -> sut.changeNumberOfGuests(NON_EXIST_ID, changeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문 테이블입니다");
    }

    @Test
    @DisplayName("빈 상태의 주문 테이블의 손님 수를 변경할 수 없다")
    void cannotChangeNumberOfGuest_WhenOrderTableIsEmpty() {
        // given
        Long orderTableId = orderTableDao.save(createEmptyTable()).getId();

        OrderTable changeRequest = createChangeOrderTableRequest(1);

        // when && then
        assertThatThrownBy(() -> sut.changeNumberOfGuests(orderTableId, changeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블입니다");
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다")
    void changeNumberOfGuest() {
        // given
        final int CHANGED_NUMBER = 2;
        Long orderTableId = orderTableDao.save(createOrderTable(1, false)).getId();
        OrderTable changeRequest = createChangeOrderTableRequest(CHANGED_NUMBER);

        // when
        OrderTable changedOrderTable = sut.changeNumberOfGuests(orderTableId, changeRequest);

        // then
        assertThat(changedOrderTable.getId()).isNotNull();
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(CHANGED_NUMBER);
    }
}
