package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceTest {

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() {
        // given
        String name = "후라이드";
        Product product = getProduct(1L, name, BigDecimal.valueOf(16000));

        MenuProduct menuProduct = getMenuProduct(null, null, product.getId(), 1);
        List<MenuProduct> menuProducts = List.of(menuProduct);

        Menu menu = getMenu(null, name, BigDecimal.valueOf(16000), 1L, menuProducts);
        Menu savedMenu = getMenu(1L, name, BigDecimal.valueOf(16000), 1L, menuProducts);

        given(menuDao.save(menu))
                .willReturn(savedMenu);
        given(menuGroupDao.existsById(menu.getMenuGroupId()))
                .willReturn(true);
        given(productDao.findById(product.getId()))
                .willReturn(Optional.of(product));

        // when
        Menu actual = menuService.create(menu);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(name)
        );
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        String friedChickenName = "후라이드";
        Product friedChicken = getProduct(1L, friedChickenName, BigDecimal.valueOf(16000));
        MenuProduct menuProductFriedChicken = getMenuProduct(null, null, friedChicken.getId(), 1);
        Menu friedChickenMenu = getMenu(null, friedChickenName, BigDecimal.valueOf(16000), 1L,
                List.of(menuProductFriedChicken));

        String seasonedChickenName = "양념치킨";
        Product seasonedChicken = getProduct(2L, seasonedChickenName, BigDecimal.valueOf(16000));
        MenuProduct menuProductSeasonedChicken = getMenuProduct(null, null, seasonedChicken.getId(), 1);
        Menu seasonedChickenMenu = getMenu(null, seasonedChickenName, BigDecimal.valueOf(16000), 1L,
                List.of(menuProductSeasonedChicken));

        given(menuDao.findAll())
                .willReturn(List.of(friedChickenMenu, seasonedChickenMenu));

        // when
        List<Menu> actual = menuService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).contains(friedChickenMenu, seasonedChickenMenu)
        );
    }
}
