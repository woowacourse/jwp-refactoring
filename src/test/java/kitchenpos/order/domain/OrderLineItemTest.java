package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderLineItemTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        // given
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));
        final Menu menu = Menu.withEmptyMenuProducts(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup);

        final OrderTable orderTable = OrderTable.withoutTableGroup(10, false);
        final Order order = Order.ofEmptyOrderLineItems(orderTable.getId());

        // expect
        assertThatCode(() -> new OrderLineItem(menu.getId(), new Quantity(10)))
                .doesNotThrowAnyException();
    }

    @DisplayName("[SUCCESS] 주문의 주인이 없는 상태로 주문 상품을 생성한다.")
    @Test
    void success_ofWithoutOrder() {
        // given
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));
        final Menu menu = Menu.withEmptyMenuProducts(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup);

        // expect
        assertThatCode(() -> OrderLineItem.withoutOrder(menu.getId(), new Quantity(10)))
                .doesNotThrowAnyException();
    }

    @DisplayName("[SUCCESS] 주문을 할당한다.")
    @Test
    void success_assignOrder() {
        // given
        final long menuId = 1L;
        final OrderLineItem orderLineItem = OrderLineItem.withoutOrder(menuId, new Quantity(10));

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderLineItem.getSeq()).isNull();
            softly.assertThat(orderLineItem.getMenuId()).isEqualTo(menuId);
            softly.assertThat(orderLineItem.getQuantity()).isEqualTo(new Quantity(10));
        });
    }
}
