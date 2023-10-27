package kitchenpos.application;

import kitchenpos.domain.dto.ProductRequest;
import kitchenpos.domain.dto.ProductResponse;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import support.fixture.ProductBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    private static Stream<List<Product>> should_return_product_list_when_request_list() {
        final Product product1 = new ProductBuilder().build();
        final Product product2 = new ProductBuilder().build();
        final Product product3 = new ProductBuilder().build();

        return Stream.of(
                List.of(),
                List.of(product1),
                List.of(product2, product3)
        );
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("모든 상품 목록을 조회할 수 있다.")
    void should_return_product_list_when_request_list(final List<Product> newProducts) {
        // given
        productRepository.saveAll(newProducts);

        final List<Product> products = productRepository.findAll();

        final List<ProductResponse> expect = products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());

        // when
        final List<ProductResponse> actual = productService.list();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("price")
                .isEqualTo(expect);
    }

    @Nested
    @DisplayName("상품 가격 테스트")
    class PriceTest {

        @ParameterizedTest
        @CsvSource(value = {"0", "1", "100000000000"})
        @DisplayName("상품 가격이 0 이상일 경우 상품이 정상적으로 저장된다.")
        void should_create_when_price_is_not_null_and_greater_or_equal_then_zero(final BigDecimal price) {
            // given
            final ProductRequest request = new ProductRequest("상품", price);

            // when
            final ProductResponse expect = productService.create(request);

            // then
            final Product product = productRepository.findById(expect.getId()).get();

            final ProductResponse actual = ProductResponse.from(product);

            assertAll(
                    () -> Assertions.assertEquals(expect.getId(), actual.getId()),
                    () -> Assertions.assertEquals(expect.getName(), actual.getName()),
                    () -> assertThat(expect.getPrice()).isEqualByComparingTo(actual.getPrice())
            );
        }

        @ParameterizedTest
        @NullSource
        @CsvSource(value = {"-1", "-2", "-100000000000"})
        @DisplayName("상품 null이거나 가격이 0 미만일 경우 IllegalArgumentException이 발생한다.")
        void should_throw_IllegalArgumentException_when_price_is_smaller_then_zero(final BigDecimal price) {
            // given
            final ProductRequest request = new ProductRequest("상품", price);

            // when & then
            assertThrowsExactly(IllegalArgumentException.class, () -> productService.create(request));
        }
    }
}
