package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MenuProductTest {

    @DisplayName("상품의 금액을 계산할 수 있다.")
    @CsvSource(value={"1000,10,10000", "10000,10,100000"}, delimiter = ',')
    @ParameterizedTest
    void calculatePrice(int productPrice, int productCount, int expect) {
        Product product = new Product("라면", BigDecimal.valueOf(productPrice));
        MenuProduct menuProduct = new MenuProduct(null, product, productCount);

        assertThat(menuProduct.calculatePrice()).isEqualTo(BigDecimal.valueOf(expect));
    }
}
