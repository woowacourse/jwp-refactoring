package kitchenpos.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProductTest {

    @DisplayName("상품 생성 시, 이름이 비어있으면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "  "})
    void product_FailWithBlankName(String blankName) {
        // when & then
        assertThatThrownBy(() -> Product.create(blankName, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품명이 비어있습니다.");
    }

    @DisplayName("상품 생성 시, 가격이 비어있으면 예외가 발생한다.")
    @Test
    void product_FailWithNullPrice() {
        // when & then
        assertThatThrownBy(() -> Product.create("상품명", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 가격이 비어있습니다.");
    }

    @DisplayName("상품 생성 시, 가격이 0원 미이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -100L})
    void product_FailWithBlankName(Long invalidPrice) {
        // when & then
        assertThatThrownBy(() -> Product.create("상품명", BigDecimal.valueOf(invalidPrice)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void product() {
        // then
        assertDoesNotThrow(() -> Product.create("상품명", BigDecimal.ZERO));
    }
}
