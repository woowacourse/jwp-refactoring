package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.exception.InvalidEmptyOrderTableException;
import kitchenpos.domain.exception.InvalidNumberOfGuestsException;
import kitchenpos.domain.exception.InvalidOrderStatusCompletionException;
import kitchenpos.domain.exception.InvalidTableGroupException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTableTest {

    @Test
    void 생성자는_유효한_데이터를_전달하면_orderTable을_초기화한다() {
        // when & then
        assertThatCode(() -> new OrderTable(0, false)).doesNotThrowAnyException();
    }

    @Test
    void 생성자는_손님_수로_음수를_전달하면_예외가_발생한다() {
        // given
        final int invalidNumberOfGuests = -1;

        // when & then
        assertThatThrownBy(() -> new OrderTable(invalidNumberOfGuests, false))
                .isInstanceOf(InvalidNumberOfGuestsException.class);
    }

    @Test
    void initOrder_메서드는_호출하면_전달한_Order로_필드를_초기화한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderStatus orderStatus = OrderStatus.COMPLETION;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final OrderTable orderTable = new OrderTable(0, false);
        final Order order = new Order(orderTable, orderStatus, LocalDateTime.now(), List.of(orderLineItem));

        // when
        orderTable.initOrder(order);

        // then
        assertThat(orderTable.getOrder()).isEqualTo(order);
    }

    @Test
    void changeEmptyStatus_메서드는_주어진_order의_상태가_COMPLETION이라면_전달한_값으로_변경한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderStatus orderStatus = OrderStatus.COMPLETION;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final OrderTable orderTable = new OrderTable(0, false);
        final Order order = new Order(orderTable, orderStatus, LocalDateTime.now(), List.of(orderLineItem));

        // when
        orderTable.changeEmptyStatus(List.of(order), true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @ParameterizedTest(name = "Order의 OrderStatus가 {0}일 때 예외가 발생한다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmptyStatus_메서드는_주어진_order의_상태가_COMPLETION이_아니라면_예외가_발생한다(final String orderStatusName) {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusName);
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final OrderTable orderTable = new OrderTable(0, false);
        final Order order = new Order(orderTable, orderStatus, LocalDateTime.now(), List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderTable.changeEmptyStatus(List.of(order), true))
                .isInstanceOf(InvalidOrderStatusCompletionException.class);
    }

    @Test
    void changeEmptyStatus_메서드는_orderTable이_tableGroup을_가지고_있다면_예외가_발생한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderStatus orderStatus = OrderStatus.COMPLETION;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final OrderTable orderTable1 = new OrderTable(0, true);
        final OrderTable orderTable2 = new OrderTable(0, true);
        final Order order = new Order(orderTable1, orderStatus, LocalDateTime.now(), List.of(orderLineItem));
        new TableGroup(List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> orderTable1.changeEmptyStatus(List.of(order), true))
                .isInstanceOf(InvalidTableGroupException.class);
    }

    @Test
    void changeNumberOfGuests_메서드는_유효한_손님_수를_전달하면_numberOfGuests를_변경한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);

        // when
        orderTable.changeNumberOfGuests(1);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    void changeNumberOfGuests_메서드는_orderTable이_비어_있으면_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                .isInstanceOf(InvalidEmptyOrderTableException.class);
    }
}
