package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Optional;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void 상품을_저장한다() {
        // given
        product.setName("상품");
        product.setPrice(BigDecimal.ONE);

        // when
        Product savedProduct = productService.create(product);
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
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품의_가격이_0보다_작으면_에외가_발생한다() {
        // given
        product.setPrice(BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
