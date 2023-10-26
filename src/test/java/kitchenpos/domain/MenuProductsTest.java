package kitchenpos.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MenuProductsTest {

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "Product 1", BigDecimal.valueOf(10));
        product2 = new Product(2L, "Product 2", BigDecimal.valueOf(20));
    }

    @Test
    @DisplayName("상품의 전체 금액이 맞으면 예외를 던지지 않음")
    void testValidateTotalPriceValid() {
        MenuProduct menuProduct1 = new MenuProduct(1L, null, product1, 2);
        MenuProduct menuProduct2 = new MenuProduct(2L, null, product2, 3);

        List<MenuProduct> menuProductList = Arrays.asList(menuProduct1, menuProduct2);
        MenuProducts menuProducts = new MenuProducts(menuProductList);

        Assertions.assertDoesNotThrow(
                () -> menuProducts.validateTotalPrice(BigDecimal.valueOf(80)));
    }

    @Test
    @DisplayName("상품의 전체 금액이 맞지 않으면 예외를 던짐")
    void testValidateTotalPriceInvalid() {
        MenuProduct menuProduct1 = new MenuProduct(1L, null, product1, 2);
        MenuProduct menuProduct2 = new MenuProduct(2L, null, product2, 3);

        List<MenuProduct> menuProductList = Arrays.asList(menuProduct1, menuProduct2);
        MenuProducts menuProducts = new MenuProducts(menuProductList);

        assertThatThrownBy(
                () -> menuProducts.validateTotalPrice(BigDecimal.valueOf(60)));
    }
}
