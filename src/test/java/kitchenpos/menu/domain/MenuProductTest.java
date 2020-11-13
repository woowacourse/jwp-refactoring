package kitchenpos.menu.domain;

import static kitchenpos.TestObjectFactory.createMenuProduct;
import static kitchenpos.TestObjectFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

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

    @DisplayName("[예외] Menu.addMenuProduct를 통하지 않은 setMenu 접근")
    @Test
    void setMenu_Fail_NotThrough_Menu() {
        Menu menu = new Menu();
        MenuProduct menuProduct = new MenuProduct();

        assertThatThrownBy(
            () -> menuProduct.setMenu(menu)
        ).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("상품 개수에 따른 가격 계산")
    @Test
    void calculatePrice() {
        Product product = createProduct(15_000);
        MenuProduct menuProduct = createMenuProduct(product, 2);

        BigDecimal actual = menuProduct.calculatePrice();
        BigDecimal expected = BigDecimal.valueOf(30_000);
        assertThat(actual).isEqualTo(expected);
    }
}