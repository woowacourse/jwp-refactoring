package kitchenpos.application;

import fixture.MenuBuilder;
import fixture.MenuProductBuilder;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest extends ServiceTest {

    @Autowired
    MenuService menuService;

    @Test
    void 메뉴를_저장한다() {
        Menu menu = MenuBuilder.init()
                .build();

        Menu created = menuService.create(menu);

        assertThat(created.getId()).isNotNull();
    }

    @Test
    void 가격이_음수면_예외를_발생한다() {
        Menu menu = MenuBuilder.init()
                .price(-1L)
                .build();

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외를_발생한다() {
        Menu menu = MenuBuilder.init()
                .menuGroupId(100L)
                .build();

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_메뉴_상품을_가지고_있으면_예외를_발생한다() {
        MenuProduct notExistProduct = MenuProductBuilder.init()
                .productId(100L)
                .build();
        Menu menu = MenuBuilder.init()
                .menuProducts(List.of(notExistProduct))
                .build();

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_제품의_가격과_일치하지_않으면_예외를_빨생한다() {
        MenuProduct menuProduct = MenuProductBuilder.init()
                .productId(1L)
                .build();
        List<MenuProduct> menuProducts = List.of(menuProduct);
        Menu menu = MenuBuilder.init()
                .menuProducts(menuProducts)
                .price(10000000L)
                .build();

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_메뉴를_조회힌다() {
        List<Menu> all = menuService.list();

        assertThat(all).hasSize(6);
    }
}
