package kitchenpos.application;

import static kitchenpos.application.DomainFixture.getProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends ServiceTest {

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        final Product product = getProduct();

        final Product savedProduct = 상품_등록(product);

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(product.getName()),
                () -> assertThat(savedProduct.getPrice()).isEqualByComparingTo(product.getPrice())
        );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        상품_등록(getProduct());

        final List<Product> products = productService.list();

        assertThat(products).hasSize(1);
    }
}
