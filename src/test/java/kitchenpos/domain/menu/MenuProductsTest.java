package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    private MenuProduct menuProduct;
    private MenuProduct menuProduct2;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup("추천메뉴");
        Menu halfHalf = new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup);
        Product chicken = new Product("강정치킨", BigDecimal.valueOf(17000));
        menuProduct = new MenuProduct(halfHalf, chicken, 2);
        MenuGroup menuGroup2 = new MenuGroup("최고메뉴");
        Menu halfHalf2 = new Menu("양념+후라이드", BigDecimal.valueOf(19000), menuGroup2);
        Product chicken2 = new Product("간장치킨", BigDecimal.valueOf(17000));
        menuProduct2 = new MenuProduct(halfHalf2, chicken2, 2);
    }

    @DisplayName("금액 합산 검증 성공")
    @Test
    void validate() {
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(menuProduct, menuProduct2));
        assertThatCode(() -> menuProducts.validate(BigDecimal.valueOf(68000)))
                .doesNotThrowAnyException();
    }

    @DisplayName("금액 합산 검증 실패")
    @Test
    void validateFail() {
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(menuProduct, menuProduct2));
        assertThatThrownBy(() -> menuProducts.validate(BigDecimal.valueOf(69000)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
