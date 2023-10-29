package kitchenpos.application.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.ServiceTest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    class 상품_등록 {

        @Test
        void 상품을_등록할_수_있다() {
            // given
            final var product = ProductFixture.상품_망고_1000원();
            final var request = ProductFixture.상품요청_생성(product);

            // when
            final var actual = productService.create(request);

            // then
            final var expected = ProductResponse.toResponse(ProductFixture.상품_망고_1000원());
            assertThat(actual).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }

        @Test
        void 상품의_가격이_0보다_작으면_예외가_발생한다() {
            // given
            final var request = ProductFixture.상품요청_생성("INVALID", -1);

            // when & then
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 상품_목록_조회 {

        @Test
        void 상품_목록을_조회할_수_있다() {
            // given
            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            // when
            final var actual = productService.list();

            // then
            final var expected = List.of(ProductResponse.toResponse(product1), ProductResponse.toResponse(product2));
            assertThat(actual).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }
    }
}
