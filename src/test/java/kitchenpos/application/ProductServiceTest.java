package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends IntegrationTest {

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product2 = new Product();
    }

    @Test
    void 상품을_저장한다() {
        // given
        product1.setName("상품");
        product1.setPrice(BigDecimal.ONE);

        // when
        Product savedProduct = productService.create(product1);
        Optional<Product> result = productDao.findById(savedProduct.getId());

        // then
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getId()).isPositive(),
                () -> assertThat(result.get().getPrice()).isEqualByComparingTo(BigDecimal.ONE)
        );
    }

    @Test
    void 상품의_가격이_null이면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> productService.create(product1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품의_가격이_0보다_작으면_에외가_발생한다() {
        // given
        product1.setPrice(BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(product1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품들을_조회한다() {
        // given
        product1.setName("상품1");
        product1.setPrice(BigDecimal.ONE);
        productDao.save(product1);
        product2.setName("상품2");
        product2.setPrice(BigDecimal.TEN);
        productDao.save(product2);

        // when
        List<Product> result = productService.list();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isPositive(),
                () -> assertThat(result.get(0).getName()).isEqualTo("상품1"),
                () -> assertThat(result.get(0).getPrice()).isEqualByComparingTo(BigDecimal.ONE),
                () -> assertThat(result.get(1).getId()).isPositive(),
                () -> assertThat(result.get(1).getName()).isEqualTo("상품2"),
                () -> assertThat(result.get(1).getPrice()).isEqualByComparingTo(BigDecimal.TEN)
        );
    }
}
