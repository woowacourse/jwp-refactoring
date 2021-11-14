package kitchenpos.menu.application;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.fixture.ProductFixture.createProduct;
import static kitchenpos.menu.fixture.ProductFixture.createProductRequest;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
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
            ProductRequest request = createProductRequest();
            ProductResponse result = productService.create(request);
            assertSoftly(it -> {
                it.assertThat(result).isNotNull();
                it.assertThat(result.getId()).isNotNull();
                it.assertThat(result.getName()).isEqualTo(request.getName());
            });
        }

        @DisplayName("상품의 가격은 음수일 수 없다.")
        @Test
        void createWithInvalidPrice() {
            ProductRequest request = createProductRequest(BigDecimal.valueOf(-1L));
            assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상품 리스트를 반환한다.")
    @Test
    void list() {
        Product product1 = createProduct("NAME1", BigDecimal.ONE);
        Product product2 = createProduct("NAME2", BigDecimal.TEN);
        List<Product> expected = productRepository.saveAll(Arrays.asList(product1, product2));
        List<ProductResponse> results = productService.list();
        assertSoftly(it -> {
            it.assertThat(results).hasSize(expected.size());
            it.assertThat(results)
                    .extracting("name")
                    .usingFieldByFieldElementComparator()
                    .contains(product1.getName(), product2.getName());
        });
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }
}
