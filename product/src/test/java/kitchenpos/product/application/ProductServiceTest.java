package kitchenpos.product.application;

import static kitchenpos.product.application.ProductServiceTest.ProductRequestFixture.상품_생성_요청;
import static kitchenpos.product.domain.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql(value = "classpath:test_truncate_table.sql", executionPhase = BEFORE_TEST_METHOD)
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

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
    void 전체_상품을_조회한다() {
        // given
        Long productId = productRepository.save(상품()).getId();

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
