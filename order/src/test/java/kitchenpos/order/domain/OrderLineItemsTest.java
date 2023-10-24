package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class OrderLineItemsTest {

    private List<Menu> menus;
    private Menu menu1;
    private Menu menu2;


    @BeforeEach

    public void setup() {
        // Mocking Menu objects
        menu1 = Mockito.mock(Menu.class);
        menu2 = Mockito.mock(Menu.class);

        Mockito.when(menu1.getId()).thenReturn(1L);
        Mockito.when(menu2.getId()).thenReturn(2L);

        menus = List.of(menu1, menu2);
    }

    @Nested
    class create {

        @Test
        void success() {
            // given
            List<OrderLineItem> validOrderLineItems = List.of(
                    new OrderLineItem(menu1.getId(), 1L),
                    new OrderLineItem(menu2.getId(), 1L)
            );

            // when
            OrderLineItems orderLineItems = OrderLineItems.of(validOrderLineItems, menus);

            // then
            assertThat(orderLineItems.getOrderLineItems())
                    .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
                    .isEqualTo(validOrderLineItems);
        }

        @Test
        void testOrderLineItemsCreationWithEmptyOrderLineItems() {
            // given
            List<OrderLineItem> empty = Collections.emptyList();

            // when
            // then
            assertThatThrownBy(() -> OrderLineItems.of(empty, menus))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("하나 이상의 상품을 주문하셔야 합니다.");
        }

        @Test
        void testOrderLineItemsCreationWithInvalidOrderLineItems() {
            // given
            Long invalidMenuId = 3L;

            List<OrderLineItem> invalidOrderLineItems = List.of(
                    new OrderLineItem(menu1.getId(), 1L),
                    new OrderLineItem(invalidMenuId, 1L)
            );

            // when
            // then
            assertThatThrownBy(() -> OrderLineItems.of(invalidOrderLineItems, menus))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("등록되지 않은 상품은 주문할 수 없습니다.");
        }

    }

}
