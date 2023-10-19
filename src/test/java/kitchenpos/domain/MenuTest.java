package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.exception.MenuPriceExpensiveThanProductsPriceException;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_10개_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    @Test
    void 메뉴_상품을_등록할때_메뉴_가격이_더_크면_예외를_발생한다() {
        // given
        MenuGroup menuGroup = 메뉴_그룹_생성();
        Menu menu = 메뉴_생성("메뉴", 1000000L, menuGroup);
        List<MenuProduct> menuProducts = List.of(메뉴_상품_10개_생성(상품_생성_10000원()));

        // when & then
        assertThatThrownBy(() -> menu.initMenuProducts(menuProducts))
                .isInstanceOf(MenuPriceExpensiveThanProductsPriceException.class);
    }
}
