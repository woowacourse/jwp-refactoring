package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.상품;
import static kitchenpos.fixture.ProductFixture.상품_등록_요청;
import static kitchenpos.fixture.ProductFixture.상품_등록_응답;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.product.Product;
import kitchenpos.repositroy.ProductRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest implements ServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @Test
    void 상품_등록_후_등록된_상품_정보를_반환한다() {
        // given
        final ProductCreateRequest request = 상품_등록_요청("후라이드치킨", BigDecimal.valueOf(1000));

        // when
        final ProductResponse savedProduct = productService.create(request);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(savedProduct.getId()).isNotNull();
            softly.assertThat(savedProduct.getProductName()).isEqualTo("후라이드치킨");
            softly.assertThat(savedProduct.getProductPrice()).isEqualTo(BigDecimal.valueOf(1000).setScale(2));
        });
    }


    @Test
    void 등록된_상품을_전체_조회한다() {
        // given
        Product product1 = productRepository.save(상품("후라이드치킨", BigDecimal.valueOf(1000L)));
        Product product2 = productRepository.save(상품("양념치킨", BigDecimal.valueOf(1200L)));

        // when
        final List<ProductResponse> result = productService.list();

        // then
        assertThat(result)
                .hasSize(2)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        상품_등록_응답(product1),
                        상품_등록_응답(product2)
                ));
    }

    @Test
    void 등록된_상품이_없다면_빈_리스트를_반환한다() {
        final List<ProductResponse> result = productService.list();

        assertThat(result).isEmpty();
    }
}
