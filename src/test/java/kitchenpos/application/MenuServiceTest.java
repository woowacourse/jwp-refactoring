package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.InvalidMenuTotalPriceException;
import kitchenpos.exception.NotFoundProductException;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends IntegrationTest {

    @Test
    void 메뉴를_생상할_수_있다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
        Menu menu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
        menu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));

        // when
        Menu extract = menuService.create(menu);

        // then
        assertThat(extract).isNotNull();
    }

    @Test
    void 메뉴_금액이_0원_미만인_경우_예외가_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
        Menu menu = new Menu("짜장면", BigDecimal.valueOf(-1), menuGroup.getId());
        menu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(InvalidMenuPriceException.class);
    }

    @Test
    void 메뉴_금액이_NULL일_경우_예외가_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
        Menu menu = new Menu("짜장면", null, menuGroup.getId());
        menu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(InvalidMenuPriceException.class);
    }

    @Test
    void 메뉴에_포함된_메뉴상품의_상품_번호가_존재하지_않는_번호일_경우_예외가_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Menu menu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
        Long notRegisterProductId = 100L;
        menu.addMenuProducts(List.of(new MenuProduct(1L, null, notRegisterProductId, 1)));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(NotFoundProductException.class);
    }

    @Test
    void 메뉴에_포함된_메뉴상품의_각_상품별_총_가격_합이_0원_이하일_경우_예외가_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(0)));
        Menu menu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
        menu.addMenuProducts(List.of(new MenuProduct(1L, menu.getId(), product.getId(), 1)));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(InvalidMenuTotalPriceException.class);
    }

    /**
     * 현재 프로덕션에 추가되야 하는 방어로직
     * - MenuProduct에 대한 product ID가 동일한지를 검증
     */
}