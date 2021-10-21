package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문Dao 테스트")
class JdbcTemplateOrderDaoTest extends DaoTest {

    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new JdbcTemplateOrderDao(dataSource);
    }

    @DisplayName("주문을 저장한다.")
    @Test
    void save() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        // when
        Order savedOrder = orderDao.save(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
    }

    @DisplayName("id로 주문을 조회한다.")
    @Test
    void findById() {
        // given
        long orderId = SAVE_ORDER();

        // when
        Optional<Order> findOrder = orderDao.findById(orderId);

        // then
        assertThat(findOrder).isPresent();
        Order order = findOrder.get();
        assertThat(order.getId()).isEqualTo(orderId);
    }

    @DisplayName("모든 주문을 조회한다.")
    @Test
    void findAll() {
        // given
        SAVE_ORDER();

        // when
        List<Order> orders = orderDao.findAll();

        // then
        // 초기화를 통해 등록된 메뉴 8개 + 새로 추가한 메뉴 1개
        assertThat(orders).hasSize(8 + 1);
    }

    private long SAVE_ORDER() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        // when
        Order savedOrder = orderDao.save(order);
        return savedOrder.getId();
    }
}
