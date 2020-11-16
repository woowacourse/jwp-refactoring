package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("Product의 Id 와 같은 Id인지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,true", "2,false"})
    void isSameId(Long id, boolean expect) {
        Product product = new Product(1L, "상품이름", 1000L);

        assertThat(product.isSameId(id)).isEqualTo(expect);
    }


    @DisplayName("상품의 가격와 양을 곱한 값을 반환한다.")
    @Test
    void calculatePrice() {
        Product product = new Product("상품이름", 123L);
        Long quantity = 2L;

        assertThat(product.calculatePrice(quantity)).isEqualTo(BigDecimal.valueOf(246L));
    }
}