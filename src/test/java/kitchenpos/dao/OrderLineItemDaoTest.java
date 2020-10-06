package kitchenpos.dao;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.utils.TestObjectUtils.NOT_EXIST_VALUE;
import static kitchenpos.utils.TestObjectUtils.createOrder;
import static kitchenpos.utils.TestObjectUtils.createOrderLineItem;
import static kitchenpos.utils.TestObjectUtils.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderLineItemDaoTest extends DaoTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderDao orderDao;

    private Long menuId;
    private Long quantity;
    private Order savedOrder;
    private OrderLineItem orderLineItem;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        menuId = 1L;
        quantity = 1L;
        OrderTable savedOrderTable = orderTableDao.save(createOrderTable(4, false));
        orderLineItem = createOrderLineItem(menuId, quantity);
        orderLineItems = Collections.singletonList(orderLineItem);
        Order order = createOrder(savedOrderTable.getId(), COOKING.name(), orderLineItems);
        order.setOrderedTime(LocalDateTime.now());
        savedOrder = orderDao.save(order);
        orderLineItem.setOrderId(savedOrder.getId());
    }

    @DisplayName("주문 항목 save - 성공")
    @Test
    void save() {
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        assertAll(() -> {
            assertThat(savedOrderLineItem.getSeq()).isNotNull();
            assertThat(savedOrderLineItem.getMenuId()).isEqualTo(menuId);
            assertThat(savedOrderLineItem.getQuantity()).isEqualTo(quantity);
        });
    }

    @DisplayName("주문 항목 findById - 성공")
    @Test
    void findById() {
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);
        Optional<OrderLineItem> foundOrderLineItem = orderLineItemDao
            .findById(savedOrderLineItem.getSeq());

        assertThat(foundOrderLineItem.isPresent()).isTrue();
    }

    @DisplayName("주문 항목 findById - 예외, 빈 데이터에 접근하려고 하는 경우")
    @Test
    void findById_EmptyResultDataAccess_ThrownException() {
        Optional<OrderLineItem> foundOrderLineItem = orderLineItemDao.findById(NOT_EXIST_VALUE);

        assertThat(foundOrderLineItem.isPresent()).isFalse();
    }

    @DisplayName("주문 항목 findAll - 성공")
    @Test
    void findAll() {
        List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();

        assertThat(orderLineItems).hasSize(0);
    }

    @DisplayName("주문 항목 findAllByOrderId - 성공")
    @Test
    void findAllByOrderId() {
        orderLineItemDao.save(orderLineItem);

        assertThat(orderLineItemDao.findAllByOrderId(savedOrder.getId())).hasSize(orderLineItems.size());
    }
}
