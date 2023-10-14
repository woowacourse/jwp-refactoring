package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductService productService;

    private static Stream<List<Product>> should_return_product_list_when_request_list() {
        final Product product1 = new Product();
        product1.setName("상품1");
        product1.setPrice(BigDecimal.valueOf(1000));

        final Product product2 = new Product();
        product2.setName("상품2");
        product2.setPrice(BigDecimal.valueOf(2000));

        return Stream.of(
                List.of(),
                List.of(product1),
                List.of(product1, product2)
        );
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("모든 상품 목록을 조회할 수 있다.")
    void should_return_product_list_when_request_list(final List<Product> products) {
        // given
        final List<Product> expect = productDao.findAll();
        expect.addAll(products);

        products.forEach(product -> productDao.save(product));

        // when
        final List<Product> actual = productService.list();

        // then
        assertEquals(expect.size(), actual.size());

        for (int i = 0; i < actual.size(); i++) {
            final Product actualProduct = actual.get(i);
            final Product expectProduct = expect.get(i);

            assertEquals(expectProduct.getName(), actualProduct.getName());
            assertThat(expectProduct.getPrice()).isEqualByComparingTo(actualProduct.getPrice());
        }
    }

    @Nested
    @DisplayName("상품 가격 테스트")
    class PriceTest {

        @ParameterizedTest
        @CsvSource(value = {"0", "1", "100000000000"})
        @DisplayName("상품 가격이 null이 아니고 0 이상일 경우 상품이 정상적으로 저장된다.")
        void should_create_when_price_is_not_null_and_greater_or_equal_then_zero(final BigDecimal price) {
            // given
            final Product product = new Product();
            product.setName("상품");
            product.setPrice(price);

            // when
            final Product expect = productService.create(product);

            // then
            final Product actual = productDao.findById(expect.getId()).get();

            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expect);
        }

        @Test
        @DisplayName("상품 가격이 null일 경우 IllegalArgumentException이 발생한다.")
        void should_throw_IllegalArgumentException_when_price_is_null() {
            // given
            final Product product = new Product();
            product.setName("상품");
            product.setPrice(null);

            // when & then
            assertThrowsExactly(IllegalArgumentException.class, () -> productService.create(product));
        }

        @ParameterizedTest
        @CsvSource(value = {"-1", "-2", "-100000000000"})
        @DisplayName("상품 가격이 0 미만일 경우 IllegalArgumentException이 발생한다.")
        void should_throw_IllegalArgumentException_when_price_is_smaller_then_zero(final BigDecimal price) {
            // given
            final Product product = new Product();
            product.setName("상품");
            product.setPrice(price);

            // when & then
            assertThrowsExactly(IllegalArgumentException.class, () -> productService.create(product));
        }
    }
}
