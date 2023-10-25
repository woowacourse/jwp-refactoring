package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.ProductCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.TestFixtureFactory.새로운_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품을_등록한다() {
        ProductCreateRequest 상품_생성_요청 = new ProductCreateRequest("상품", new BigDecimal("10000.00"));

        Product product = productService.create(상품_생성_요청);

        assertSoftly(softly -> {
            softly.assertThat(product.getId()).isNotNull();
            softly.assertThat(product.getProductName().getName()).isEqualTo("상품");
        });
    }

    @Test
    void 상품의_가격은_0원_이상이어야_한다() {
        ProductCreateRequest 상품_생성_요청 = new ProductCreateRequest("상품", new BigDecimal(-1));

        assertThatThrownBy(() -> productService.create(상품_생성_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품의_목록을_조회한다() {
        Product 등록된_상품 = productRepository.save(새로운_상품(null, "상품", new BigDecimal(10000)));

        List<Product> products = productService.findAll();

        assertThat(products).hasSize(1);
    }
}
