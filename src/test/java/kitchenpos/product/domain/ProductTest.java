package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("상품 가격이 양수가 아니면 생성할 수 없다.")
    @Test
    void createException1() {
        assertThatThrownBy(() -> new Product("상품", -100L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 이름이 없으면 생성할 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createException2(String name) {
        assertThatThrownBy(() -> new Product(name, 10000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%s : 올바르지 않은 이름입니다.", name);
    }
}