package kitchenpos.application;

import static kitchenpos.application.ProductServiceTest.ProductRequestFixture.상품_생성_요청;
import static kitchenpos.common.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.ProductDao;
import kitchenpos.dto.product.ProductCreateRequest;
import kitchenpos.dto.product.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성한다() {
        // given
        ProductCreateRequest productCreateRequest = 상품_생성_요청();

        // when
        ProductResponse createdProduct = productService.create(productCreateRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(createdProduct.getId()).isNotNull();
            softly.assertThat(createdProduct).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(ProductResponse.from(상품()));
        });
    }

    @Test
    void 상품을_생성할_때_가격이_0미만이면_예외를_던진다() {
        // given
        ProductCreateRequest invalidProduct = 상품_생성_요청(BigDecimal.valueOf(-1L));

        // expect
        assertThatThrownBy(() -> productService.create(invalidProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품을_생성할_때_가격이_null이면_예외를_던진다() {
        // given
        ProductCreateRequest invalidProduct = 상품_생성_요청(null);

        // expect
        assertThatThrownBy(() -> productService.create(invalidProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        Long productId = productDao.save(상품()).getId();

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertThat(products).usingRecursiveComparison()
                .isEqualTo(List.of(ProductResponse.from(상품(productId))));
    }

    static class ProductRequestFixture {

        public static ProductCreateRequest 상품_생성_요청() {
            return new ProductCreateRequest("productName", BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP));
        }

        public static ProductCreateRequest 상품_생성_요청(BigDecimal price) {
            return new ProductCreateRequest("productName", price);
        }
    }
}
