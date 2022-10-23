package kitchenpos.dao;

import static kitchenpos.support.ProductFixture.PRODUCT_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class ProductDaoTest extends JdbcDaoTest {

    @Test
    void 제품을_저장할_수_있다() {
        // given
        final Product product = PRODUCT_1.생성();

        // when
        final Product savedProduct = productDao.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    void 제품을_아이디로_조회할_수_있다() {
        // given
        final Product savedProduct = 상품을_저장한다(PRODUCT_1.생성());

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
        final int alreadyExistCount = productDao.findAll().size();
        final Product savedProduct = 상품을_저장한다(PRODUCT_1.생성());

        // when
        final List<Product> products = productDao.findAll();

        // then
        assertThat(products).usingFieldByFieldElementComparator()
                .hasSize(alreadyExistCount + 1)
                .contains(savedProduct);
    }
}
