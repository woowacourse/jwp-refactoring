package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.vo.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.DataDependentIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends DataDependentIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @DisplayName("새 상품을 저장한다.")
    @Test
    void create_success() {
        // given
        final Product product = new Product("product", Price.from(BigDecimal.valueOf(1000L)));
        final ProductRequest productRequest = new ProductRequest("product", BigDecimal.valueOf(1000L));

        // when
        final ProductResponse savedProduct = productService.create(productRequest);

        // then
        assertThat(savedProduct).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(ProductResponse.from(product));
    }

    @DisplayName("모든 상품을 조회한다.")
    @Test
    void list() {
        // given
        final Product product1 = productRepository.save(new Product("product1", Price.from(BigDecimal.valueOf(1000L))));
        final Product product2 = productRepository.save(new Product("product2", Price.from(BigDecimal.valueOf(1000L))));

        // when
        final List<ProductResponse> foundProducts = productService.list();

        // then
        assertThat(foundProducts).usingRecursiveComparison()
            .comparingOnlyFields("id")
            .isEqualTo(List.of(ProductResponse.from(product1), ProductResponse.from(product2)));
    }
}
