package kitchenpos.application;

import kitchenpos.dto.ProductRequest;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        BigDecimal productPrice = BigDecimal.valueOf(10000L);
        Product product = new Product(productName, productPrice);

        given(productRepository.save(any(Product.class)))
                .willReturn(product);

        ProductRequest productRequest = new ProductRequest(productName, productPrice.longValue());

        // when
        Product actual = productService.create(productRequest);

        // then
        assertThat(actual).isEqualTo(product);
    }

    @Test
    @DisplayName("저장하려는 상품의 가격이 0보다 작으면 예외를 발생시킨다.")
    void negativePriceWillReturnException() {
        // given
        String productName = "product";
        BigDecimal productPrice = BigDecimal.valueOf(-10000L);

        Product product = new Product(productName, productPrice);

        given(productRepository.save(any(Product.class)))
                .willReturn(product);

        ProductRequest productRequest = new ProductRequest(productName, productPrice.longValue());

        // when, then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장하려는 상품의 가격이 null일 경우 예외를 발생시킨다.")
    void nullPriceWillReturnException() {
        // given
        String productName = "product";

        Product product = new Product(productName, null);

        given(productRepository.save(any(Product.class)))
                .willReturn(product);

        ProductRequest productRequest = new ProductRequest(productName, null);

        // when, then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록된 상품들을 불러올 수 있다.")
    void list() {
        // given
        Product product1 = new Product("product1", BigDecimal.valueOf(10000L));
        Product product2 = new Product("product2", BigDecimal.valueOf(10000L));

        List<Product> expected = Arrays.asList(product1, product2);
        given(productRepository.findAll())
                .willReturn(expected);

        // when
        List<Product> actual = productService.list();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
