package kitchenpos.domain;

import static kitchenpos.TestObjectFactory.createMenu;
import static kitchenpos.TestObjectFactory.createMenuProduct;
import static kitchenpos.TestObjectFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @DisplayName("메뉴상품 생성 시 메뉴 등록 여부 확인")
    @Test
    void setMenu() {
        Menu menu = Menu.builder()
            .price(BigDecimal.valueOf(18_000))
            .build();

        MenuProduct menuProduct = MenuProduct.builder()
            .menu(menu)
            .build();

        assertAll(
            () -> assertThat(menuProduct.getMenu()).isEqualTo(menu),
            () -> assertThat(menuProduct.getMenu().getMenuProducts()).containsOnly(menuProduct)
        );
    }

    @DisplayName("메뉴상품 생성 시 상품 등록 여부 확인")
    @Test
    void setProduct() {
        Product product = Product.builder()
            .price(BigDecimal.valueOf(18_000))
            .build();

        MenuProduct menuProduct = MenuProduct.builder()
            .product(product)
            .build();

        assertAll(
            () -> assertThat(menuProduct.getProduct()).isEqualTo(product),
            () -> assertThat(menuProduct.getProduct().getMenuProducts()).containsOnly(menuProduct)
        );
    }

    @DisplayName("상품 개수에 따른 가격 계산")
    @Test
    void calculatePrice() {
        Product product = createProduct(15_000);
        Menu menu = createMenu(10);
        MenuProduct menuProduct = createMenuProduct(menu, product, 2);

        BigDecimal actual = menuProduct.calculatePrice();
        BigDecimal expected = BigDecimal.valueOf(30_000);
        assertThat(actual).isEqualTo(expected);
    }
}