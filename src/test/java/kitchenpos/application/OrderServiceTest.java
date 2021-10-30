package kitchenpos.application;

import kitchenpos.annotation.IntegrationTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    private OrderLineItem orderLineItem;
    private OrderTable notEmptyTable;
    private Order mealStatusOrder;
    private Order completionStatusOrder;

    @BeforeEach
    void setUp() {
        orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        notEmptyTable = new OrderTable();
        notEmptyTable.setEmpty(false);

        mealStatusOrder = new Order();
        mealStatusOrder.setOrderStatus("MEAL");

        completionStatusOrder = new Order();
        completionStatusOrder.setOrderStatus("COMPLETION");
    }

    @Test
    @DisplayName("Order 요청 시에는 order_line_item이 반드시 있어야 한다.(메뉴 주문을 무조건 해야 한다)")
    public void orderLineItemEmptyException() {
        //given
        Order order = new Order();
        order.setOrderTableId(1L);

        //when
        order.setOrderLineItems(Collections.emptyList());

        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("동일한 Menu가 별개의 order_line_item에 들어있어서는 안된다.(메뉴의 종류의 개수와 order_line_item의 개수가 같아야 한다)")
    public void notDistinctOrderLineItemsException() {
        //given
        Order order = new Order();
        order.setOrderTableId(1L);

        //when
        order.setOrderLineItems(Arrays.asList(orderLineItem, orderLineItem));

        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 order_table에서 order 요청을 할 수 없다.")
    public void emptyOrderTableOrderException() {
        //given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        //when
        order.setOrderTableId(1L);

        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Order를 등록할 수 있다.")
    public void enrollOrder() {
        //given
        Order order = creatableOrder();

        //when * then
        assertDoesNotThrow(() -> orderService.create(order));
    }

    @Test
    @DisplayName("Order 생성 후에 OrderLineItem의 order_id에 생성된 Order의 id가 할당되어야 한다.")
    public void allocateOrderIdIntoOrderLineItem() {
        //given
        Order order = creatableOrder();

        //when & then
        Order savedOrder = orderService.create(order);
        for (OrderLineItem lineItem : savedOrder.getOrderLineItems()) {
            assertThat(lineItem.getOrderId()).isNotNull();
        }
    }

    @Test
    @DisplayName("존재하는 Order 조회를 할 수 있다.")
    public void findAll() {
        //given
        Order order = creatableOrder();

        //when
        orderService.create(order);
        List<Order> orders = orderService.list();

        //then
        assertThat(orders).hasSize(1);
        for (Order o : orders) {
            assertThat(o.getOrderLineItems()).isNotEmpty();
        }
    }

    @Test
    @DisplayName("존재하지 않는 Order의 상태를 바꿀 수 없다.")
    public void changeNotExistOrderStatusException() {
        //given & when
        Order order = new Order();
        order.setOrderStatus("COMPLETION");

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(10L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Order의 상태를 바꿀 수 있다.")
    public void changeOrderStatus() {
        //given
        Order order = creatableOrder();

        //when
        Order savedOrder = orderService.create(order);

        //then
        assertDoesNotThrow(() -> orderService.changeOrderStatus(savedOrder.getId(), mealStatusOrder));
    }

    @Test
    @DisplayName("상태 변경 후에는 order_line_item을 포함한 Order를 반환받는다.")
    public void receiveOrderAfterChangeOrderStatus() {
        //given
        Order order = creatableOrder();

        //when
        Order savedOrder = orderService.create(order);
        Order statusChangedOrder = orderService.changeOrderStatus(savedOrder.getId(), mealStatusOrder);

        //then
        assertThat(statusChangedOrder.getId()).isEqualTo(savedOrder.getId());
        assertThat(statusChangedOrder.getOrderLineItems()).isNotEmpty();
    }

    @Test
    @DisplayName("이미 COMPLETION 상태의 Order는 상태를 변경할 수 없다.")
    public void changeCompletionStatusOrderException() {
        //given
        Order order = creatableOrder();

        //when
        Order savedOrder = orderService.create(order);
        orderService.changeOrderStatus(savedOrder.getId(), mealStatusOrder);
        orderService.changeOrderStatus(savedOrder.getId(), completionStatusOrder);

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), completionStatusOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Order creatableOrder() {
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        tableService.changeEmpty(1L, notEmptyTable);
        order.setOrderTableId(1L);

        return order;
    }
}