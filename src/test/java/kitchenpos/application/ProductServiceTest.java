package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.request.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@Import(ProductService.class)
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("상품", 1000L);

        // when
        Product actual = productService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(productRepository.findById(actual.getId())).isPresent();
            softly.assertThat(actual.getName()).isEqualTo(request.getName());
            softly.assertThat(actual.getPrice())
                    .isEqualByComparingTo(BigDecimal.valueOf(request.getPrice()));
        });
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        ProductCreateRequest 후라이드_생성_요청 = new ProductCreateRequest("후라이드", 16000L);
        ProductCreateRequest 양념치킨_생성_요청 = new ProductCreateRequest("양념치킨", 16000L);

        Product 후라이드 = productService.create(후라이드_생성_요청);
        Product 양념치킨 = productService.create(양념치킨_생성_요청);

        // when
        List<Product> list = productService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(list).hasSize(2);
            softly.assertThat(list).usingRecursiveComparison()
                    .isEqualTo(List.of(후라이드, 양념치킨));
        });
    }
}
