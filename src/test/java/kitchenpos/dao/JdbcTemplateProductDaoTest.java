package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import(value = JdbcTemplateProductDao.class)
@JdbcTest
class JdbcTemplateProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    void 상품을_저장하면_id값이_채워진다() {
        Product product = 상품_생성_10000원();

        Product savedProduct = productDao.save(product);

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(product.getName()),
                () -> assertThat(savedProduct.getPrice().compareTo(product.getPrice())).isZero()
        );
    }

    @Test
    void 상품을_id로_조회할_수_있다() {
        Product product = productDao.save(상품_생성_10000원());

        Optional<Product> actual = productDao.findById(product.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get()).usingRecursiveComparison()
                        .isEqualTo(product)
        );
    }

    @Test
    void 없는_상품_id로_조회하면_Optional_empty를_반환한다() {
        Optional<Product> actual = productDao.findById(0L);

        assertThat(actual).isEmpty();
    }
}
