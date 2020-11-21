package kitchenpos.dao;

import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
public class OrderDaoTest {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    private OrderTable orderTable;

    @BeforeEach
    void setup() {
        orderTable = orderTableDao.save(createOrderTable(null, true, 0, null));
    }

    @DisplayName("주문을 저장할 수 있다.")
    @Test
    void save() {
        Order order = createOrder(null, LocalDateTime.now(), OrderStatus.COOKING,
            orderTable.getId());

        Order savedOrder = orderDao.save(order);

        assertAll(
            () -> assertThat(savedOrder.getId()).isNotNull(),
            () -> assertThat(savedOrder).isEqualToIgnoringGivenFields(order, "id")
        );
    }

    @DisplayName("주문 아이디로 주문을 조회할 수 있다.")
    @Test
    void findById() {
        Order order = orderDao.save(
            createOrder(null, LocalDateTime.now(), OrderStatus.COOKING, orderTable.getId()));

        Optional<Order> foundOrder = orderDao.findById(order.getId());

        assertThat(foundOrder.get()).isEqualToComparingFieldByField(order);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        List<Order> savedOrders = Arrays.asList(
            orderDao.save(createOrder(null, LocalDateTime.now(), OrderStatus.COOKING,
                orderTable.getId())),
            orderDao.save(createOrder(null, LocalDateTime.now(), OrderStatus.COOKING,
                orderTable.getId())),
            orderDao.save(createOrder(null, LocalDateTime.now(), OrderStatus.COOKING,
                orderTable.getId()))
        );

        List<Order> allOrders = orderDao.findAll();

        assertThat(allOrders).usingFieldByFieldElementComparator().containsAll(savedOrders);
    }

    @DisplayName("주문 테이블 아이디와 주문 상태 목록이 주어지면 주문 존재 여부를 확인할 수 있다.")
    @MethodSource("provideOrderStatusesAndExpected")
    @ParameterizedTest
    void existsByOrderTableIdAndOrderStatusIn(List<OrderStatus> orderStatuses,
        List<OrderStatus> condition, boolean expected) {
        List<Order> savedOrders = orderStatuses.stream().map(it -> orderDao.save(
            createOrder(null, LocalDateTime.now(), it, orderTable.getId())))
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(),
                condition.stream()
                    .map(OrderStatus::name)
                    .collect(Collectors.toList()))
            ).isEqualTo(expected),
            () -> assertThat(orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId() + 1,
                condition.stream()
                    .map(OrderStatus::name)
                    .collect(Collectors.toList()))
            ).isFalse()
        );
    }

    private static Stream<Arguments> provideOrderStatusesAndExpected() {
        return Stream.of(
            Arguments.of(
                Arrays.asList(OrderStatus.COOKING),
                Arrays.asList(OrderStatus.COOKING),
                true
            ),
            Arguments.of(
                Arrays.asList(OrderStatus.COOKING),
                Arrays.asList(OrderStatus.MEAL),
                false
            ),
            Arguments.of(
                Arrays.asList(OrderStatus.COOKING, OrderStatus.COMPLETION),
                Arrays.asList(OrderStatus.COOKING),
                true
            ),
            Arguments.of(
                Arrays.asList(OrderStatus.COOKING, OrderStatus.COMPLETION),
                Arrays.asList(OrderStatus.MEAL),
                false
            ),
            Arguments.of(
                Arrays.asList(OrderStatus.COOKING, OrderStatus.COMPLETION),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.COMPLETION),
                true
            ),
            Arguments.of(
                Arrays.asList(OrderStatus.COOKING, OrderStatus.COMPLETION),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL),
                true
            ),
            Arguments.of(
                Arrays.asList(OrderStatus.COOKING, OrderStatus.COMPLETION),
                Arrays.asList(OrderStatus.MEAL, OrderStatus.MEAL),
                false
            )
        );
    }

    @DisplayName("주문 테이블 아이디 목록과 주문 상태 목록이 주어지면 주문 존재 여부를 확인할 수 있다.")
    @MethodSource("provideOrderStatusesAndExpected")
    @ParameterizedTest
    void existsByOrderTableIdInAndOrderStatusIn(List<OrderStatus> orderStatuses,
        List<OrderStatus> condition, boolean expected) {
        List<Order> savedOrders = orderStatuses.stream().map(it -> orderDao.save(
            createOrder(null, LocalDateTime.now(), it, orderTable.getId())))
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(orderTable.getId(), orderTable.getId() + 1),
                condition.stream()
                    .map(OrderStatus::name)
                    .collect(Collectors.toList()))
            ).isEqualTo(expected),
            () -> assertThat(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(orderTable.getId() + 1, orderTable.getId() + 2),
                condition.stream()
                    .map(OrderStatus::name)
                    .collect(Collectors.toList()))
            ).isFalse()
        );
    }
}
