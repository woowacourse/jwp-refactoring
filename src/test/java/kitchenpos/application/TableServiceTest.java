package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블을 생성한다")
    @Test
    void create() {
        final OrderTable orderTable = tableService.create(new OrderTable());

        assertThat(orderTable.getId()).isNotNull();
    }

    @DisplayName("주문 테이블 전체 목록을 조회한다")
    @Test
    void findAll() {
        final List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(8);
    }

    @DisplayName("빈 테이블로 변경할 테이블이 존재하지 않다면, 예외를 발생한다")
    @Test
    void does_not_exist_order_table_exception() {
        assertThatThrownBy(() -> tableService.changeEmpty(0L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 변경할 때 해당 테이블이 단체로 지정되어 있다면, 예외를 발생한다")
    @Test
    void grouped_table_exception() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(new ArrayList<>());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(savedTableGroup.getId());
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 변경할 때 주문 상테가 cooking 또는 meal 이라면, 예외를 발생한다")
    @ValueSource(strings = {"COOKING", "MEAL"})
    @ParameterizedTest
    void order_status_not_completion_exception(final String status) {
        final Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(status);
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다")
    @Test
    void changeEmptyTable() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(2);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        final OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable);

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("변경할 손님 수가 0보자 작은 경우, 예외를 발생한다")
    @Test
    void negative_number_of_guests_exception() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final OrderTable changeOrderTable = new OrderTable();
        changeOrderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경할 때 빈 테이블인 경우, 예외를 발생한다")
    @Test
    void empty_order_table_exception() {
        final OrderTable changeOrderTable = new OrderTable();
        changeOrderTable.setNumberOfGuests(10);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 손님 수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        final OrderTable changeOrderTable = new OrderTable();
        changeOrderTable.setNumberOfGuests(20);

        final OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(),
                changeOrderTable);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(20);
    }
}
