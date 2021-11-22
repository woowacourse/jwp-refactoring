package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.exception.CannotChangeOrderStatus;
import kitchenpos.exception.InvalidOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Order 도메인 테스트")
class OrderTest {

    @DisplayName("[실패] 빈 orderLineItmes면 Order 생성 불가")
    @Test
    void newOrder_emptyOrderLineItems_ExceptionThrown() {
        // given
        List<OrderLineItem> orderLineItems = Collections.emptyList();
        OrderTable orderTable = new OrderTable(1, false);
        // when
        // then
        assertThatThrownBy(() -> new Order(orderTable, orderLineItems))
            .isInstanceOf(InvalidOrderException.class);
    }

    @DisplayName("[실패] OrderTable이 empty 상태라면 생성 불가")
    @Test
    void newOrder_emptyOrderTable_ExceptionThrown() {
        // given
        List<OrderLineItem> orderLineItems = Collections
            .singletonList(new OrderLineItem(menu(), 1L));
        OrderTable orderTable = new OrderTable(0, true);

        // when
        // then
        assertThatThrownBy(() -> new Order(orderTable, orderLineItems))
            .isInstanceOf(InvalidOrderException.class);
    }

    @DisplayName("[성공] OrderStatus가 COMPLETION가 아니라면 OrderStatus 변경")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COMPLETION"})
    void changeOrderStatus_ValidOrderStatus_ExceptionThrown(OrderStatus orderStatus) {
        // given
        OrderTable orderTable = new OrderTable(1, false);
        List<OrderLineItem> orderLineItems = Collections
            .singletonList(new OrderLineItem(menu(), 1L));
        Order order = new Order(orderTable, orderLineItems);

        // when
        order.changeOrderStatus(orderStatus.name());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus.name());
    }

    @DisplayName("[실패] OrderStatus가 COMPLETION이라면 OrderStatus 변경 불가")
    @Test
    void changeOrderStatus_OrderStatusCompletion_ExceptionThrown() {
        // given
        OrderTable orderTable = new OrderTable(1, false);
        List<OrderLineItem> orderLineItems = Collections
            .singletonList(new OrderLineItem(menu(), 1L));
        Order order = new Order(orderTable, orderLineItems);

        order.changeOrderStatus(OrderStatus.COMPLETION.name());

        // when
        // then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
            .isInstanceOf(CannotChangeOrderStatus.class);

    }

    private Menu menu() {
        List<MenuProduct> menuProducts = Arrays.asList(
            menuProduct(BigDecimal.valueOf(10), 2L),
            menuProduct(BigDecimal.valueOf(20), 1L)
        );

        return new Menu(
            "메뉴",
            BigDecimal.ONE,
            new MenuGroup("메뉴그룹"), menuProducts
        );
    }

    private MenuProduct menuProduct(BigDecimal price, Long quantity) {
        return new MenuProduct(new Product("상품", price), quantity);
    }
}
