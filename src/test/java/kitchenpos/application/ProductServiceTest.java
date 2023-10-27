package kitchenpos.application;

import kitchenpos.common.exception.PriceEmptyException;
import kitchenpos.helper.IntegrationTestHelper;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductCreateRequest;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static kitchenpos.fixture.ProductFixture.상품_생성_요청;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends IntegrationTestHelper {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성한다() {
        // given
        Product product = 상품_생성_10000원();
        ProductCreateRequest request = 상품_생성_요청(product);

        // when
        Product result = productService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getName()).isEqualTo(product.getName());
            softly.assertThat(result.getPrice().compareTo(product.getPrice()));
        });
    }

    @Test
    void 상품_가격은_0원_이상이어야한다() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("테스트", -1L);

        // when & then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(PriceEmptyException.class);
    }

    @Test
    void 상품_목록을_조회할_수_있다() {
        // given
        Product product = productService.create(상품_생성_요청(상품_생성_10000원()));

        // when
        List<Product> result = productService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0).getId()).usingRecursiveComparison()
                    .isEqualTo(product.getId());
        });
    }
}
