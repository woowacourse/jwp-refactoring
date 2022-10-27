package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;

class OrderServiceTest extends ServiceTest {

    @Autowired
    protected OrderService orderService;
    @Autowired
    protected MenuService menuService;
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected MenuGroupRepository menuGroupRepository;
    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Menu createdMenu = createMenu(createdMenuGroup);

        OrderTable orderTable = new OrderTable(3, false);
        OrderTable createdOrderTable = orderTableRepository.save(orderTable);

        // when
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(createdOrderTable.getId(),
            List.of(new OrderLineItemCreateRequest(createdMenu.getId(), 10)));

        Order createdOrder = orderService.create(orderCreateRequest);

        // then
        assertAll(
            () -> assertThat(createdOrder.getOrderTable()).isEqualTo(createdOrderTable),
            () -> assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
            () -> assertThat(createdOrder.getOrderLineItems()).hasSize(1)
        );
    }

    @Test
    @DisplayName("주문 항목이 비어있을 수 없다")
    void emptyOrderLineItems() {
        // given
        OrderTable orderTable = new OrderTable(3, false);
        OrderTable createdOrderTable = orderTableRepository.save(orderTable);

        // when
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(createdOrderTable.getId(),
            List.of());

        // then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목에 해당하는 메뉴가 모두 저장되어 있어야 한다")
    void nonRegisteredMenu() {
        // given
        OrderTable orderTable = new OrderTable(3, false);
        OrderTable createdOrderTable = orderTableRepository.save(orderTable);

        // when
        Long fakeMenuId = 999L;

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(createdOrderTable.getId(),
            List.of(new OrderLineItemCreateRequest(fakeMenuId, 10)));

        // then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목에 해당하는 주문 테이블이 함께 등록되어야 한다")
    void withoutOrderTable() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Menu createdMenu = createMenu(createdMenuGroup);

        Long fakeOrderTableId = 999L;

        // when
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(fakeOrderTableId,
            List.of(new OrderLineItemCreateRequest(createdMenu.getId(), 10)));

        // then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있을 수 없다")
    void emptyOrderTable() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Menu createdMenu = createMenu(createdMenuGroup);

        OrderTable orderTable = new OrderTable(3, true);
        OrderTable createdOrderTable = orderTableRepository.save(orderTable);

        // when
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(createdOrderTable.getId(),
            List.of(new OrderLineItemCreateRequest(createdMenu.getId(), 10)));

        // then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다")
    void list() {
        // given
        Order createdOrder = createOrder();

        // when
        List<Order> orders = orderService.list();

        // then
        assertAll(
            () -> assertThat(orders).hasSize(1),
            () -> assertThat(orders.get(0).getId()).isEqualTo(createdOrder.getId()),
            () -> assertThat(orders.get(0).getOrderTable()).isEqualTo(createdOrder.getOrderTable())
        );
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        // given
        Order createdOrder = createOrder();

        // when
        Order changedOrder = orderService.changeOrderStatus(createdOrder.getId(), new OrderStatusUpdateRequest(OrderStatus.MEAL));

        // then
        assertAll(
            () -> assertThat(changedOrder.getId()).isEqualTo(createdOrder.getId()),
            () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL)
        );
    }

    @Test
    @DisplayName("등록되지 않은 주문 아이디로 변경할 수 없다")
    void nonRegisteredOrderId() {
        // given
        Long fakeOrderId = 999L;

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(fakeOrderId, new OrderStatusUpdateRequest(OrderStatus.MEAL)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 완료된 주문은 변경할 수 없다")
    void alreadyCompletedOrder() {
        // given
        Order createdOrder = createOrder();
        createdOrder.updateStatus(OrderStatus.COMPLETION);

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(createdOrder.getId(), new OrderStatusUpdateRequest(OrderStatus.MEAL)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private MenuGroup createMenuGroup() {
        MenuGroup menuGroup = new MenuGroup("testGroup");
        return menuGroupRepository.save(menuGroup);
    }

    private Menu createMenu(MenuGroup menuGroup) {
        Product product = productRepository.save(new Product("testProduct", BigDecimal.TEN));

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("test", BigDecimal.ONE, menuGroup.getId(),
            List.of(new MenuProductCreateRequest(product.getId(), 1)));
        return menuService.create(menuCreateRequest);
    }

    private Order createOrder() {
        MenuGroup createdMenuGroup = createMenuGroup();
        Menu createdMenu = createMenu(createdMenuGroup);

        OrderTable orderTable = new OrderTable(3, false);
        OrderTable createdOrderTable = orderTableRepository.save(orderTable);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(createdOrderTable.getId(),
            List.of(new OrderLineItemCreateRequest(createdMenu.getId(), 10)));

        return orderService.create(orderCreateRequest);
    }
}
