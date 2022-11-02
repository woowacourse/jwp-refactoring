package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.ProductCreateRequest;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends ServiceTest {

    @Test
    void 상품을_생성한다() {
        ProductCreateRequest productCreateRequest =
                new ProductCreateRequest("뿌링 치킨", BigDecimal.valueOf(10_000L));
        Product savedProduct = productService.create(productCreateRequest);

        assertThat(productRepository.findById(savedProduct.getId())).isPresent();
    }

    @Test
    void 상품목록을_불러온다() {
        int beforeSize = productService.list().size();
        productRepository.save(new Product("뿌링 치킨", BigDecimal.valueOf(10_000L)));

        assertThat(productService.list().size()).isEqualTo(beforeSize + 1);
    }
}
