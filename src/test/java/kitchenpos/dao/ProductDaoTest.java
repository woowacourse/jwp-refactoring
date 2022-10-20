package kitchenpos.dao;

import static kitchenpos.support.TestFixtureFactory.상품을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    void 상품을_저장하면_id값이_채워진다() {
        Product product = 상품을_생성한다("상품", new BigDecimal(0));

        Product savedProduct = productDao.save(product);

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(product.getName()),
                () -> assertThat(savedProduct.getPrice().compareTo(product.getPrice())).isZero()
        );
    }

    @Test
    void 상품을_id로_조회할_수_있다() {
        Product product = productDao.save(상품을_생성한다("상품", new BigDecimal(0)));

        Product actual = productDao.findById(product.getId())
                .orElseGet(Assertions::fail);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    void 없는_상품_id로_조회하면_Optional_empty를_반환한다() {
        Optional<Product> actual = productDao.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_상품을_조회할_수_있다() {
        Product product1 = productDao.save(상품을_생성한다("상품1", new BigDecimal(1_000)));
        Product product2 = productDao.save(상품을_생성한다("상품2", new BigDecimal(2_000)));

        List<Product> actual = productDao.findAll();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("price")
                .containsExactly(product1, product2);
    }
}
