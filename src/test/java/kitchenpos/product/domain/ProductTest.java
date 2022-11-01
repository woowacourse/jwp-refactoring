package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ProductTest {

    @Test
    @DisplayName("예외사항이 존재하지 않는 경우 객체를 생성한다.")
    void product() {
        assertDoesNotThrow(() -> new Product("name", new BigDecimal(1000)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("name이 비어있을 경우 예외가 발생한다.")
    void emptyName(String name) {
        assertThatThrownBy(() -> new Product(name, new BigDecimal(1000)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("상품의 이름은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("price가 null일 경우 예외가 발생한다.")
    void nullPrice() {
        assertThatThrownBy(() -> new Product("name", null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("상품의 가격은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("price가 0원 미만일 경우 예외가 발생한다.")
    void negativePrice() {
        assertThatThrownBy(() -> new Product("name", new BigDecimal(-1)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("상품은 0원 미만일 수 없습니다.");
    }

}
