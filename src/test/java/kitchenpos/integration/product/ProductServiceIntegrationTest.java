package kitchenpos.integration.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.integration.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceIntegrationTest extends IntegrationTest {

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create_Valid_Success() {
        // given
        Product product = new Product("얌 프라이", BigDecimal.valueOf(8000, 2));

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(product);
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다. - null인 경우")
    @Test
    void create_InvalidPriceWithNull_Fail() {
        // given
        Product product = new Product("얌 프라이");

        // when
        // then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다. - 0 미만인 경우")
    @Test
    void create_InvalidPriceWithNegative_Fail() {
        // given
        Product product = new Product("얌 프라이", BigDecimal.valueOf(-1000));

        // when
        // then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void list_Valid_Success() {
        // given
        Product product = new Product("얌 프라이", BigDecimal.valueOf(8000, 2));

        Product savedProduct = productService.create(product);

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).hasSize(7);
        assertThat(products.get(6))
            .usingRecursiveComparison()
            .isEqualTo(savedProduct);
    }
}
