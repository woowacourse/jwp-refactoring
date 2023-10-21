package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Nested
    class 상품_등록 {

        @Test
        void 상품을_등록할_수_있다() {
            // given
            final var product = ProductFixture.상품_망고_1000원();
            given(productRepository.save(any()))
                    .willReturn(product);

            // when
            final var actual = productService.create(product);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(product);
        }

        @Test
        void 상품의_가격이_0보다_작으면_예외가_발생한다() {
            // given
            final var product = ProductFixture.상품_망고_N원(-1);

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 상품_목록_조회 {

        @Test
        void 상품_목록을_조회할_수_있다() {
            // given
            final var products = List.of(ProductFixture.상품_망고_1000원(), ProductFixture.상품_치킨_15000원());
            given(productRepository.findAll())
                    .willReturn(products);

            // when
            final var actual = productService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(products);
        }
    }
}
