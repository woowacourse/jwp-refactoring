package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemsTest {
    @DisplayName("잘못된 주문 항목으로 생성시 예외 반환")
    @Test
    void ofTest() {
        assertThatThrownBy(() -> {
            OrderLineItems.of(Collections.emptyList());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목에 포함된 메뉴 아이디 반환")
    @Test
    void getMenuIdsTest() {
        Menu firstMenu = new Menu(1L);
        Menu secondMenu = new Menu(2L);
        Menu thirdMenu = new Menu(3L);
        OrderLineItem firstOrderLineItem = new OrderLineItem(firstMenu, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(secondMenu, 1L);
        OrderLineItem thirdOrderLineItem = new OrderLineItem(thirdMenu, 1L);

        OrderLineItems orderLineItems = OrderLineItems.of(
                Arrays.asList(firstOrderLineItem, secondOrderLineItem, thirdOrderLineItem)
        );
        List<Long> menuIds = orderLineItems.getMenuIds();

        assertThat(menuIds).containsExactly(1L, 2L, 3L);
    }

    @DisplayName("입력한 주문 항목 수와 생성된 일급 컬렉션의 크키가 동일한지 판단")
    @Test
    void validateOrderLineItemSizeTest() {
        Menu firstMenu = new Menu(1L);
        Menu secondMenu = new Menu(2L);
        OrderLineItem firstOrderLineItem = new OrderLineItem(firstMenu, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(secondMenu, 1L);

        OrderLineItems orderLineItems = OrderLineItems.of(
                Arrays.asList(firstOrderLineItem, secondOrderLineItem)
        );
        int input = 2;

        assertThat(orderLineItems.isSameMenuCount(input)).isFalse();
    }
}