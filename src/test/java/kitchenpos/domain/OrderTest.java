package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    private final Product product = new Product.Builder()
            .name("product")
            .price(BigDecimal.valueOf(10000))
            .build();

    private final MenuProduct menuProduct = new MenuProduct.Builder()
            .product(product)
            .quantity(1L)
            .build();

    private final Menu menu = new Menu.Builder()
            .name("menu")
            .price(BigDecimal.valueOf(10000))
            .menuGroup(null)
            .menuProducts(Arrays.asList(menuProduct))
            .build();

    private final OrderTable orderTable = new OrderTable.Builder()
            .id(100L)
            .numberOfGuests(4)
            .empty(false)
            .build();

    private final OrderLineItem orderLineItem = new OrderLineItem.Builder()
            .menu(menu)
            .quantity(1L)
            .build();

    @DisplayName("빌더 패턴으로 Order를 생성할 수 있다.")
    @Test
    void createOrderWithBuilder() {
        final Order order = new Order.Builder()
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.MAX)
                .orderLineItems(Arrays.asList(orderLineItem))
                .build();

        assertThat(order.getOrderTable()).isEqualTo(orderTable);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderedTime()).isEqualTo(LocalDateTime.MAX);
        assertThat(order.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("주문 생성 시 orderLineItem은 비어있을 수 없다")
    @Test
    void orderLineItemCantBeNull() {
        assertThatThrownBy(() -> new Order.Builder()
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.MAX)
                .orderLineItems(null)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 시 orderLineItem의 메뉴는 중복되면 안된다.")
    @Test
    void orderLineItemMenuShouldNotDuplicate() {
        assertThatThrownBy(() -> new Order.Builder()
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.MAX)
                .orderLineItems(Arrays.asList(orderLineItem, orderLineItem))
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 시 orderLineItem의 OrderStutus는 꼭 Cooking이어야 한다.")
    @Test
    void orderLineItemMenuShouldBeCooking() {
        assertThatThrownBy(() -> new Order.Builder()
                .orderTable(orderTable)
                .orderStatus(OrderStatus.MEAL)
                .orderedTime(LocalDateTime.MAX)
                .orderLineItems(Arrays.asList(orderLineItem))
                .build()
        ).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Order.Builder()
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COMPLETION)
                .orderedTime(LocalDateTime.MAX)
                .orderLineItems(Arrays.asList(orderLineItem))
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 OrderStatus가 Complete라면 변경할 수 없다")
    @Test
    void orderStatusCannotChangeToComplete() {
        final Order order = new Order.Builder()
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.MAX)
                .orderLineItems(Arrays.asList(orderLineItem))
                .build();
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
