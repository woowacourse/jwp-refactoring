package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문항목Dao 테스트")
class JdbcTemplateOrderLineItemDaoTest extends DomainDaoTest {

    private OrderLineItemDao orderLineItemDao;

    @BeforeEach
    void setUp() {
        orderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
    }

    @DisplayName("주문항목을 저장한다.")
    @Test
    void save() {
        // given
        long orderId = SAVE_ORDER_RETURN_ID();
        OrderLineItem orderLineItem = new OrderLineItem(orderId, 1L, 1);

        // when
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        // then
        assertThat(savedOrderLineItem.getSeq()).isNotNull();
        assertThat(savedOrderLineItem.getMenuId()).isEqualTo(orderLineItem.getMenuId());
        assertThat(savedOrderLineItem.getOrderId()).isEqualTo(orderLineItem.getOrderId());
        assertThat(savedOrderLineItem.getQuantity()).isEqualTo(orderLineItem.getQuantity());
    }

    @DisplayName("id로 주문항목을 조회한다.")
    @Test
    void findById() {
        // given
        long orderId = SAVE_ORDER_RETURN_ID();
        long orderLineItemId = SAVE_ORDER_LINE_ITEM(orderId);

        // when
        Optional<OrderLineItem> findOrderLineItem = orderLineItemDao.findById(orderLineItemId);

        // then
        assertThat(findOrderLineItem).isPresent();
        OrderLineItem orderLineItem = findOrderLineItem.get();
        assertThat(orderLineItem.getSeq()).isEqualTo(orderLineItemId);
        assertThat(orderLineItem.getOrderId()).isEqualTo(orderId);
    }

    @DisplayName("모든 주문항목을 조회한다.")
    @Test
    void findAll() {
        // given
        long orderId = SAVE_ORDER_RETURN_ID();
        SAVE_ORDER_LINE_ITEM(orderId);

        // when
        List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();

        // then
        assertThat(orderLineItems).hasSize(1);
    }

    @DisplayName("주문Id에 해당하는 주문항목을 모두 조회한다.")
    @Test
    void findAllByOrderId() {
        // given
        long orderId = SAVE_ORDER_RETURN_ID();
        SAVE_ORDER_LINE_ITEM(orderId);

        // when
        List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(orderId);

        // then
        assertThat(orderLineItems).hasSize(1);
    }

    private long SAVE_ORDER_LINE_ITEM(long orderId) {
        OrderLineItem orderLineItem = new OrderLineItem(orderId, 1L, 1);

        // when
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);
        return savedOrderLineItem.getSeq();
    }
}
