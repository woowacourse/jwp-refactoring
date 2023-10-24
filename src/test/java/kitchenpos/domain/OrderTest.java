package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.exception.InvalidOrderLineItemException;
import kitchenpos.domain.exception.InvalidOrderStatusException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 생성자는_유효한_데이터를_전달하면_order를_초기화한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderTable orderTable = new OrderTable(0, false);
        final OrderStatus orderStatus = OrderStatus.COOKING;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);

        // when
        final Order order = new Order(orderTable, orderStatus, LocalDateTime.now(), List.of(orderLineItem));

        // then
        assertAll(
                () -> assertThat(orderLineItem.getOrder()).isEqualTo(order),
                () -> assertThat(orderTable.getOrder()).isEqualTo(order)
        );
    }

    @Test
    void 생성자는_OrderLineItem_컬렉션이_비어_있으면_예외가_발생한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderTable orderTable = new OrderTable(0, false);
        final OrderStatus orderStatus = OrderStatus.COOKING;

        // when & then
        assertThatThrownBy(() -> new Order(orderTable, orderStatus, LocalDateTime.now(), Collections.emptyList()))
                .isInstanceOf(InvalidOrderLineItemException.class);
    }

    @ParameterizedTest(name = "OrderStatus가 {0}일 때 정상 처리된다.")
    @EnumSource(value = OrderStatus.class)
    void updateOrderStatus_메서드는_Order의_상태가_COMPLETION이_아니면_전달한_OrderStatus로_변경한다(final OrderStatus changeOrderStatus) {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderTable orderTable = new OrderTable(0, false);
        final OrderStatus orderStatus = OrderStatus.COOKING;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final Order order = new Order(orderTable, orderStatus, LocalDateTime.now(), List.of(orderLineItem));

        // when
        order.updateOrderStatus(changeOrderStatus);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(changeOrderStatus);
    }

    @ParameterizedTest(name = "OrderStatus가 {0}일 때 정상 처리된다.")
    @EnumSource(value = OrderStatus.class)
    void updateOrderStatus_메서드는_Order의_상태가_COMPLETION이면_예외가_발생한다(final OrderStatus changeOrderStatus) {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderTable orderTable = new OrderTable(0, false);
        final OrderStatus orderStatus = OrderStatus.COMPLETION;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final Order order = new Order(orderTable, orderStatus, LocalDateTime.now(), List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> order.updateOrderStatus(changeOrderStatus))
                .isInstanceOf(InvalidOrderStatusException.class);
    }

    @ParameterizedTest(name = "OrderStatsu가 {0}일 때 false를 반환한다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    void isCompletion_메서드는_orderStatus가_COMPLETION이_아니라면_false를_반환한다(final String orderStatusName) {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderTable orderTable = new OrderTable(0, false);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusName);
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final Order order = new Order(orderTable, orderStatus, LocalDateTime.now(), List.of(orderLineItem));

        // when
        final boolean actual = order.isCompletion();

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void isCompletion_메서드는_orderStatus가_COMPLETION이라면_true를_반환한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderTable orderTable = new OrderTable(0, false);
        final OrderStatus orderStatus = OrderStatus.COMPLETION;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final Order order = new Order(orderTable, orderStatus, LocalDateTime.now(), List.of(orderLineItem));

        // when
        final boolean actual = order.isCompletion();

        // then
        assertThat(actual).isTrue();
    }
}
