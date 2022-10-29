package kitchenpos.application;

import static kitchenpos.DtoFixture.getProductCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.ui.request.prodcut.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends ServiceTest {

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        final ProductCreateRequest request = getProductCreateRequest("마이쮸", BigDecimal.valueOf(800));

        final Product savedProduct = 상품_등록(request);

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(request.getName()),
                () -> assertThat(savedProduct.getPrice()).isEqualByComparingTo(request.getPrice())
        );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        상품_등록(getProductCreateRequest("마이쮸", BigDecimal.valueOf(800)));

        final List<Product> products = productService.list();

        assertThat(products).hasSize(1);
    }
}
