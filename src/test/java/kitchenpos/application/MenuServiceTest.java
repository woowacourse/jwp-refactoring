package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;

    @Test
    void create_menu_success() {
        // given
        final Menu savedChicken = generateMenu("chicken");
        final Product chickenProduct = generateProduct("chicken", 10000L);
        final MenuProduct menuProductA = new MenuProduct();
        menuProductA.setMenuId(savedChicken.getMenuGroupId());
        menuProductA.setProductId(chickenProduct.getId());
        menuProductA.setQuantity(2);
        menuProductDao.save(menuProductA);

        final Product sauceProduct = generateProduct("sauce", 3000L);
        final MenuProduct menuProductB = new MenuProduct();
        menuProductB.setMenuId(savedChicken.getMenuGroupId());
        menuProductB.setProductId(sauceProduct.getId());
        menuProductB.setQuantity(5);
        menuProductDao.save(menuProductB);

        final Menu requestMenu = new Menu();
        requestMenu.setName("seasoningChicken");
        requestMenu.setMenuProducts(List.of(menuProductA, menuProductB));
        requestMenu.setPrice(BigDecimal.valueOf(35000));
        requestMenu.setMenuGroupId(savedChicken.getMenuGroupId());

        // when
        final Menu createdMenu = menuService.create(requestMenu);

        // then
        assertThat(createdMenu.getId()).isNotNull();
        assertThat(createdMenu.getMenuProducts()).hasSize(2);
    }

    @Nested
    class create_menu_failure {

        @Test
        void price_is_zero() {
            // given
            final Menu menu = new Menu();
            menu.setPrice(BigDecimal.ZERO);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void without_price() {
            // given
            final Menu menu = new Menu();
            menu.setPrice(null);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void menu_group_is_not_exist() {
            // given
            final Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(10000L));
            menu.setName("chicken");

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void menu_product_is_not_exist() {
            // given
            final Menu savedChicken = generateMenu("chicken");
            final Product savedProduct = generateProduct("sauce", 3000L);
            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setMenuId(savedChicken.getMenuGroupId());
            menuProduct.setProductId(savedProduct.getId());
            menuProduct.setQuantity(3);
            final MenuProduct notMenuProduct = new MenuProduct();

            final Menu requestMenu = new Menu();
            requestMenu.setName("chicken");
            requestMenu.setMenuProducts(List.of(menuProduct, notMenuProduct));
            requestMenu.setPrice(BigDecimal.valueOf(10000L));
            requestMenu.setMenuGroupId(savedChicken.getMenuGroupId());

            // when & then
            assertThatThrownBy(() -> menuService.create(requestMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void menu_price_is_not_equal_total_sum_of_menu_products() {
            // given
            final Menu savedChicken = generateMenu("chicken");
            final Product chickenProduct = generateProduct("chicken", 10000L);
            final MenuProduct menuProductA = new MenuProduct();
            menuProductA.setMenuId(savedChicken.getMenuGroupId());
            menuProductA.setProductId(chickenProduct.getId());
            menuProductA.setQuantity(2);

            final Product sauceProduct = generateProduct("sauce", 3000L);
            final MenuProduct menuProductB = new MenuProduct();
            menuProductB.setMenuId(savedChicken.getMenuGroupId());
            menuProductB.setProductId(sauceProduct.getId());
            menuProductB.setQuantity(5);

            final Menu requestMenu = new Menu();
            requestMenu.setName("chicken");
            requestMenu.setMenuProducts(List.of(menuProductA, menuProductB));
            requestMenu.setPrice(BigDecimal.valueOf(35000L + 1L));
            requestMenu.setMenuGroupId(savedChicken.getMenuGroupId());

            // when & then
            assertThatThrownBy(() -> menuService.create(requestMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void list() {
        // given
        final Menu savedChicken = generateMenu("chicken");
        final Product chickenProduct = generateProduct("chicken", 10000L);
        final MenuProduct menuProductA = new MenuProduct();
        menuProductA.setMenuId(savedChicken.getMenuGroupId());
        menuProductA.setProductId(chickenProduct.getId());
        menuProductA.setQuantity(2);
        menuProductDao.save(menuProductA);

        final Product sauceProduct = generateProduct("sauce", 3000L);
        final MenuProduct menuProductB = new MenuProduct();
        menuProductB.setMenuId(savedChicken.getMenuGroupId());
        menuProductB.setProductId(sauceProduct.getId());
        menuProductB.setQuantity(5);
        menuProductDao.save(menuProductB);

        final Menu requestMenu = new Menu();
        requestMenu.setName("seasoningChicken");
        requestMenu.setMenuProducts(List.of(menuProductA, menuProductB));
        requestMenu.setPrice(BigDecimal.valueOf(35000));
        requestMenu.setMenuGroupId(savedChicken.getMenuGroupId());

        // when
        final Menu createdMenu = menuService.create(requestMenu);

        // then
        assertSoftly(softly -> {
            softly.assertThat(createdMenu.getId()).isNotNull();
            softly.assertThat(createdMenu.getMenuProducts()).hasSize(2);
        });
    }
}
