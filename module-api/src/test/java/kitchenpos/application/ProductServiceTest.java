package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@DisplayName("ProductService 테스트")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("Product 생성 테스트")
    @Test
    void create() {
        // given
        final ProductRequest productRequest = new ProductRequest("된장찌개", 6000);

        // when
        final ProductResponse productResponse = productService.create(productRequest);

        // then
        final List<Product> foundAllProducts = productRepository.findAll();
        assertThat(foundAllProducts).hasSize(1);
        final Product foundProduct = foundAllProducts.get(0);

        assertThat(productResponse.getId())
            .isNotNull()
            .isEqualTo(foundProduct.getId());

        assertThat(productResponse.getName())
            .isEqualTo(productRequest.getName())
            .isEqualTo(foundProduct.getName());

        assertThat(productResponse.getPrice())
            .isEqualTo(productResponse.getPrice())
            .isEqualTo(foundProduct.getPrice().getValueAsInt());
    }
}
