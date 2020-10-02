package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.DomainFactory.createOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JdbcTemplateOrderDaoTest extends JdbcTemplateDaoTest {
    @Autowired
    private JdbcTemplateOrderDao jdbcTemplateOrderDao;

    @BeforeEach
    void setUp() {
        saveOrderTable(1L, 0, true);

        orderIds = new ArrayList<>();
    }

    @DisplayName("새로운 주문 저장")
    @Test
    void saveNewOrderTest() {
        Order newOrder = createOrder(1L, OrderStatus.COOKING.toString(), LocalDateTime.now());

        Order savedOrder = jdbcTemplateOrderDao.save(newOrder);
        orderIds.add(savedOrder.getId());

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(1L),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString()),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull()
        );
    }

    @DisplayName("기존 주문의 상태 변경")
    @Test
    void saveExistsOrderTest() {
        Order newOrder = createOrder(1L, OrderStatus.COOKING.toString(), LocalDateTime.now());
        Order savedOrder = jdbcTemplateOrderDao.save(newOrder);
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.toString());

        Order changeOrder = jdbcTemplateOrderDao.save(savedOrder);
        orderIds.add(changeOrder.getId());

        assertAll(
                () -> assertThat(changeOrder.getId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(changeOrder.getOrderTableId()).isEqualTo(savedOrder.getOrderTableId()),
                () -> assertThat(changeOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.toString()),
                () -> assertThat(changeOrder.getOrderedTime()).isEqualTo(savedOrder.getOrderedTime())
        );
    }

    @DisplayName("아이디로 주문 찾기")
    @Test
    void findByIdTest() {
        Order order = createOrder(1L, OrderStatus.COOKING.toString(), LocalDateTime.now());
        Order savedOrder = jdbcTemplateOrderDao.save(order);

        Order findOrder = jdbcTemplateOrderDao.findById(savedOrder.getId()).get();
        orderIds.add(findOrder.getId());

        assertAll(
                () -> assertThat(findOrder.getId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(findOrder.getOrderTableId()).isEqualTo(savedOrder.getOrderTableId()),
                () -> assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString()),
                () -> assertThat(findOrder.getOrderedTime()).isEqualTo(savedOrder.getOrderedTime())
        );
    }

    @DisplayName("없는 주문 아이디 입력 시 빈 객체 반환")
    @Test
    void findByIdWithInvalidOrderIdTest() {
        Optional<Order> findOrder = jdbcTemplateOrderDao.findById(0L);

        assertThat(findOrder).isEqualTo(Optional.empty());
    }

    @DisplayName("모든 주문 반환")
    @Test
    void findAllTest() {
        Order cookingOrder = createOrder(1L, OrderStatus.COOKING.toString(), LocalDateTime.now());
        Order mealOrder = createOrder(1L, OrderStatus.MEAL.toString(), LocalDateTime.now());
        Order completionOrder = createOrder(1L, OrderStatus.COMPLETION.toString(), LocalDateTime.now());

        jdbcTemplateOrderDao.save(cookingOrder);
        jdbcTemplateOrderDao.save(mealOrder);
        jdbcTemplateOrderDao.save(completionOrder);

        List<Order> allOrders = jdbcTemplateOrderDao.findAll();
        allOrders.forEach(order -> orderIds.add(order.getId()));

        assertThat(allOrders).hasSize(3);
    }

    @DisplayName("테이블에 주문이 존재하는지 확인")
    @Test
    void existsByOrderTableIdAndOrderStatusInTest() {
        Order cookingOrder = createOrder(1L, OrderStatus.COOKING.toString(), LocalDateTime.now());
        Order mealOrder = createOrder(1L, OrderStatus.MEAL.toString(), LocalDateTime.now());
        Order savedCookingOrder = jdbcTemplateOrderDao.save(cookingOrder);
        Order savedMealOrder = jdbcTemplateOrderDao.save(mealOrder);
        List<String> orderStatuses = Arrays.stream(OrderStatus.values())
                .map(Enum::toString)
                .collect(Collectors.toList());

        boolean isExists = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(1L, orderStatuses);
        orderIds.add(savedCookingOrder.getId());
        orderIds.add(savedMealOrder.getId());

        assertThat(isExists).isTrue();
    }

    @DisplayName("주문이 존재하지 않는 테이블에 주문이 없는지 확인")
    @Test
    void existsByNoOrderTableIdAndOrderStatusInTest() {
        List<String> orderStatuses = Arrays.stream(OrderStatus.values())
                .map(Enum::toString)
                .collect(Collectors.toList());

        boolean isExists = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(1L, orderStatuses);

        assertThat(isExists).isFalse();
    }

    @DisplayName("모든 테이블에 주문이 있는지 확인")
    @Test
    void existsByOrderTableIdInAndOrderStatusInTest() {
        saveOrderTable(2L, 0, true);
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        Order cookingOrder = createOrder(1L, OrderStatus.COOKING.toString(), LocalDateTime.now());
        Order mealOrder = createOrder(1L, OrderStatus.MEAL.toString(), LocalDateTime.now());
        Order savedCookingOrder = jdbcTemplateOrderDao.save(cookingOrder);
        Order savedMealOrder = jdbcTemplateOrderDao.save(mealOrder);
        List<String> orderStatuses = Arrays.stream(OrderStatus.values())
                .map(Enum::toString)
                .collect(Collectors.toList());

        boolean isExists = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
        orderIds.add(savedCookingOrder.getId());
        orderIds.add(savedMealOrder.getId());

        assertThat(isExists).isTrue();
    }

    @DisplayName("모든 테이블에 주문이 없는지 확인")
    @Test
    void existsByNoOrderTableIdInAndOrderStatusInTest() {
        saveOrderTable(2L, 0, true);
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        List<String> orderStatuses = Arrays.stream(OrderStatus.values())
                .map(Enum::toString)
                .collect(Collectors.toList());

        boolean isExists = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);

        assertThat(isExists).isFalse();
    }

    @AfterEach
    void tearDown() {
        deleteOrders();
    }
}