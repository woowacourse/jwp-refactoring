package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.exception.InvalidOrderLineItemException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderLineItemsTest {

    @Test
    void of_메서드는_유효한_데이터를_전달하면_OrderLineItems를_초기화한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), product.price(), product.name(), 1L);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup.getId());
        final OrderTable orderTable = new OrderTable(0, false);
        final OrderStatus orderStatus = OrderStatus.MEAL;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final Order order = new Order(orderTable.getId(), orderStatus, LocalDateTime.now(), List.of(orderLineItem));

        // when
        final OrderLineItems actual = OrderLineItems.of(order, List.of(orderLineItem));

        // then
        assertAll(
                () -> assertThat(actual.getValues()).hasSize(1),
                () -> assertThat(actual.getValues().get(0).getOrder()).isEqualTo(order)
        );
    }

    @Test
    void of_메서드는_orderItems가_비어_있으면_예외가_발생한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), product.price(), product.name(), 1L);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup.getId());
        final OrderTable orderTable = new OrderTable(0, false);
        final OrderStatus orderStatus = OrderStatus.MEAL;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final Order order = new Order(orderTable.getId(), orderStatus, LocalDateTime.now(), List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> OrderLineItems.of(order, Collections.emptyList()))
                .isInstanceOf(InvalidOrderLineItemException.class);
    }
}
