package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.helper.IntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.상품_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성한다() {
        // given
        Product product = 상품_생성_10000원();

        // when
        Product result = productService.create(product);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getName()).isEqualTo(product.getName());
            softly.assertThat(result.getPrice().compareTo(product.getPrice()));
        });
    }

    @Test
    void 상품_가격은_0원_이상이어야한다() {
        // given
        Product product = 상품_생성("테스트", new BigDecimal(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_목록을_조회할_수_있다() {
        // given
        Product product = productService.create(상품_생성_10000원());

        // when
        List<Product> result = productService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0)).usingRecursiveComparison()
                    .isEqualTo(product);
        });
    }
}
