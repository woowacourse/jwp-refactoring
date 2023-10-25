package kitchenpos.domain;

import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
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
        final Menu menu = new Menu(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup, MenuProducts.empty());

        final OrderTable orderTable = OrderTable.withoutTableGroup(10, false);;
        final Order order = Order.ofEmptyOrderLineItems(orderTable);

        // expect
        assertThatCode(() -> new OrderLineItem(order, menu, new Quantity(10)))
                .doesNotThrowAnyException();
    }

    @DisplayName("[SUCCESS] 주문의 주인이 없는 상태로 주문 상품을 생성한다.")
    @Test
    void success_ofWithoutOrder() {
        // given
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));
        final Menu menu = new Menu(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup, MenuProducts.empty());

        // expect
        assertThatCode(() -> OrderLineItem.withoutOrder(menu, new Quantity(10)))
                .doesNotThrowAnyException();
    }

    @DisplayName("[SUCCESS] 주문을 할당한다.")
    @Test
    void success_assignOrder() {
        // given
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));
        final Menu menu = new Menu(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup, MenuProducts.empty());

        // when
        final OrderLineItem orderLineItem = OrderLineItem.withoutOrder(menu, new Quantity(10));
        final Order order = Order.ofEmptyOrderLineItems(OrderTable.withoutTableGroup(10, false));
        orderLineItem.assignOrder(order);

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderLineItem.getSeq()).isNull();
            softly.assertThat(orderLineItem.getOrder()).isEqualTo(order);
            softly.assertThat(orderLineItem.getMenu()).isEqualTo(menu);
            softly.assertThat(orderLineItem.getQuantity()).isEqualTo(new Quantity(10));
        });
    }
}
