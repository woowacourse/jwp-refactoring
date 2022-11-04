package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @Test
    @DisplayName("주문 상품에 주문을 지정한다.")
    void arrangeOrder() {
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
                .name(menu.getName())
                .price(menu.getPrice())
                .build();
        OrderTable orderTable = OrderTable.builder()
                .empty(false)
                .build();
        Order order = Order.builder()
                .orderLineItems(List.of(orderLineItem))
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COOKING)
                .build();

        assertThat(orderLineItem.getOrder()).isEqualTo(order);
    }
}
