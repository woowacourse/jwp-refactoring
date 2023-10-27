package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class OrderTest {

    @Test
    void 주문을_생성할때_주문테이블이_비어있으면_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable(2, true);
        final OrderStatus orderStatus = OrderStatus.COOKING;

        final Product product = new Product("상품", BigDecimal.valueOf(10000));
        final MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(product, 1)));
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Menu menu = new Menu(null, "메뉴", new Price(BigDecimal.valueOf(10000)), menuGroup, menuProducts);
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu, 1));

        // expected
        assertThatThrownBy(() -> new Order(orderTable, orderStatus, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 비어있습니다.");
    }

    @Test
    void 주문이_완료된_상태이면_true를_반환한다() {
        // given
        final OrderTable orderTable = new OrderTable(2, false);
        final OrderStatus orderStatus = OrderStatus.COMPLETION;

        final Product product = new Product("상품", BigDecimal.valueOf(10000));
        final MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(product, 1)));
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Menu menu = new Menu(null, "메뉴", new Price(BigDecimal.valueOf(10000)), menuGroup, menuProducts);
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu, 1));

        final Order order = new Order(orderTable, orderStatus, orderLineItems);

        // when
        final boolean result = order.completed();

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 주문이_완료되지_않은_상태이면_false를_반환한다(final OrderStatus orderStatus) {
        // given
        final OrderTable orderTable = new OrderTable(2, false);

        final Product product = new Product("상품", BigDecimal.valueOf(10000));
        final MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(product, 1)));
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Menu menu = new Menu(null, "메뉴", new Price(BigDecimal.valueOf(10000)), menuGroup, menuProducts);
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu, 1));

        final Order order = new Order(orderTable, orderStatus, orderLineItems);

        // when
        final boolean result = order.completed();

        // then
        assertThat(result).isFalse();
    }
}
