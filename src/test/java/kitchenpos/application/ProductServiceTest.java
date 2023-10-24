package kitchenpos.application;


import kitchenpos.application.dto.request.CreateProductRequest;
import kitchenpos.application.dto.response.CreateProductResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Nested
    class 상품_등록 {

        @Test
        void 상품을_등록한다() {
            // given
            CreateProductRequest request = ProductFixture.REQUEST.후라이드_치킨_16000원();
            Product product = ProductFixture.PRODUCT.후라이드_치킨();
            given(productRepository.save(any(Product.class)))
                    .willReturn(product);

            // when
            CreateProductResponse result = productService.create(request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(result.getId()).isEqualTo(product.getId());
                softly.assertThat(result.getName()).isEqualTo(product.getName());
                softly.assertThat(result.getPrice()).isEqualTo(product.getPrice().toString());
            });
        }

        @ParameterizedTest(name = "상품의 가격이 {0}이면 예외")
        @ValueSource(strings = {"-1", "-9999999"})
        void 상품의_가격이_0원_미만이면_예외(String price) {
            // given
            CreateProductRequest request = ProductFixture.REQUEST.후라이드_치킨_N원(price);

            // when & then
            Assertions.assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 상품_목록_조회 {

        @Test
        void 상품_목록을_조회한다() {
            // given
            List<Product> products = List.of(ProductFixture.PRODUCT.후라이드_치킨());
            given(productRepository.findAll())
                    .willReturn(products);

            // when
            List<ProductResponse> result = productService.list();

            // then
            SoftAssertions.assertSoftly(softly -> {
                Product product = products.get(0);
                ProductResponse productResponse = result.get(0);
                softly.assertThat(result).hasSize(1);
                softly.assertThat(productResponse.getId()).isEqualTo(product.getId());
                softly.assertThat(productResponse.getName()).isEqualTo(product.getName());
                softly.assertThat(productResponse.getPrice()).isEqualTo(product.getPrice().toString());
            });
        }
    }
}
