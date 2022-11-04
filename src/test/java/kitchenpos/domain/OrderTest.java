package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("빈 주문 항목으로 주문을 생성할 경우 예외가 발생한다.")
    void validateEmpty() {
        assertThatThrownBy(() -> Order.builder()
                .orderLineItems(List.of())
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("중복되는 주문 항목으로 주문을 생성할 경우 예외가 발생한다.")
    void validateDuplicate() {
        Product product = Product.builder()
                .price(BigDecimal.valueOf(5000))
                .build();
        MenuProduct menuProduct = MenuProduct.builder()
                .product(product)
                .quantity(1L)
                .build();
        Menu menu = Menu.builder()
                .id(1L)
                .price(BigDecimal.valueOf(5000))
                .menuProducts(List.of(menuProduct))
                .build();
        OrderLineItem orderLineItem = OrderLineItem.builder()
                .menu(menu)
                .build();

        assertThatThrownBy(() -> Order.builder()
                .orderLineItems(List.of(orderLineItem, orderLineItem))
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목엔 중복되는 메뉴가 존재할 수 없습니다.");
    }

    @Test
    @DisplayName("비활성화된 테이블에 주문을 생성할 경우 예외가 발생한다.")
    void validateEmptyOrderTable() {
        Product product = Product.builder()
                .price(BigDecimal.valueOf(5000))
                .build();
        MenuProduct menuProduct = MenuProduct.builder()
                .product(product)
                .quantity(1L)
                .build();
        Menu menu = Menu.builder()
                .id(1L)
                .price(BigDecimal.valueOf(5000))
                .menuProducts(List.of(menuProduct))
                .build();
        OrderLineItem orderLineItem = OrderLineItem.builder()
                .menu(menu)
                .build();
        OrderTable orderTable = OrderTable.builder()
                .empty(true)
                .build();

        assertThatThrownBy(() -> Order.builder()
                .orderLineItems(List.of(orderLineItem))
                .orderTable(orderTable)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비활성화된 주문 테이블은 주문을 받을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        Product product = Product.builder()
                .price(BigDecimal.valueOf(5000))
                .build();
        MenuProduct menuProduct = MenuProduct.builder()
                .product(product)
                .quantity(1L)
                .build();
        Menu menu = Menu.builder()
                .id(1L)
                .price(BigDecimal.valueOf(5000))
                .menuProducts(List.of(menuProduct))
                .build();
        OrderLineItem orderLineItem = OrderLineItem.builder()
                .menu(menu)
                .build();
        OrderTable orderTable = OrderTable.builder()
                .empty(false)
                .build();
        Order order = Order.builder()
                .orderLineItems(List.of(orderLineItem))
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COOKING)
                .build();

        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    @DisplayName("이미 계산 완료된 주문의 상태를 변경할 경우 예외가 발생한다.")
    void changeOrderStatusFails() {
        Product product = Product.builder()
                .price(BigDecimal.valueOf(5000))
                .build();
        MenuProduct menuProduct = MenuProduct.builder()
                .product(product)
                .quantity(1L)
                .build();
        Menu menu = Menu.builder()
                .id(1L)
                .price(BigDecimal.valueOf(5000))
                .menuProducts(List.of(menuProduct))
                .build();
        OrderLineItem orderLineItem = OrderLineItem.builder()
                .menu(menu)
                .build();
        OrderTable orderTable = OrderTable.builder()
                .empty(false)
                .build();
        Order order = Order.builder()
                .orderLineItems(List.of(orderLineItem))
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COMPLETION)
                .build();

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 이미 계산 완료되었습니다.");
    }
}
