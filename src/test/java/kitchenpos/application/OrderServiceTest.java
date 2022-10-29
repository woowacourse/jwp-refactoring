package kitchenpos.application;

import static kitchenpos.Fixture.DomainFixture.GUEST_NUMBER;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
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

    private OrderLineItemDto orderLineItemDto1;
    private OrderLineItemDto orderLineItemDto2;
    private Menu menu1;
    private Menu menu2;

    @BeforeEach
    void setUp() {
        Product product = productRepository.save(Product.of("상품1", new BigDecimal(2500.0)));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));
        MenuProduct menuProduct1 = new MenuProduct(null, product, Quantity.from(2L));
        MenuProduct menuProduct2 = new MenuProduct(null, product, Quantity.from(3L));
        menu1 = menuRepository.save(Menu.of("메뉴1", Price.from(new BigDecimal(5000.0)), menuGroup,
                List.of(menuProduct1, menuProduct2)));
        menu2 = menuRepository
                .save(Menu.of("메뉴2", Price.from(new BigDecimal(4500.0)), menuGroup, List.of(menuProduct2)));

        orderLineItemDto1 = new OrderLineItemDto(menu1.getId(), 2L);
        orderLineItemDto2 = new OrderLineItemDto(menu2.getId(), 1L);
    }

    @DisplayName("Order를 등록할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = tableRepository.save(new OrderTable(null, GUEST_NUMBER, false, null));
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), null,
                List.of(orderLineItemDto1, orderLineItemDto2));

        orderService.create(orderRequest);

        assertThat(orderRepository.findAll()).hasSize(1);
    }

    @DisplayName("Menu 없이 Order를 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_EmptyMenu() {
        OrderTable emptyOrderTable = tableRepository.save(new OrderTable(null, GUEST_NUMBER, true, null));
        OrderRequest orderRequest = new OrderRequest(emptyOrderTable.getId(), null, Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(MenuNotEnoughException.class);
    }

    @DisplayName("존재하지 않는 Menu로 Order를 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_NotFoundMenu() {
        OrderLineItemDto notFoundOrderLineItem = new OrderLineItemDto(1000L, 2L);
        OrderTable emptyOrderTable = tableRepository.save(new OrderTable(null, GUEST_NUMBER, false, null));
        OrderRequest orderRequest = new OrderRequest(emptyOrderTable.getId(), null, List.of(notFoundOrderLineItem));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴를 찾을 수 없습니다.");
    }

    @DisplayName("empty인 Table에 해당하는 Order를 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_EmptyTable() {
        OrderTable emptyOrderTable = tableRepository.save(new OrderTable(null, GUEST_NUMBER, true, null));
        OrderRequest orderRequest = new OrderRequest(emptyOrderTable.getId(), null,
                List.of(orderLineItemDto1, orderLineItemDto2));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(EmptyTableOrderException.class);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = tableRepository.save(new OrderTable(null, GUEST_NUMBER, false, null));
        OrderResponse order = orderService.create(
                new OrderRequest(orderTable.getId(), null, List.of(orderLineItemDto1, orderLineItemDto2)));

        orderService.changeOrderStatus(
                order.getId(), new OrderRequest(orderTable.getId(), "MEAL", Collections.emptyList()));

        Order changedOrder = orderRepository.findAll().get(0);
        assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL);
    }

    @DisplayName("주문 상태가 COMPLETION인 주문의 상태를 변경하려고 하면 예외를 발생시킨다.")
    @Test
    void changeOrderStatus_Exception_AlreadyCompletionOrder() {
        OrderTable orderTable = tableRepository.save(new OrderTable(null, GUEST_NUMBER, false, null));
        Order order = orderRepository.save(
                new Order(orderTable, COMPLETION, new ArrayList<>()));

        assertThatThrownBy(() -> orderService.changeOrderStatus(
                order.getId(), new OrderRequest(orderTable.getId(), "MEAL", Collections.emptyList())))
                .isInstanceOf(AlreadyCompletionOrderStatusException.class);
    }
}
