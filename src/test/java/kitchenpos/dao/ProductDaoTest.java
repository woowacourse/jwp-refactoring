package kitchenpos.dao;

import static kitchenpos.support.ProductFixture.PRODUCT_PRICE_10000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductDaoTest extends JdbcDaoTest {

    @Test
    void 제품을_저장할_수_있다() {
        // given
        final Product product = PRODUCT_PRICE_10000.생성();

        // when
        final Product savedProduct = productDao.save(product);

        // then
        assertThat(savedProduct.getId()).isEqualTo(1L);
    }

    @Test
    void 제품을_아이디로_조회할_수_있다() {
        // given
        final Product savedProduct = 제품을_저장한다(PRODUCT_PRICE_10000.생성());

        // when
        final Product foundProduct = productDao.findById(savedProduct.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
                () -> assertThat(foundProduct.getName()).isEqualTo("제품1"),
                () -> assertThat(foundProduct.getPrice().intValue()).isEqualTo(10000)
        );
    }

    @Test
    void 제품을_모두_조회할_수_있다() {
        // given
        final Product savedProduct = 제품을_저장한다(PRODUCT_PRICE_10000.생성());

        // when
        final List<Product> products = productDao.findAll();

        // then
        assertThat(products).usingFieldByFieldElementComparator()
                .containsOnly(savedProduct);
    }
}
