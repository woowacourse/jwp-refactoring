package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends ApplicationTest {

    @DisplayName("새로운 상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final Product product = new Product("후라이드", BigDecimal.valueOf(16000));

        // when
        final Product createdProduct = productService.create(product);

        // then
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getId()).isNotNull();
    }

    @DisplayName("상품의 가격이 0보다 작으면 상품을 등록할 수 없다.")
    @Test
    void createWithMinusPrice() {
        // given
        final Product product = new Product("후라이드", BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 없는 경우 상품을 등록할 수 없다.")
    @Test
    void createWithNullPrice() {
        // given
        final Product product = new Product();
        product.setName("후라이드");

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 전체 조회할 수 있다.")
    @Test
    void list() {
        // when
        final List<Product> products = productService.list();

        // then
        assertThat(products)
                .hasSize(6)
                .extracting("name", "price")
                .containsExactlyInAnyOrder(
                        tuple("후라이드", BigDecimal.valueOf(16000)),
                        tuple("양념치킨", BigDecimal.valueOf(16000)),
                        tuple("반반치킨", BigDecimal.valueOf(16000)),
                        tuple("통구이", BigDecimal.valueOf(16000)),
                        tuple("간장치킨", BigDecimal.valueOf(17000)),
                        tuple("순살치킨", BigDecimal.valueOf(17000))
                );
    }
}
