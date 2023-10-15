package kitchenpos.application;

import static kitchenpos.fixture.ProductFixtures.양념치킨_17000원;
import static kitchenpos.fixture.ProductFixtures.후라이드치킨_16000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
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
        Product product = 후라이드치킨_16000원;

        // when
        Product actual = productService.create(product);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("가격이 0원보다 낮은 상품을 생성하면 예외가 발생한다.")
    @ValueSource(ints = {-1, -100, -35_000, -100_000})
    @ParameterizedTest
    void create_PriceLowerThanZero_ExceptionThrown(int invalidPrice) {
        // given
        BigDecimal price = BigDecimal.valueOf(invalidPrice);

        // when, then
        assertThatThrownBy(() -> productService.create(new Product("상품", price)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given
        productService.create(후라이드치킨_16000원);
        productService.create(양념치킨_17000원);

        // when
        List<Product> products = productService.list();

        // then
        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products).allMatch(product -> product.getId() != null)
        );
    }
}
