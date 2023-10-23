package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import kitchenpos.product.application.dto.ProductRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품_등록() {
        // given
        ProductRequest request = new ProductRequest("제이슨의 무료 나눔 마우스", BigDecimal.valueOf(0));

        // when
        ProductResponse savedProduct = productService.create(request);
        Long productId = savedProduct.getId();

        // then
        assertThat(productRepository.findById(productId)).isPresent();

    }

    @Test
    void 상품_목록_조회() {
        // given
        List<Product> products = List.of(
                new Product("파스타", new Price(new BigDecimal("28000.00"))),
                new Product("스테이크", new Price(new BigDecimal("60000.00")))
        );

        Iterable<Product> savedProducts = productRepository.saveAll(products);
        List<ProductResponse> expected = StreamSupport.stream(savedProducts.spliterator(), false)
                .map(ProductResponse::from)
                .collect(Collectors.toList());

        // when
        List<ProductResponse> actual = productService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

}
