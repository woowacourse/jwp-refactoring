package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.request.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        // given
        String name = "후라이드치킨";
        BigDecimal price = BigDecimal.valueOf(16_000);
        ProductCreateRequest request = new ProductCreateRequest(name, price);

        // when
        Product actual = productService.create(request);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("가격이 0원보다 낮은 상품을 생성하면 예외가 발생한다.")
    @ValueSource(ints = {-1, -100, -35_000, -100_000})
    @ParameterizedTest
    void create_PriceLowerThanZero_ExceptionThrown(int invalidPrice) {
        // given
        String name = "후라이드치킨";
        BigDecimal price = BigDecimal.valueOf(invalidPrice);
        ProductCreateRequest request = new ProductCreateRequest(name, price);

        // when, then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given
        productService.create(new ProductCreateRequest("후라이드치킨", BigDecimal.valueOf(16_000)));
        productService.create(new ProductCreateRequest("양념치킨", BigDecimal.valueOf(17_000)));

        // when
        List<Product> products = productService.list();

        // then
        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products).allMatch(product -> product.getId() != null)
        );
    }
}
