package kitchenpos.domain.order;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuFixture.메뉴_상품;
import static kitchenpos.fixture.MenuFixture.메뉴_상품들;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.exception.OrderException.NoMenuException;
import kitchenpos.exception.OrderException.NoQuantityException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderLineItemTest {

    @Test
    void 주문_상품은_수량이_최소_0보다_작거나_같다면_예외가_발생한다() {
        // given
        final MenuProducts menuProducts = 메뉴_상품들(메뉴_상품(상품("상품", BigDecimal.valueOf(10000L)), 1L));
        final MenuGroup menuGroup = 메뉴_그룹("메뉴_그룹");
        final Menu menu = 메뉴("메뉴", 1000L, menuProducts, menuGroup);

        // expected
        assertThatThrownBy(() -> new OrderLineItem(menu, 0L))
                .isInstanceOf(NoQuantityException.class);
    }

    @Test
    void 주문_상품은_수량이_없다면_예외가_발생한다() {
        // given
        final MenuProducts menuProducts = 메뉴_상품들(메뉴_상품(상품("상품", BigDecimal.valueOf(10000L)), 1L));
        final MenuGroup menuGroup = 메뉴_그룹("메뉴_그룹");
        final Menu menu = 메뉴("메뉴", 1000L, menuProducts, menuGroup);

        // expected
        assertThatThrownBy(() -> new OrderLineItem(menu, null))
                .isInstanceOf(NoQuantityException.class);
    }

    @Test
    void 주문_상품은_메뉴가_없다면_예외가_발생한다() {
        // expected
        assertThatThrownBy(() -> new OrderLineItem(null, 1L))
                .isInstanceOf(NoMenuException.class);
    }
}
