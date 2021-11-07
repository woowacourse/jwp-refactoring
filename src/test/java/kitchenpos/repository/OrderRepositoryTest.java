package kitchenpos.repository;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql(scripts = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("OrderRepository 테스트")
class OrderRepositoryTest {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = OrderTableFixture.create();
        orderTableRepository.save(orderTable);
    }

    @DisplayName("주문 추가")
    @Test
    void create() {
        Orders orders = new Orders(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        Orders savedOrder = ordersRepository.save(orders);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문 전체 조회")
    @Test
    void list() {
        Orders order1 = new Orders(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        Orders order2 = new Orders(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        Orders order3 = new Orders(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        ordersRepository.save(order1);
        ordersRepository.save(order2);
        ordersRepository.save(order3);

        List<Orders> results = ordersRepository.findAll();

        assertThat(results).hasSize(3);
    }
}
