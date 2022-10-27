package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.generateOrder;
import static kitchenpos.fixture.OrderTableFixture.generateOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.FakeOrderDao;
import kitchenpos.dao.FakeOrderTableDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableServiceTest {

    private TableService tableService;

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;

    @BeforeEach
    void beforeEach() {
        this.orderDao = new FakeOrderDao();
        this.orderTableDao = new FakeOrderTableDao();
        this.tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void create() {
        // given
        OrderTable orderTable = generateOrderTable(null, 0, true);

        // when
        OrderTable newOrderTable = tableService.create(orderTable);

        // then
        assertAll(
                () -> assertThat(newOrderTable.getId()).isNotNull(),
                () -> assertThat(newOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(newOrderTable.getNumberOfGuests()).isEqualTo(0),
                () -> assertThat(newOrderTable.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다.")
    void list() {
        // given
        OrderTable orderTable1 = generateOrderTable(null, 0, true);
        tableService.create(orderTable1);
        OrderTable orderTable2 = generateOrderTable(null, 0, true);
        tableService.create(orderTable2);

        // when
        List<OrderTable> products = tableService.list();

        // then
        assertThat(products.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("주문 테이블 상태를 변경한다.")
    void changeEmpty() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, true);
        OrderTable nonEmptyOrderTable = generateOrderTable(null, 0, false);
        OrderTable savedOrderTable = orderTableDao.save(emptyOrderTable);

        // when
        OrderTable changeOrderTable = tableService.changeEmpty(savedOrderTable.getId(), nonEmptyOrderTable);

        // then
        assertThat(changeOrderTable.isEmpty()).isEqualTo(false);
    }

    @Test
    @DisplayName("주문 테이블 상태를 변경 시 주문 상태가 cooking이나 meal이면 예외를 반환한다.")
    void changeEmpty_WhenOrderStatusCooking() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, true);
        OrderTable nonEmptyOrderTable = generateOrderTable(null, 0, false);

        OrderTable savedOrderTable = orderTableDao.save(emptyOrderTable);

        Order order = generateOrder(LocalDateTime.now(), savedOrderTable.getId(), OrderStatus.COOKING.name(), new ArrayList<>());
        orderDao.save(order);
        // when
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), nonEmptyOrderTable)).isInstanceOf(
                IllegalArgumentException.class);
    }
}
