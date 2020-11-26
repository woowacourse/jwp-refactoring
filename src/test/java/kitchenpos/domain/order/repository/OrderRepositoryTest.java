package kitchenpos.domain.order.repository;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
public class OrderRepositoryTest {
    private static final Integer 테이블_사람_1명 = 1;
    private static final Integer 테이블_사람_2명 = 2;
    private static final Boolean 테이블_비어있음 = true;

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("Order 중 특정 상태에 해당하는 것이 있는지 TableId로 검색하면, 올바르게 수행된다.")
    @Test
    void existsByOrderTableIdAndOrderStatusInTest() {
        OrderTable orderTable1 = new OrderTable(테이블_사람_1명, 테이블_비어있음);
        OrderTable orderTable2 = new OrderTable(테이블_사람_2명, 테이블_비어있음);
        Order order1 = new Order(orderTable1, OrderStatus.COOKING);
        Order order2 = new Order(orderTable2, OrderStatus.COMPLETION);

        OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);
        orderRepository.save(order1);
        orderRepository.save(order2);

        assertTrue(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                savedOrderTable1.getId(), Arrays.asList(OrderStatus.COOKING)));
        assertFalse(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                savedOrderTable2.getId(), Arrays.asList(OrderStatus.COOKING)));

    }
}
