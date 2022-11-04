package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @DisplayName("정상적인 경우 주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        final OrderTable orderTable = new OrderTable(5, false);
        final MenuGroup menuGroup = new MenuGroup("신메뉴 그룹");
        final Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        final MenuProduct menuProduct = new MenuProduct(product, 5);
        final Menu menu = new Menu("후라이드", BigDecimal.valueOf(16000), menuGroup, List.of(menuProduct));
        final Order sut = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(),
                List.of(new OrderLineItem(menu.getName(), menu.getPrice(), 1)));

        sut.changeOrderStatus(OrderStatus.MEAL);

        assertThat(sut.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문이 계산 완료 상태면 변경할 수 없다.")
    @Test
    void changeOrderStatusWithCompletionOrder() {
        final OrderTable orderTable = new OrderTable(5, false);
        final MenuGroup menuGroup = new MenuGroup("신메뉴 그룹");
        final Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        final MenuProduct menuProduct = new MenuProduct(product, 5);
        final Menu menu = new Menu("후라이드", BigDecimal.valueOf(16000), menuGroup, List.of(menuProduct));
        final Order sut = new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.now(),
                List.of(new OrderLineItem(menu.getName(), menu.getPrice(), 1)));

        assertThatThrownBy(() -> sut.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
