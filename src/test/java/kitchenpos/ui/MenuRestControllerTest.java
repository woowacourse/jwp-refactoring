package kitchenpos.ui;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuRestControllerTest extends ControllerTest {

    private MenuGroup chickenCombo;
    private Product friedChicken;
    private List<MenuProduct> products;

    @BeforeEach
    void setUp() throws Exception {
        // given
        chickenCombo = menuGroup("Chicken Combo");
        friedChicken = product("Fried Chicken", 14000L);
        MenuProduct friedChickenProduct = menuProduct(friedChicken, 1);
        MenuProduct cokeProduct = menuProduct(product("Coke", 1000L), 1);
        products = Arrays.asList(friedChickenProduct, cokeProduct);
    }

    @Test
    void create() throws Exception {
        // when
        Menu friedChickenCombo = menu("Fried Chicken Combo", 14500L, chickenCombo, products);
        List<MenuProduct> products = friedChickenCombo.getMenuProducts();

        // then
        assertAll(
                () -> assertThat(friedChickenCombo.getMenuGroupId()).isEqualTo(chickenCombo.getId()),
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products.get(0).getSeq()).isEqualTo(1L),
                () -> assertThat(products.get(0).getProductId()).isEqualTo(friedChicken.getId()),
                () -> assertThat(products.get(0).getMenuId()).isEqualTo(friedChickenCombo.getId())
        );
    }

    @Test
    void create_NegativePrice() {
        // when
        assertThatThrownBy(() -> menu("Fried Chicken Combo", -14500L, chickenCombo, products))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_PriceHigherThanSumOfSingleProducts() {
        // when
        assertThatThrownBy(() -> menu("Fried Chicken Combo", 15500L, chickenCombo, products))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() throws Exception {
        // when
        Menu friedChickenCombo = menu("Fried Chicken Combo", 14500L, chickenCombo, products);
        List<Menu> menus = menus();

        // then
        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menus.get(0)).usingRecursiveComparison().isEqualTo(friedChickenCombo)
        );

    }
}