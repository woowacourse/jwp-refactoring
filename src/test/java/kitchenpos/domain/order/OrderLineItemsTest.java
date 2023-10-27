package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class OrderLineItemsTest {

    @Test
    @DisplayName("주문 항목이 비어있으면 생성할 수 없다")
    void orderLineItems_empty() {
        // given
        final List<OrderLineItem> emptyOrderLineItem = Collections.emptyList();

        // when & then
        assertThatThrownBy(() -> new OrderLineItems(emptyOrderLineItem))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 비어있습니다.");
    }

    @Test
    @DisplayName("주문 항목에 중복된 메뉴가 있으면 예외가 발생한다")
    void orderLineItems_duplicateMenu() {
        // given
        final MenuGroup 두마리_메뉴 = new MenuGroup("두마리 메뉴");
        final Menu 후라이드_후라이드 = new Menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리_메뉴);
        final Menu 후라이드_양념치킨 = new Menu("후라이드+양념치킨", BigDecimal.valueOf(32000), 두마리_메뉴);
        final OrderLineItem 후라이드_후라이드_1개 = new OrderLineItem(후라이드_후라이드.getId(), 1);
        final OrderLineItem 후라이드_후라이드_2개 = new OrderLineItem(후라이드_후라이드.getId(), 2);
        final OrderLineItem 후라이드_양념치킨_1개 = new OrderLineItem(후라이드_양념치킨.getId(), 1);

        // when
        assertThatThrownBy(() -> new OrderLineItems(List.of(후라이드_후라이드_1개, 후라이드_후라이드_2개, 후라이드_양념치킨_1개)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목의 메뉴는 중복될 수 없습니다.");
    }
}
