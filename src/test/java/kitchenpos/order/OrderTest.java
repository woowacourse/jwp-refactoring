package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.exception.InvalidOrderException;
import kitchenpos.common.exception.InvalidOrderStatusException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Nested
    class 주문_생성시 {

        @Test
        void 테이블이_비어있다면_예외가_발생한다() {
            // given
            final var orderTable = new OrderTable(0, true);
            final var product = new Product("후라이드", BigDecimal.valueOf(1000));
            final var menuGroup = new MenuGroup("메뉴 그룹");
            final var menu = new Menu("메뉴", BigDecimal.valueOf(1000), List.of(new MenuProduct(product, 1L)), menuGroup);
            final var orderLineItems = List.of(new OrderLineItem(menu.getId(), 1L, menu.getName(), menu.getPrice()));

            // when
            final ThrowingCallable action = () -> orderTable.order(orderLineItems, LocalDateTime.now());

            // then
            assertThatThrownBy(action).isInstanceOf(InvalidOrderException.class);
        }

        @Test
        void 주문_상품이_비어있다면_예외가_발생한다() {
            // given
            final var orderTable = new OrderTable(1, false);
            final List<OrderLineItem> orderLineItems = Collections.emptyList();

            // when
            final ThrowingCallable action = () -> new Order(orderLineItems, LocalDateTime.now(), orderTable.getId());

            // then
            assertThatThrownBy(action).isInstanceOf(InvalidOrderException.class);
        }

        @Test
        void 첫_생성_후_주문_상태는_조리중이다() {
            // given
            final var orderTable = new OrderTable(1, false);
            final var product = new Product("후라이드", BigDecimal.valueOf(1000));
            final var menuGroup = new MenuGroup("메뉴 그룹");
            final var menu = new Menu("메뉴", BigDecimal.valueOf(1000), List.of(new MenuProduct(product, 1L)), menuGroup);
            final var orderLineItems = List.of(new OrderLineItem(menu.getId(), 1L, menu.getName(), menu.getPrice()));

            // when
            final var order = new Order(orderLineItems, LocalDateTime.now(), orderTable.getId());

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        }
    }

    @Nested
    class 주문_상태_변경시 {

        @ParameterizedTest
        @CsvSource(value = {"COOKING,MEAL", "MEAL,COMPLETION"})
        void 주문_상태를_조리중_식사중_완료_순으로_변경할_수_있다(OrderStatus before, OrderStatus after) {
            // given
            final var orderTable = new OrderTable(1, false);
            final var product = new Product("후라이드", BigDecimal.valueOf(1000));
            final var menuGroup = new MenuGroup("메뉴 그룹");
            final var menu = new Menu("메뉴", BigDecimal.valueOf(1000), List.of(new MenuProduct(product, 1L)), menuGroup);
            final var orderLineItems = List.of(new OrderLineItem(menu.getId(), 1L, menu.getName(), menu.getPrice()));
            final var order = new Order(null, before, orderLineItems, LocalDateTime.now(), orderTable.getId());

            // when
            final Executable action = () -> order.changeStatusTo(after);

            // then
            assertDoesNotThrow(action);
        }

        @ParameterizedTest
        @CsvSource(value = {"COOKING,COMPLETION", "MEAL,COOKING", "MEAL,MEAL"})
        void 주문_상태를_잘못된_순서로_변경할_수_없다(OrderStatus before, OrderStatus after) {
            // given
            final var orderTable = new OrderTable(1, false);
            final var product = new Product("후라이드", BigDecimal.valueOf(1000));
            final var menuGroup = new MenuGroup("메뉴 그룹");
            final var menu = new Menu("메뉴", BigDecimal.valueOf(1000), List.of(new MenuProduct(product, 1L)), menuGroup);
            final var orderLineItems = List.of(new OrderLineItem(menu.getId(), 1L, menu.getName(), menu.getPrice()));
            final var order = new Order(null, before, orderLineItems, LocalDateTime.now(), orderTable.getId());

            // when
            final ThrowingCallable action = () -> order.changeStatusTo(after);

            // then
            assertThatThrownBy(action).isInstanceOf(InvalidOrderStatusException.class);
        }
    }
}

