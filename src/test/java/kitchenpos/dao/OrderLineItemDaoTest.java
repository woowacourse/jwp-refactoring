package kitchenpos.dao;

import static kitchenpos.constants.DaoConstants.TEST_ORDER_LINE_ITEM_QUANTITY;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderLineItemDaoTest extends KitchenPosDaoTest {

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @DisplayName("OrderLineItem 저장 - 성공")
    @Test
    void save_Success() {
        Long orderId = getCreatedOrderId();
        Long menuId = getCreatedMenuId();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        assertThat(savedOrderLineItem.getSeq()).isNotNull();
        assertThat(savedOrderLineItem.getOrderId()).isEqualTo(orderId);
        assertThat(savedOrderLineItem.getMenuId()).isEqualTo(menuId);
        assertThat(savedOrderLineItem.getQuantity()).isEqualTo(TEST_ORDER_LINE_ITEM_QUANTITY);
    }

    @DisplayName("OrderLineItem ID로 OrderLineItem 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnOrderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(getCreatedOrderId());
        orderLineItem.setMenuId(getCreatedMenuId());
        orderLineItem.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        OrderLineItem foundOrderLineItem = orderLineItemDao.findById(savedOrderLineItem.getSeq())
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 orderLineItem이 없습니다."));

        assertThat(foundOrderLineItem.getSeq()).isEqualTo(savedOrderLineItem.getSeq());
        assertThat(foundOrderLineItem.getOrderId()).isEqualTo(savedOrderLineItem.getOrderId());
        assertThat(foundOrderLineItem.getMenuId()).isEqualTo(savedOrderLineItem.getMenuId());
        assertThat(foundOrderLineItem.getQuantity()).isEqualTo(savedOrderLineItem.getQuantity());
    }

    @DisplayName("OrderLineItem ID로 OrderLineItem 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(getCreatedOrderId());
        orderLineItem.setMenuId(getCreatedMenuId());
        orderLineItem.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        Optional<OrderLineItem> foundOrderLineItem
            = orderLineItemDao.findById(savedOrderLineItem.getSeq() + 1);

        assertThat(foundOrderLineItem.isPresent()).isFalse();
    }

    @DisplayName("전체 OrderLineItem 조회 - 성공")
    @Test
    void findAll_Success() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(getCreatedOrderId());
        orderLineItem.setMenuId(getCreatedMenuId());
        orderLineItem.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();

        assertThat(orderLineItems).isNotNull();
        assertThat(orderLineItems).isNotEmpty();

        List<Long> orderLineItemIds = getIds(orderLineItems);

        assertThat(orderLineItemIds).contains(savedOrderLineItem.getSeq());
    }

    @DisplayName("Order ID로 OrderLineItems 조회 - 조회됨, Order ID가 존재하는 경우")
    @Test
    void findAllByOrderId_ExistsOrderId_Success() {
        Long orderId = getCreatedOrderId();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(getCreatedMenuId());
        orderLineItem.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        OrderLineItem otherOrderLineItem = new OrderLineItem();
        otherOrderLineItem.setOrderId(orderId);
        otherOrderLineItem.setMenuId(getCreatedMenuId());
        otherOrderLineItem.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);
        OrderLineItem savedOtherOrderLineItem = orderLineItemDao.save(otherOrderLineItem);

        OrderLineItem orderLineItemWithOtherOrderId = new OrderLineItem();
        orderLineItemWithOtherOrderId.setOrderId(orderId);
        orderLineItemWithOtherOrderId.setMenuId(getCreatedMenuId());
        orderLineItemWithOtherOrderId.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);
        orderLineItemDao.save(orderLineItemWithOtherOrderId);

        List<Long> ids
            = Arrays.asList(savedOrderLineItem.getSeq(), savedOtherOrderLineItem.getSeq());

        List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(orderId);
        assertThat(orderLineItems).hasSize(ids.size());

        List<Long> orderLineItemIds = getIds(orderLineItems);
        assertThat(orderLineItemIds).containsAll(ids);
    }

    @DisplayName("Order ID로 OrderLineItems 조회 - 조회되지 않음, Order ID가 존재하지 않는 경우")
    @Test
    void findAllByOrderId_NotExistsOrderId_Success() {
        Long orderId = getCreatedOrderId();

        List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(orderId);
        assertThat(orderLineItems).isEmpty();
    }


    private List<Long> getIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItem::getSeq)
            .collect(Collectors.toList());
    }
}
