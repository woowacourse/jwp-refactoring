package kitchenpos.domain.menuproduct;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.MenuFixture.createMenuProduct;
import static kitchenpos.ProductFixture.createProduct1;
import static kitchenpos.ProductFixture.createProduct2;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MenuProductsTest {

    @DisplayName("메뉴 가격이 메뉴 상품들 가격의 총합보다 클 경우 메뉴 상품들을 생성할 수 없다.")
    @Test
    void createExceptionIfPrice() {
        List<MenuProduct> menuProducts = Arrays.asList(createMenuProduct(createProduct1()), createMenuProduct(createProduct2()));

        assertThatThrownBy(() -> MenuProducts.of(BigDecimal.valueOf(100000), menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품들을 생성한다.")
    @Test
    void create() {
        List<MenuProduct> menuProducts = Arrays.asList(createMenuProduct(createProduct1()), createMenuProduct(createProduct2()));

        assertDoesNotThrow(() -> MenuProducts.of(BigDecimal.valueOf(100), menuProducts));
    }
}