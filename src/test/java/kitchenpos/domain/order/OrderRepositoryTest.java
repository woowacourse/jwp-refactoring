package kitchenpos.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableJpaAuditing
@Sql("/truncate.sql")
@DataJpaTest
public class OrderRepositoryTest {
    private static final Integer 테이블_사람_1명 = 1;
    private static final Integer 테이블_사람_2명 = 2;
    private static final Boolean 테이블_비어있음 = true;

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("Order를 DB에 저장할 경우, 올바르게 수행된다.")
    @Test
    void saveTest() {
        OrderTable orderTable = new OrderTable(테이블_사람_1명, 테이블_비어있음);
        Order order = new Order(orderTable, OrderStatus.COOKING);

        orderTableRepository.save(orderTable);
        Order savedOrder = orderRepository.save(order);
        Long size = orderTableRepository.count();

        assertThat(size).isEqualTo(1L);
        assertThat(savedOrder.getId()).isEqualTo(1L);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(savedOrder.getOrderTable().getNumberOfGuests()).isEqualTo(테이블_사람_1명);
        assertThat(savedOrder.getOrderTable().getEmpty()).isEqualTo(테이블_비어있음);
    }

    @DisplayName("Order를 DB에서 조회할 경우, 올바르게 수행된다.")
    @Test
    void findAllTest() {
        OrderTable orderTable1 = new OrderTable(테이블_사람_1명, 테이블_비어있음);
        OrderTable orderTable2 = new OrderTable(테이블_사람_2명, 테이블_비어있음);
        Order order1 = new Order(orderTable1, OrderStatus.COOKING);
        Order order2 = new Order(orderTable2, OrderStatus.COOKING);

        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);
        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getId()).isEqualTo(1L);
        assertThat(orders.get(0).getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(orders.get(0).getOrderTable().getNumberOfGuests()).isEqualTo(테이블_사람_1명);
        assertThat(orders.get(0).getOrderTable().getEmpty()).isEqualTo(테이블_비어있음);
        assertThat(orders.get(1).getId()).isEqualTo(2L);
        assertThat(orders.get(1).getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(orders.get(1).getOrderTable().getNumberOfGuests()).isEqualTo(테이블_사람_2명);
        assertThat(orders.get(1).getOrderTable().getEmpty()).isEqualTo(테이블_비어있음);
    }

    @DisplayName("Order를 DB에서 개별 조회할 경우, 올바르게 수행된다.")
    @Test
    void findByIdTest() {
        OrderTable orderTable1 = new OrderTable(테이블_사람_1명, 테이블_비어있음);
        OrderTable orderTable2 = new OrderTable(테이블_사람_2명, 테이블_비어있음);
        Order order1 = new Order(orderTable1, OrderStatus.COOKING);
        Order order2 = new Order(orderTable2, OrderStatus.COOKING);

        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);
        orderRepository.save(order1);
        orderRepository.save(order2);

        Order foundOrder = orderRepository.findById(2L)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(foundOrder.getId()).isEqualTo(2L);
        assertThat(foundOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(foundOrder.getOrderTable().getNumberOfGuests()).isEqualTo(테이블_사람_2명);
        assertThat(foundOrder.getOrderTable().getEmpty()).isEqualTo(테이블_비어있음);
    }

    @DisplayName("Order 중 특정 상태에 해당하는 것이 있는지 TableId로 검색하면, 올바르게 수행된다.")
    @Test
    void existsByOrderTableIdAndOrderStatusInTest() {
        OrderTable orderTable1 = new OrderTable(테이블_사람_1명, 테이블_비어있음);
        OrderTable orderTable2 = new OrderTable(테이블_사람_2명, 테이블_비어있음);
        Order order1 = new Order(orderTable1, OrderStatus.COOKING);
        Order order2 = new Order(orderTable2, OrderStatus.COMPLETION);

        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);
        orderRepository.save(order1);
        orderRepository.save(order2);

        assertTrue(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                1L, Arrays.asList(OrderStatus.COOKING)));
        assertFalse(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                2L, Arrays.asList(OrderStatus.COOKING)));

    }
}
