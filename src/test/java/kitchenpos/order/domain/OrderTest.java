package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTest {

    private final Menu pizza = new Menu(1L, "pizza", BigDecimal.TEN, new MenuGroup(1L, "pizza"),
            List.of(new MenuProduct(new Product("pizza", BigDecimal.TEN), 3)));

    @Test
    void 빈_테이블이_주문_생성_시_예외_발생() {
        assertThatThrownBy(() -> new Order(new OrderTable(1L, 3, true), OrderStatus.COOKING, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Order의_OrderLineItems_와_주문하려는_메뉴_개수가_다르면_예외발생() {
        Order order = new Order(new OrderTable(1L, 3, false), OrderStatus.COOKING, LocalDateTime.now());
        order.addOrderLineItem(new OrderLineItem(pizza, 3));
        assertThatThrownBy(() -> order.checkEqualMenuCount(2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Order의_OrderLineItems_와_주문하려는_메뉴_개수가_같으면_정상() {
        Order order = new Order(new OrderTable(1L, 3, false), OrderStatus.COOKING, LocalDateTime.now());
        order.addOrderLineItem(new OrderLineItem(pizza, 3));
        assertDoesNotThrow(() -> order.checkEqualMenuCount(1));
    }

    @Test
    void 주문의_상태가_COMPLETION_인지_확인한다() {
        Order order = new Order(new OrderTable(1L, 3, false), OrderStatus.COMPLETION, LocalDateTime.now());
        assertThat(order.isCompleted()).isTrue();
    }

    @Test
    void 주문의_상태가_COMPLETION이_아닌지_확인한다() {
        Order order = new Order(new OrderTable(1L, 3, false), OrderStatus.COOKING, LocalDateTime.now());
        assertThat(order.isCompleted()).isFalse();
    }
}
