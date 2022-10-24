package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.domain.Product;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    private ProductService productService;

    @Autowired
    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @Test
    void createProduct() {
        // given
        ProductRequest product = new ProductRequest("상품", BigDecimal.valueOf(1000));

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct).isNotNull();
    }

    @Test
    void createProductWithNullPrice() {
        // given
        ProductRequest product = new ProductRequest("상품", null);

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createProductWithZeroPrice() {
        // given
        ProductRequest product = new ProductRequest("상품", BigDecimal.valueOf(0));

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct).isNotNull();
    }

    @Test
    void createProductWithNegativePrice() {
        // given
        ProductRequest product = new ProductRequest("상품", BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findProducts() {
        // given & when
        List<Product> products = productService.list();

        // then
        int defaultSize = 6;
        assertThat(products).hasSize(defaultSize);
    }
}
