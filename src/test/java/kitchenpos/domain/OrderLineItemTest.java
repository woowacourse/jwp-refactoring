package kitchenpos.domain;

import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderLineItemTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        // given
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));
        final Menu menu = new Menu(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup, MenuProducts.empty());

        final OrderTable orderTable = new OrderTable(null, 10, true);
        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), new OrderLineItems(new ArrayList<>()));

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
        assertThatCode(() -> OrderLineItem.ofWithoutOrder(menu, new Quantity(10)))
                .doesNotThrowAnyException();
    }

    @DisplayName("[SUCCESS] 주문을 할당한다.")
    @Test
    void success_assignOrder() {
        // given
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));
        final Menu menu = new Menu(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup, MenuProducts.empty());

        // when
        final OrderLineItem orderLineItem = OrderLineItem.ofWithoutOrder(menu, new Quantity(10));
        final Order order = Order.ofEmptyOrderLineItems(new OrderTable(null, 10, true));
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
