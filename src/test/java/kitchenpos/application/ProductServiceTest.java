package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    @DisplayName("상품 생성 테스트")
    class CreateProductTest {

        @Test
        @DisplayName("상품 생성에 성공한다.")
        void success() {
            // given
            final Product productRequest = new Product();
            productRequest.setName("테스트 상품");
            productRequest.setPrice(BigDecimal.valueOf(1000));

            // when
            final Product createdProduct = productService.create(productRequest);

            // then
            assertThat(createdProduct.getPrice()).isNotNull();
        }

        @ParameterizedTest(name = "입력값 : {0}")
        @CsvSource(value = {"-1", "null"}, nullValues = {"null"})
        @DisplayName("상품의 가격이 잘못되면 예외를 발생시킨다.")
        void throwExceptionWithWrongPrice(final BigDecimal price) {
            // given
            final Product productRequest = new Product();
            productRequest.setName("테스트 상품");
            productRequest.setPrice(price);

            // when
            // then
            Assertions.assertThatThrownBy(() -> productService.create(productRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("모든 상품을 조회한다.")
    void test() {
        // given
        final Product productRequest = new Product();
        productRequest.setName("테스트 상품");
        productRequest.setPrice(BigDecimal.valueOf(1000));
        final Product testProduct = productService.create(productRequest);

        // when
        final List<Product> products = productService.list();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(products).isNotEmpty();
            final Product createdProduct = products.get(products.size() - 1);
            softly.assertThat(createdProduct.getId()).isEqualTo(testProduct.getId());
        });

    }

}
