package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ServiceTest;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.product.application.dto.ProductDto;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Test
    void 상품을_생성할_수_있다() {
        // given
        final var request = 상품_생성_요청("후라이드", 16000);

        // when
        final var response = productService.create(request);

        // then
        assertThat(productRepository.findById(response.getId())).isPresent();
    }

    @Test
    void 상품의_가격이_0원_미만이면_생성할_수_없다() {
        // given
        final var request = 상품_생성_요청("가짜상품", -1000);

        // when & then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0이상이어야 합니다.");
    }

    @Test
    void 상품의_목록을_조회할_수_있다() {
        // given
        final var 후라이드 = productRepository.save(ProductFixture.후라이드_16000);
        final var 양념치킨 = productRepository.save(ProductFixture.양념치킨_16000);
        final var 순살치킨 = productRepository.save(ProductFixture.순살치킨_16000);
        final var 상품목록 = List.of(후라이드, 양념치킨, 순살치킨);

        final var expected = 상품목록.stream()
                .map(ProductDto::toDto)
                .collect(Collectors.toList());

        // when
        final var actual = productService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
