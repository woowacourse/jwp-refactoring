package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록 할 수 있다.")
    @Test
    void create() {
        final Product product = new Product("pizza", BigDecimal.valueOf(1000));

        final Product savedProduct = productService.create(product);

        assertThat(savedProduct.getId()).isNotNull();
    }

    @DisplayName("가격이 null 이거나 0미만인 상품은 등록할 수 없다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1"})
    void create_Exception_Price(BigDecimal price) {
        final Product product = new Product("pizza", price);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 리스트를 가져올 수 있다.")
    @Test
    void list() {
        final List<Product> products = productService.list();

        assertThat(products).hasSize(6);
    }
}
