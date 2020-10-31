package kitchenpos.domain;

import kitchenpos.domain.exceptions.EmptyNameException;
import kitchenpos.domain.exceptions.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("상품 생성 실패 - 이름이 빈 문자열")
    @Test
    public void createFailNameEmpty() {
        assertThatThrownBy(() -> new Product("", BigDecimal.valueOf(10_000L)))
                .isInstanceOf(EmptyNameException.class);
    }

    @DisplayName("상품 생성 실패 - 가격이 음수")
    @Test
    public void createFailPriceMinus() {
        assertThatThrownBy(() -> new Product("파스타", BigDecimal.valueOf(-10L)))
                .isInstanceOf(InvalidPriceException.class);
    }

    @DisplayName("상품 생성")
    @Test
    public void createProduct() {
        Product product = new Product("파스타", BigDecimal.valueOf(10_000L));

        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("파스타");
        assertThat(product.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10_000L));
    }
}