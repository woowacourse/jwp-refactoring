package kitchenpos.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.application.common.factory.ProductFactory;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @DisplayName("생성 - 상품을 생성(등록)할 수 있다.")
    @Test
    void create_success() {
        // given
        Product product = ProductFactory.create("후라이드", BigDecimal.valueOf(16_000));

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct.getName())
            .isEqualTo(product.getName());
        assertThat(savedProduct.getPrice().longValue())
            .isEqualTo(product.getPrice().longValue());
    }

    // TODO: @ParameterizedTest 특정 객체로 할 수 있는 방법 찾기
    @DisplayName("생성 - 상품 가격은 0보다 작을 수 없다.")
    @Test
    void create_price_fail() {
        // given, when
        Product product1 = ProductFactory.create("후라이드", BigDecimal.valueOf(-1));
        Product product2 = ProductFactory.create("반반치킨", null);

        // then
        assertThatThrownBy(() -> productService.create(product1))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> productService.create(product2))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조회 - 전체 상품을 조회할 수 있다.")
    @Test
    void list_success() {
        // given
        Product product1 = ProductFactory.create("후라이드", BigDecimal.valueOf(16_000));
        Product product2 = ProductFactory.create("반반치킨", BigDecimal.valueOf(15_000));

        // when
        Product savedProduct1 = productService.create(product1);
        Product savedProduct2 = productService.create(product2);
        List<Product> products = productService.list();

        // then
        assertThat(products).hasSize(2);
        assertThat(products)
            .containsExactlyInAnyOrder(savedProduct1, savedProduct2);
    }
}
