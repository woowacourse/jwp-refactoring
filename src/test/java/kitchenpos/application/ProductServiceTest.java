package kitchenpos.application;

import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductCreateRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("제품을 생성하고 ID를 반환한다.")
    void createProduct() {
        // Given
        ProductCreateRequest request = new ProductCreateRequest("강정치킨", new BigDecimal("17000"));

        // When
        Long productId = productService.create(request);

        // Then
        assertThat(productId).isNotNull();
        final Product product = productRepository.findById(productId).get();
        assertThat(product.getName()).isEqualTo("강정치킨");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("17000"));
    }

    @Test
    @DisplayName("모든 제품을 조회한다.")
    void findAllProducts() {
        // Given
        productService.create(new ProductCreateRequest("제품1", new BigDecimal("1000")));
        productService.create(new ProductCreateRequest("제품2", new BigDecimal("2000")));

        // When
        List<ProductResponse> products = productService.findAll();

        // Then
        assertThat(products).hasSize(8);
        assertThat(products.stream().anyMatch(product -> product.getName().equals("제품1"))).isTrue();
        assertThat(products.stream().anyMatch(product -> product.getName().equals("제품2"))).isTrue();
    }
}
