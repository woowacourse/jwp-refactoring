package kitchenpos.application;

import kitchenpos.domain.product.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = ProductFixture.아메리카노();
    }

    @Test
    void 상품을_등록한다() {
        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct).usingRecursiveComparison()
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .ignoringFields("id")
                .isEqualTo(product);
    }

    @Test
    void 상품_가격이_null이면_등록할_수_없다() {
        // given
        product.setPrice(null);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(product));
    }

    @Test
    void 상품_가격이_0보다_작으면_등록할_수_없다() {
        // given
        product.setPrice(BigDecimal.valueOf(-1));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(product));
    }

    @Test
    void 상품_목록을_조회한다() {
        // given
        Product savedProduct = productService.create(product);

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products.get(products.size() - 1))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedProduct);
    }
}
