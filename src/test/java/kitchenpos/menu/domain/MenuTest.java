package kitchenpos.menu.domain;

import static kitchenpos.TestObjectFactory.createMenu;
import static kitchenpos.TestObjectFactory.createMenuProduct;
import static kitchenpos.TestObjectFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴 생성 시 메뉴 그룹 등록 여부 확인")
    @Test
    void setMenuGroup() {
        MenuGroup menuGroup = MenuGroup.builder().build();
        Product product = createProduct(10_000);
        MenuProduct menuProduct = createMenuProduct(product, 2);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct);

        Menu menu = Menu.builder()
            .price(BigDecimal.valueOf(18_000))
            .menuGroup(menuGroup)
            .menuProducts(menuProducts)
            .build();

        assertThat(menu.getMenuGroup()).isEqualTo(menuGroup);
    }

    @DisplayName("메뉴 생성 시 메뉴상품 등록 여부 확인")
    @Test
    void setMenuProducts() {
        Product product1 = createProduct(15_000);
        Product product2 = createProduct(16_000);
        MenuProduct menuProduct1 = createMenuProduct(product1, 1);
        MenuProduct menuProduct2 = createMenuProduct(product2, 1);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);

        Menu menu = createMenu(menuProducts, 10);

        assertAll(
            () -> assertThat(menuProduct1.getMenuId()).isEqualTo(menu.getId()),
            () -> assertThat(menuProduct2.getMenuId()).isEqualTo(menu.getId())
        );
    }

    @DisplayName("[예외] 가격이 0보다 작은 메뉴 생성")
    @Test
    void create_Fail_With_InvalidPrice() {
        assertThatThrownBy(
            () -> Menu.builder()
                .price(BigDecimal.valueOf(-1))
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 메뉴상품 가격의 총합보다 가격이 높은 메뉴 생성")
    @Test
    void create_Fail_With_MenuProductsPriceOver() {
        Product product1 = createProduct(15_000);
        Product product2 = createProduct(16_000);
        MenuProduct menuProduct1 = createMenuProduct(product1, 1);
        MenuProduct menuProduct2 = createMenuProduct(product2, 1);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);

        assertThatThrownBy(
            () -> Menu.builder()
                .price(BigDecimal.valueOf(100_000_000))
                .menuProducts(menuProducts)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }
}