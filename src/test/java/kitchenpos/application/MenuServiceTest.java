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
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceTest {

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

    @Test
    void list() {
        // given
        String friedChickenName = "후라이드";
        String seasonedChickenName = "양념치킨";
        Product friedChicken = getProduct(1L, friedChickenName, BigDecimal.valueOf(16000));
        Product seasonedChicken = getProduct(2L, seasonedChickenName, BigDecimal.valueOf(16000));

        MenuProduct menuProductFriedChicken = getMenuProduct(null, null, friedChicken.getId(), 1);
        MenuProduct menuProductSeasonedChicken = getMenuProduct(null, null, seasonedChicken.getId(), 1);

        Menu menu1 = getMenu(null, friedChickenName, BigDecimal.valueOf(16000), 1L, List.of(menuProductFriedChicken));
        Menu menu2 = getMenu(null, seasonedChickenName, BigDecimal.valueOf(16000), 1L,
                List.of(menuProductSeasonedChicken));

        given(menuDao.findAll())
                .willReturn(List.of(menu1, menu2));

        // when
        List<Menu> actual = menuService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).contains(menu1, menu2)
        );
    }
}
