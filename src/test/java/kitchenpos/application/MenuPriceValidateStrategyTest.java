package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.validator.DefaultMenuPriceValidateStrategy;
import kitchenpos.validator.MenuPriceValidateStrategy;

class MenuPriceValidateStrategyTest {
    private MenuPriceValidateStrategy menuPriceValidateStrategy;
    private List<Product> products;
    private List<MenuProductCreateRequest> menuProducts;

    @BeforeEach
    void setUp() {
        menuPriceValidateStrategy = new DefaultMenuPriceValidateStrategy();

        products = Arrays.asList(ProductFixture.createWithPrice(1L, 1000L),
            ProductFixture.createWithPrice(2L, 2000L), ProductFixture.createWithPrice(3L, 3000L));

        menuProducts = Arrays.asList(MenuProductFixture.createRequest(1L, 1),
            MenuProductFixture.createRequest(2L, 1), MenuProductFixture.createRequest(3L, 1));
    }

    @DisplayName("Menu Price가 null인 경우 예외를 반환한다.")
    @Test
    void validateNullPrice() {
        assertThatThrownBy(
            () -> menuPriceValidateStrategy.validate(products, menuProducts, null))
            .isInstanceOf(InvalidMenuPriceException.class);
    }

    @DisplayName("Menu Price가 음수인 경우 예외를 반환한다.")
    @Test
    void validateNegativePrice() {
        assertThatThrownBy(
            () -> menuPriceValidateStrategy.validate(products, menuProducts,
                BigDecimal.valueOf(-1000)))
            .isInstanceOf(InvalidMenuPriceException.class);
    }

    @DisplayName("Menu Price가 Product price의 합보다 큰 경우 예외를 반환한다.")
    @Test
    void validateSumOfProduct() {
        assertThatThrownBy(() -> menuPriceValidateStrategy.validate(products, menuProducts,
            BigDecimal.valueOf(7000L)))
            .isInstanceOf(InvalidMenuPriceException.class);
    }
}
