package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.getProductRequest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductIntegrationTest extends IntegrationTest{

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성한다() {
        final Product product = productService.create(getProductRequest());
        assertThat(product.getId()).isNotNull();
    }

    @Test
    void 상품목록을_조회한다() {
        final List<Product> products = productService.list();
        assertThat(products).hasSize(6);
    }
}
