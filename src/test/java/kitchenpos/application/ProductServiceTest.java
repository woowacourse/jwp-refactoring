package kitchenpos.application;

import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.ProductFixtures.PIZZA;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("상품 생성 테스트")
    class CreateProductTest {

        @Test
        @DisplayName("상품 생성에 성공한다.")
        void success() {
            // given
            final ProductCreateRequest request = new ProductCreateRequest("테스트 상품", BigDecimal.valueOf(1000));

            // when
            final ProductResponse response = productService.create(request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();
                softly.assertThat(response.getName()).isEqualTo(request.getName());
                softly.assertThat(response.getPrice()).isEqualByComparingTo(request.getPrice());
            });
        }

        @ParameterizedTest(name = "입력값 : {0}")
        @CsvSource(value = {"-1", "null"}, nullValues = {"null"})
        @DisplayName("상품의 가격이 잘못되면 예외를 발생시킨다.")
        void throwExceptionWithWrongPrice(final BigDecimal price) {
            // given
            final ProductCreateRequest request = new ProductCreateRequest("테스트 상품", price);

            // when
            // then
            Assertions.assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("모든 상품을 조회한다.")
    void getProductList() {
        // given
        final Product testProduct = productRepository.save(PIZZA());
        final ProductResponse expectedLastResponse = ProductResponse.from(testProduct);

        // when
        final List<ProductResponse> response = productService.list();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotEmpty();
            final ProductResponse actualLastProduct = response.get(response.size() - 1);
            softly.assertThat(actualLastProduct).usingRecursiveComparison()
                    .isEqualTo(expectedLastResponse);
        });
    }
}
