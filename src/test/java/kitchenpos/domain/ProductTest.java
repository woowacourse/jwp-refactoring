package kitchenpos.domain;

import kitchenpos.exception.FieldNotValidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @DisplayName("상품을 생성한다. - 실패, name이 ")
    @ParameterizedTest(name = "{displayName} {0}인 경우")
    @NullAndEmptySource
    void createWithInvalidName(String name) {
        /// given - when
        assertThatThrownBy(() -> new Product(name, BigDecimal.TEN))
                .isInstanceOf(FieldNotValidException.class)
                .hasMessageContaining("상품명이 유효하지 않습니다.");
    }

    @DisplayName("상품을 생성한다. - 실패, 가격이 null인 경우")
    @Test
    void createWithInvalidPrice() {
        /// given - when
        assertThatThrownBy(() -> new Product("대왕치킨", null))
                .isInstanceOf(FieldNotValidException.class)
                .hasMessageContaining("상품 가격이 유효하지 않습니다.");
    }

    @DisplayName("상품을 생성한다. - 실패, 가격이 음수인 경우")
    @Test
    void createWithInvalidNegativePrice() {
        /// given - when
        assertThatThrownBy(() -> new Product("대왕치킨", BigDecimal.valueOf(-100)))
                .isInstanceOf(FieldNotValidException.class)
                .hasMessageContaining("상품 가격이 유효하지 않습니다.");
    }
}
