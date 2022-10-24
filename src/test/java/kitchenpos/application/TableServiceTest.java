package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void create() {
        // given
        OrderTable orderTable = new OrderTable();

        // when
        OrderTable newOrderTable = tableService.create(orderTable);

        // then
        assertAll(
                () -> assertThat(newOrderTable.getId()).isNotNull(),
                () -> assertThat(newOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(newOrderTable.getNumberOfGuests()).isEqualTo(0),
                () -> assertThat(newOrderTable.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다.")
    void list() {
        // when
        List<OrderTable> products = tableService.list();

        // then
        assertThat(products.size()).isEqualTo(8);
    }

    @Test
    @DisplayName("주문 테이블 상태를 변경한다.")
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        // when
        OrderTable changeOrderTable = tableService.changeEmpty(savedOrderTable.getId(), orderTable);

        // then
        assertThat(changeOrderTable.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("주문 테이블 상태를 변경 시 주문 상태가 cooking이나 meal이면 예외를 반환한다.")
    void changeEmpty_WhenOrderStatusCooking() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Order order= new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderLineItems(new ArrayList<>());
        orderDao.save(order);
        // when
        assertThatThrownBy(()->tableService.changeEmpty(savedOrderTable.getId(), orderTable)).isInstanceOf(IllegalArgumentException.class);
    }
}
