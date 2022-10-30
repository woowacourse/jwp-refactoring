package kitchenpos.application;

import static kitchenpos.support.MenuFixture.MENU_PRICE_10000;
import static kitchenpos.support.MenuGroupFixture.MENU_GROUP_1;
import static kitchenpos.support.MenuProductFixture.MENU_PRODUCT_1;
import static kitchenpos.support.ProductFixture.PRODUCT_PRICE_1000;
import static kitchenpos.support.ProductFixture.PRODUCT_PRICE_10000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuProductResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.MenusResponse;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Test
    void 메뉴를_생성한다() {
        // given
        final Long productId = 제품을_저장한다(PRODUCT_PRICE_10000.생성()).getId();
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal(10_000), menuGroupId,
                List.of(new MenuProductRequest(productId, 1)));

        // when
        final MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        assertThat(menuResponse.getId()).isEqualTo(1L);
    }

    @Test
    void 메뉴의_가격이_음수이면_예외를_발생한다() {
        // given
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal(-1), menuGroupId, List.of());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_null이면_예외를_발생한다() {
        // given
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final MenuRequest menuRequest = new MenuRequest("메뉴", null, menuGroupId, List.of());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_그룹이_존재하지_않으면_예외를_발생한다() {
        // given
        final long notExistMenuGroupId = Long.MAX_VALUE;
        final MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal(10_000), notExistMenuGroupId, List.of());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴제품의_총가격의_합이_메뉴가격_보다_작으면_예외를_발생한다() {
        // given
        final Long productId = 제품을_저장한다(PRODUCT_PRICE_1000.생성()).getId();
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();

        final List<MenuProductRequest> menuProductRequests = List.of(new MenuProductRequest(productId, 1));
        final MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal(10_000), menuGroupId, menuProductRequests);

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_메뉴를_조회할_때_메뉴제품도_함께_조회되어야_한다() {
        // given
        final Product product = 제품을_저장한다(PRODUCT_PRICE_10000.생성());
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final MenuProduct menuProduct = MENU_PRODUCT_1.생성(product);
        final Menu savedMenu = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId, List.of(menuProduct)));

        final MenuResponse expectedMenuResponse = MenuResponse.from(savedMenu);
        final MenuProductResponse expectedMenuProductResponse = MenuProductResponse.from(menuProduct);

        // when
        final MenusResponse menusResponse = menuService.list();

        // then
        assertAll(
                () ->  assertThat(menusResponse.getMenus()).usingRecursiveFieldByFieldElementComparator()
                        .usingElementComparatorIgnoringFields("menuProductResponses")
                        .usingComparatorForType(Comparator.comparingInt(BigDecimal::intValue), BigDecimal.class)
                        .containsOnly(expectedMenuResponse),
                () -> assertThat(menusResponse.getMenus().get(0).getMenuProductResponses())
                        .usingRecursiveFieldByFieldElementComparator()
                        .usingComparatorForType(Comparator.comparingInt(BigDecimal::intValue), BigDecimal.class)
                        .containsOnly(expectedMenuProductResponse)
        );
    }
}
