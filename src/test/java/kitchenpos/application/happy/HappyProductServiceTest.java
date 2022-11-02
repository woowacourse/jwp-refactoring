package kitchenpos.application.happy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.dao.JpaProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductResponse;
import kitchenpos.ui.dto.request.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class HappyProductServiceTest extends HappyServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private JpaProductRepository productRepository;

    @Test
    void 상품을_생성한다() {
        // given
        ProductRequest request = new ProductRequest("상품", BigDecimal.valueOf(10_000));

        // when
        ProductResponse actual = productService.create(request);

        // then
        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getName()).isEqualTo(request.getName());
            assertThat(actual.getPrice().doubleValue()).isEqualTo(request.getPrice().doubleValue());
        });
    }


    @Test
    void 저장된_전체_상품_목록을_반환한다() {
        // given
        int expected = 3;
        for (int i = 0; i < expected; i++) {
            Product product = new Product("product " + i, new BigDecimal(10_000));
            productRepository.save(product);
        }
        // when
        List<ProductResponse> actual = productService.list();
        // then
        assertThat(actual).hasSize(expected);
    }
}
