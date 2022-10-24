package kitchenpos.application;

import static kitchenpos.support.MenuFixture.MENU_PRICE_10000;
import static kitchenpos.support.MenuGroupFixture.MENU_GROUP_1;
import static kitchenpos.support.MenuProductFixture.MENU_PRODUCT_1;
import static kitchenpos.support.ProductFixture.PRODUCT_PRICE_1000;
import static kitchenpos.support.ProductFixture.PRODUCT_PRICE_10000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Test
    void 메뉴를_생성한다() {
        // given
        final Long productId = 제품을_저장한다(PRODUCT_PRICE_10000.생성()).getId();
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Menu menu = MENU_PRICE_10000.생성(menuGroupId, List.of(MENU_PRODUCT_1.생성(productId)));

        // when
        final Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
    }

    @Test
    void 메뉴의_가격이_음수이면_예외를_발생한다() {
        // given
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Menu menu = new Menu("메뉴", new BigDecimal(-1), menuGroupId);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_null이면_예외를_발생한다() {
        // given
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Menu menu = new Menu("메뉴", null, menuGroupId);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_그룹이_존재하지_않으면_예외를_발생한다() {
        // given
        final long notExistMenuGroupId = Long.MAX_VALUE;
        final Menu menu = MENU_PRICE_10000.생성(notExistMenuGroupId);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴제품의_총가격의_합이_메뉴가격과_작으면_예외를_발생한다() {
        // given
        final Long productId = 제품을_저장한다(PRODUCT_PRICE_1000.생성()).getId();
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Menu menu = MENU_PRICE_10000.생성(menuGroupId, List.of(MENU_PRODUCT_1.생성(productId)));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_메뉴를_조회할_때_메뉴제품도_함께_조회되어야_한다() {
        // given
        final Long productId = 제품을_저장한다(PRODUCT_PRICE_10000.생성()).getId();
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Menu savedMenu = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId));
        final MenuProduct savedMenuProduct = 메뉴상품을_저장한다(MENU_PRODUCT_1.생성(savedMenu.getId(), productId));

        // when
        final List<Menu> menus = menuService.list();

        // then
        final Optional<Menu> foundMenu = menus.stream()
                .filter(menu -> menu.getId().equals(savedMenu.getId()))
                .findFirst();
        assertThat(foundMenu).isPresent();
        assertThat(foundMenu.get().getMenuProducts()).usingElementComparatorIgnoringFields("seq")
                .containsOnly(savedMenuProduct);
    }
}
