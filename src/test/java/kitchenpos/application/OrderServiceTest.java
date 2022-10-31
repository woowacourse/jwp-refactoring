package kitchenpos.application;

import static kitchenpos.Fixture.DomainFixture.GUEST_NUMBER;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.Quantity;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.AlreadyCompletionOrderStatusException;
import kitchenpos.exception.EmptyTableOrderException;
import kitchenpos.exception.MenuNotEnoughException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    private OrderLineItemCreateRequest orderLineItemResponse1;
    private OrderLineItemCreateRequest orderLineItemResponse2;

    @BeforeEach
    void setUp() {
        Product product = productRepository.save(new Product("상품1", new Price(new BigDecimal(2500))));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));
        MenuProduct menuProduct1 = new MenuProduct(product, new Quantity(2L));
        MenuProduct menuProduct2 = new MenuProduct(product, new Quantity(3L));
        Menu menu1 = menuRepository.save(new Menu("메뉴1", new Price(new BigDecimal(5000)), menuGroup,
                List.of(menuProduct1)));
        Menu menu2 = menuRepository
                .save(new Menu("메뉴2", new Price(new BigDecimal(4500)), menuGroup, List.of(menuProduct2)));

        orderLineItemResponse1 = new OrderLineItemCreateRequest(menu1.getId(), 2L);
        orderLineItemResponse2 = new OrderLineItemCreateRequest(menu2.getId(), 1L);
    }

    @DisplayName("Order를 등록할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = tableRepository.save(new OrderTable(GUEST_NUMBER, false));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(), null,
                List.of(orderLineItemResponse1, orderLineItemResponse2));

        orderService.create(orderCreateRequest);

        assertThat(orderRepository.findAll()).hasSize(1);
    }

    @DisplayName("Menu 없이 Order를 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_EmptyMenu() {
        OrderTable emptyOrderTable = tableRepository.save(new OrderTable(GUEST_NUMBER, true));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(emptyOrderTable.getId(), null,
                Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(MenuNotEnoughException.class);
    }

    @DisplayName("존재하지 않는 Menu로 Order를 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_NotFoundMenu() {
        OrderLineItemCreateRequest notFoundOrderLineItem = new OrderLineItemCreateRequest(1000L, 2L);
        OrderTable emptyOrderTable = tableRepository.save(new OrderTable(GUEST_NUMBER, false));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(emptyOrderTable.getId(), null,
                List.of(notFoundOrderLineItem));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴를 찾을 수 없습니다.");
    }

    @DisplayName("empty인 Table에 해당하는 Order를 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_EmptyTable() {
        OrderTable emptyOrderTable = tableRepository.save(new OrderTable(GUEST_NUMBER, true));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(emptyOrderTable.getId(), null,
                List.of(orderLineItemResponse1, orderLineItemResponse2));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(EmptyTableOrderException.class);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = tableRepository.save(new OrderTable(GUEST_NUMBER, false));
        OrderResponse order = orderService.create(
                new OrderCreateRequest(orderTable.getId(), null,
                        List.of(orderLineItemResponse1, orderLineItemResponse2)));

        orderService.changeOrderStatus(
                order.getId(), new OrderStatusChangeRequest("MEAL"));

        Order changedOrder = orderRepository.findAll().get(0);
        assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL);
    }

    @DisplayName("주문 상태가 COMPLETION인 주문의 상태를 변경하려고 하면 예외를 발생시킨다.")
    @Test
    void changeOrderStatus_Exception_AlreadyCompletionOrder() {
        OrderTable orderTable = tableRepository.save(new OrderTable(GUEST_NUMBER, false));
        Order order = Order.newOrder(orderTable);
        order.changeOrderStatus(COMPLETION);
        Order savedOrder = orderRepository.save(order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(
                savedOrder.getId(), new OrderStatusChangeRequest("MEAL")))
                .isInstanceOf(AlreadyCompletionOrderStatusException.class);
    }
}
