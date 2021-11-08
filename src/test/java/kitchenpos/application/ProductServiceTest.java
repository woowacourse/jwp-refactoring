package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.createProduct;
import static kitchenpos.fixture.ProductFixture.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("classpath:db/test/truncate.sql")
@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @DisplayName("상품 생성")
    @Nested
    class CreateProduct {

        @DisplayName("상품을 생성한다.")
        @Test
        void create() {
            ProductRequest productRequest = createProductRequest();
            ProductResponse savedProduct = productService.create(productRequest);
            assertAll(
                    () -> assertThat(savedProduct).isNotNull(),
                    () -> assertThat(savedProduct.getId()).isNotNull(),
                    () -> assertThat(savedProduct.getName()).isEqualTo(productRequest.getName())
            );
        }

        @DisplayName("상품의 가격은 음수일 수 없다.")
        @Test
        void createWithInvalidPrice() {
            ProductRequest productRequest = createProductRequest(BigDecimal.valueOf(-1L));
            assertThatThrownBy(() -> productService.create(productRequest)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상품 리스트를 반환한다.")
    @Test
    void list() {
        List<Product> savedProducts = Arrays.asList(createProduct(1L), createProduct(2L));
        productRepository.saveAll(savedProducts);
        List<ProductResponse> list = productService.list();
        assertAll(
                () -> assertThat(list).hasSize(savedProducts.size()),
                () -> assertThat(list).usingRecursiveComparison().isEqualTo(ProductResponse.listOf(savedProducts))
        );
    }
}
