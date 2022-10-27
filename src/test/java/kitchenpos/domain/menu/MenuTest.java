package kitchenpos.domain.menu;

import static kitchenpos.fixture.Fixture.PRODUCT_양념치킨;
import static kitchenpos.fixture.Fixture.PRODUCT_후라이드;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 상품_등록시_Menu_가격이_총금액보다_비싸면_예외가_발생한다() {
        final MenuProduct menuProduct1 = new MenuProduct(PRODUCT_후라이드, 1L);
        final MenuProduct menuProduct2 = new MenuProduct(PRODUCT_양념치킨, 1L);

        assertThatThrownBy(() -> new Menu("후라이드+양념치킨", new MenuPrice(BigDecimal.valueOf(34000)), 2L,
                List.of(menuProduct1, menuProduct2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Menu 가격은 Product 가격의 합을 초과할 수 없습니다.");
    }
}
