package kitchenpos.domain;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
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

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Nested
    class 상품_등록 {

        @Test
        void 상품을_등록한다() {
            // given
            Product product = ProductFixture.PRODUCT.후라이드_치킨();
            given(productDao.save(any(Product.class)))
                    .willReturn(product);

            // when
            Product result = productService.create(product);

            // then
            Assertions.assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(product);
        }

        @ParameterizedTest(name = "상품의 가격이 {0}이면 예외")
        @ValueSource(strings = {"-1", "-9999999"})
        void 상품의_가격이_0원_미만이면_예외(Long price) {
            // given
            Product product = ProductFixture.PRODUCT.후라이드_치킨(price);

            // when & then
            Assertions.assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 상품_목록_조회 {

        @Test
        void 상품_목록을_조회한다() {
            // given
            List<Product> products = List.of(ProductFixture.PRODUCT.후라이드_치킨());
            given(productDao.findAll())
                    .willReturn(products);

            // when
            List<Product> result = productService.list();

            // then
            Assertions.assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(products);
        }
    }
}
