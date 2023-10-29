package kitchenpos.product.application;

import static kitchenpos.support.TestFixtureFactory.새로운_상품;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.exception.PriceException;
import kitchenpos.exception.ProductException;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_등록한다() {
        ProductCreateRequest 상품_생성_요청 = new ProductCreateRequest("상품", new BigDecimal("10000.00"));

        ProductResponse 상품_생성_응답 = productService.create(상품_생성_요청);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(상품_생성_응답.getId()).isNotNull();
            softly.assertThat(상품_생성_응답.getName()).isEqualTo("상품");
            softly.assertThat(상품_생성_응답.getPrice()).isEqualTo(new BigDecimal("10000.00"));
        });
    }

    @Test
    void 상품의_이름은_최대_255자이다() {
        ProductCreateRequest 상품_생성_요청 = new ProductCreateRequest("짱".repeat(256), new BigDecimal("10000.00"));
        Assertions.assertThatThrownBy(() -> productService.create(상품_생성_요청))
                .isInstanceOf(ProductException.class)
                .hasMessage("상품의 이름이 유효하지 않습니다.");
    }

    @Test
    void 상품의_가격은_0원_이상이어야_한다() {
        ProductCreateRequest 상품_생성_요청 = new ProductCreateRequest("상품", new BigDecimal(-1));

        Assertions.assertThatThrownBy(() -> productService.create(상품_생성_요청))
                .isInstanceOf(PriceException.class)
                .hasMessage("가격이 유효하지 않습니다.");
    }

    @Test
    void 상품의_가격은_100조원_미만이어야_한다() {
        ProductCreateRequest 상품_생성_요청 = new ProductCreateRequest("상품", BigDecimal.valueOf(Math.pow(10, 20)));

        Assertions.assertThatThrownBy(() -> productService.create(상품_생성_요청))
                .isInstanceOf(PriceException.class)
                .hasMessage("가격이 유효하지 않습니다.");
    }

    @Test
    void 상품의_목록을_조회한다() {
        Product 등록된_상품 = productRepository.save(새로운_상품(null, "상품", new BigDecimal(10000)));

        List<ProductResponse> 상품_목록_조회_응답 = productService.readAll();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(상품_목록_조회_응답).hasSize(1);
            softly.assertThat(상품_목록_조회_응답.get(0).getName()).isEqualTo("상품");
            softly.assertThat(상품_목록_조회_응답.get(0).getPrice()).isEqualTo(new BigDecimal(10000));
        });
    }
}
