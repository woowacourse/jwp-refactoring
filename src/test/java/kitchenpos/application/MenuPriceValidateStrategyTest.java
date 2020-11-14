package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;

class MenuPriceValidateStrategyTest {
    MenuPriceValidateStrategy menuPriceValidateStrategy;

    @BeforeEach
    void setUp() {
        menuPriceValidateStrategy = new DefaultMenuPriceValidateStrategy();
    }

    @DisplayName("Menu Price가 null인 경우 예외를 반환한다.")
    @Test
    void validateNullPrice() {
        assertThatThrownBy(
            () -> menuPriceValidateStrategy.validate(null, null, null))
            .isInstanceOf(InvalidMenuPriceException.class);
    }

    @DisplayName("Menu Price가 음수인 경우 예외를 반환한다.")
    @Test
    void validateNegativePrice() {
        assertThatThrownBy(
            () -> menuPriceValidateStrategy.validate(null, null, BigDecimal.valueOf(-1000)))
            .isInstanceOf(InvalidMenuPriceException.class);
    }

    @DisplayName("Menu Price가 Product price의 합보다 큰 경우 예외를 반환한다.")
    @Test
    void validateSumOfProduct() {
        Product product1000 = ProductFixture.createWithPrice(1L, 1000L);
        Product product2000 = ProductFixture.createWithPrice(2L, 2000L);
        Product product3000 = ProductFixture.createWithPrice(3L, 3000L);

        Map<Long, Product> productMap = new HashMap<>();
        productMap.put(product1000.getId(), product1000);
        productMap.put(product2000.getId(), product2000);
        productMap.put(product3000.getId(), product3000);

        MenuProductCreateRequest request1000 = MenuProductFixture.createRequest(1L, 1);
        MenuProductCreateRequest request2000 = MenuProductFixture.createRequest(2L, 1);
        MenuProductCreateRequest request3000 = MenuProductFixture.createRequest(3L, 1);

        assertThatThrownBy(() -> menuPriceValidateStrategy.validate(productMap,
            Arrays.asList(request1000, request2000, request3000), BigDecimal.valueOf(7000L)))
            .isInstanceOf(InvalidMenuPriceException.class);
    }
}
