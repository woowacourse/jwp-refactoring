package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderStatusRequest;
import kitchenpos.common.exception.InvalidMenuException;
import kitchenpos.common.exception.InvalidOrderException;
import kitchenpos.common.exception.InvalidTableException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ApplicationTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문 생성시 주문 항목이 비어있으면 예외가 발생한다.")
    @Test
    void createOrderWithEmptyOrderLineItem() {
        Long tableId = 주문테이블_생성(new OrderTable(null, 5, false));

        assertThatThrownBy(() -> orderService.create(new OrderRequest(tableId, List.of())))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("주문 항목이 비어있습니다.");
    }

    @DisplayName("주문 생성시 주문 항목의 메뉴가 존재하지 않으면 예외가 발생한다.")
    @Test
    void createOrderWithNotFoundMenu() {
        Long tableId = 주문테이블_생성(new OrderTable(null, 5, false));
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 4);

        assertThatThrownBy(
                () -> orderService.create(new OrderRequest(tableId, List.of(orderLineItemRequest))))
                .isInstanceOf(InvalidMenuException.class)
                .hasMessage("메뉴가 존재하지 않습니다.");
    }

    @DisplayName("주문 생성시 주문이 존재하지 않으면 예외가 발생한다.")
    @Test
    void createOrderWithNotFoundOrderTable() {
        MenuGroup menuGroup = 메뉴그룹_생성(new MenuGroup("메뉴그룹"));
        Product product = 상품_생성(new Product("상품1", BigDecimal.valueOf(19_000)));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1, BigDecimal.valueOf(19_000));
        Menu menu = 메뉴_생성(Menu.create("메뉴12", BigDecimal.valueOf(17_000), menuGroup.getId(), List.of(menuProduct)));
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 4);

        assertThatThrownBy(() -> orderService.create(new OrderRequest(1000L, List.of(orderLineItemRequest))))
                .isInstanceOf(InvalidTableException.class)
                .hasMessage("테이블이 존재하지 않습니다.");
    }

    @DisplayName("주문 생성시 테이블이 빈 테이블이면 예외가 발생한다.")
    @Test
    void createOrderWithEmptyTable() {
        MenuGroup menuGroup = 메뉴그룹_생성(new MenuGroup("메뉴그룹"));
        Product product = 상품_생성(new Product("상품1", BigDecimal.valueOf(19_000)));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1, BigDecimal.valueOf(19_000));
        Menu menu = 메뉴_생성(Menu.create("메뉴11e1", BigDecimal.valueOf(17_000), menuGroup.getId(), List.of(menuProduct)));
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 4);

        Long tableId = 주문테이블_생성(new OrderTable(null, 5, true));

        assertThatThrownBy(
                () -> orderService.create(new OrderRequest(tableId, List.of(orderLineItemRequest))))
                .isInstanceOf(InvalidTableException.class)
                .hasMessage("빈 테이블입니다.");
    }

    @DisplayName("주문 생성시 주문이 완료 상태면 예외가 발생한다.")
    @Test
    void createOrderWithInvalidTableStatus() {
        MenuGroup menuGroup = 메뉴그룹_생성(new MenuGroup("메뉴그룹"));
        Product product = 상품_생성(new Product("상품1", BigDecimal.valueOf(19_000)));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1, BigDecimal.valueOf(19_000));
        Menu menu = 메뉴_생성(Menu.create("메뉴111", BigDecimal.valueOf(17_000), menuGroup.getId(), List.of(menuProduct)));
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 4);

        Long tableId = 주문테이블_생성(new OrderTable(null, 5, true));

        assertThatThrownBy(
                () -> orderService.create(new OrderRequest(tableId, List.of(orderLineItemRequest))))
                .isInstanceOf(InvalidTableException.class)
                .hasMessage("빈 테이블입니다.");
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() {
        MenuGroup menuGroup = 메뉴그룹_생성(new MenuGroup("메뉴그룹"));
        Product product = 상품_생성(new Product("상품1", BigDecimal.valueOf(19_000)));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1, BigDecimal.valueOf(19_000));
        Menu menu = 메뉴_생성(Menu.create("메뉴111", BigDecimal.valueOf(17_000), menuGroup.getId(), List.of(menuProduct)));
        Long tableId = 주문테이블_생성(new OrderTable(null, 5, false));

        orderService.create(new OrderRequest(tableId, List.of(new OrderLineItemRequest(menu.getId(), 2))));

        List<OrderResponse> orders = orderService.list();

        assertThat(orders).hasSize(1);
    }

    @DisplayName("주문 상태를 변경한다.")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatus(String status) {
        MenuGroup menuGroup = 메뉴그룹_생성(new MenuGroup("메뉴그룹"));
        Product product = 상품_생성(new Product("상품1", BigDecimal.valueOf(19_000)));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1, BigDecimal.valueOf(19_000));
        Menu menu = 메뉴_생성(Menu.create("메뉴111", BigDecimal.valueOf(17_000), menuGroup.getId(), List.of(menuProduct)));
        Long tableId = 주문테이블_생성(new OrderTable(null, 5, false));
        Long orderId = orderService.create(
                new OrderRequest(tableId, List.of(new OrderLineItemRequest(menu.getId(), 2))));

        orderService.changeOrderStatus(orderId, new OrderStatusRequest(status));
        Order order = orderDao.findById(orderId).orElseThrow();

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.find(status));
    }

    @DisplayName("주문 상태 변경시 이미 주문 상태가 완료이면 예외가 발생한다.")
    @Test
    void changeOrderStatusWithCompletionOrder() {
        Long tableId = 주문테이블_생성(new OrderTable(null, 5, false));
        Long orderId = 주문_생성(new Order(tableId, OrderStatus.COMPLETION, LocalDateTime.now()));

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, new OrderStatusRequest("COOKING")))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("주문이 완료 상태입니다.");
    }

    @DisplayName("주문 상태 변경시 주문이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeOrderStatusWithNotFoundOrder() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(1000L, new OrderStatusRequest("MEAL")))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("주문이 존재하지 않습니다.");
    }
}
