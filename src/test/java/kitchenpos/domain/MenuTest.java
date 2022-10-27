package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MenuTest {

    private final Long noId = null;

    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() {
        // when, then
        assertDoesNotThrow(() -> new Menu("후라이드+후라이드+후라이드", new BigDecimal(0), noId,
            List.of(new MenuProduct(noId, 3))));
    }

    @Test
    @DisplayName("가격이 음수이면 예외를 던진다.")
    void create_price_negative() {
        // when, then
        assertThatThrownBy(() -> new Menu("후라이드+후라이드+후라이드", new BigDecimal(-1), noId,
            List.of(new MenuProduct(noId, 3))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {50000, 49999})
    @DisplayName("가격은 상품가격의 합보다 작거나 같아야 한다.")
    void validatePriceUnderProductsSum(int priceValue) {
        // given
        Menu menu = createMenu(priceValue);

        List<Product> products = List.of(new Product(1L, "후라이드", BigDecimal.valueOf(10000)),
            new Product(2L, "양념", BigDecimal.valueOf(10000)));

        // when, then
        Assertions.assertDoesNotThrow(() -> menu.validatePriceUnderProductsSum(products));
    }

    @Test
    @DisplayName("가격이 상품 가격의 합보다 크면 예외를 던진다.")
    void validatePriceUnderProductsSum_exception() {
        // given
        int priceValue = 50001;
        Menu menu = createMenu(priceValue);

        List<Product> products = List.of(new Product(1L, "후라이드", BigDecimal.valueOf(10000)),
            new Product(2L, "양념", BigDecimal.valueOf(10000)));

        // when, then
        assertThatThrownBy(() -> menu.validatePriceUnderProductsSum(products))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("price must be equal to or less than the sum of product prices");
    }

    @Test
    @DisplayName("상품 가격의 합을 검증할 때 MenuProduct 의 Id가 Product 의 Id와 일치하지 않으면 예외를 던진다.")
    void validateMenuProductMatchProduct() {
        // given
        int priceValue = 50000;
        Menu menu = createMenu(priceValue);

        List<Product> products = List.of(new Product(2L, "후라이드", BigDecimal.valueOf(10000)),
            new Product(1L, "양념", BigDecimal.valueOf(10000)));

        // when, then
        assertThatThrownBy(() -> menu.validatePriceUnderProductsSum(products))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("menu product not match product");
    }

    private Menu createMenu(final int priceValue) {
        return new Menu("후라이드+양념", new BigDecimal(priceValue), noId,
            List.of(new MenuProduct(1L, 2), new MenuProduct(2L, 3)));
    }
}
