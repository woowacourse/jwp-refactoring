package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @DisplayName("상품을 저장할 수 있다.")
    void create() {
        // given
        String productName = "product";
        Long productPrice = 10000L;
        Product expectedProduct = new Product(productName, productPrice);

        given(productRepository.save(any(Product.class)))
                .willReturn(expectedProduct);

        ProductRequest productRequest = new ProductRequest(productName, productPrice);

        // when
        ProductResponse actual = productService.create(productRequest);
        ProductResponse expected = ProductResponse.from(expectedProduct);

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("등록된 상품들을 불러올 수 있다.")
    void list() {
        // given
        Product product1 = new Product("product1", 10000L);
        Product product2 = new Product("product2", 10000L);

        List<Product> expectedProducts = Arrays.asList(product1, product2);
        given(productRepository.findAll())
                .willReturn(expectedProducts);

        List<ProductResponse> expected = expectedProducts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());

        // when
        List<ProductResponse> actual = productService.list();


        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
