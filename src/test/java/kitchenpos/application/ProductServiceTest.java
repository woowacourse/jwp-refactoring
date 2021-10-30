package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class ProductServiceTest {
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("Product를 저장할 수 있다.")
    void create() {
        // given
        String productName = "product";
        Long productPrice = 10000L;
        Product expected = new Product(productName, productPrice);

        given(productRepository.save(any(Product.class)))
                .willReturn(expected);

        ProductRequest productRequest = new ProductRequest(productName, productPrice);

        // when
        Product actual = productService.create(productRequest);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("등록된 상품들을 불러올 수 있다.")
    void list() {
        // given
        Product product1 = new Product("product1", 10000L);
        Product product2 = new Product("product2", 10000L);

        List<Product> expected = Arrays.asList(product1, product2);
        given(productRepository.findAll())
                .willReturn(expected);

        // when
        List<Product> actual = productService.list();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
