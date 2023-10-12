package kitchenpos.dao;

import kitchenpos.domain.Product;
import kitchenpos.helper.JdbcTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateProductDaoTest extends JdbcTestHelper {

    @Autowired
    private ProductDao productDao;

    @Test
    void 상품을_저장하면_id값이_채워진다() {
        // given
        Product product = 상품_생성_10000원();

        // when
        Product savedProduct = productDao.save(product);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedProduct.getId()).isNotNull();
            softly.assertThat(savedProduct.getName()).isEqualTo(product.getName());
            softly.assertThat(savedProduct.getPrice().compareTo(product.getPrice())).isZero();
        });
    }

    @Test
    void 상품을_id로_조회할_수_있다() {
        // given
        Product product = productDao.save(상품_생성_10000원());

        // when
        Optional<Product> actual = productDao.findById(product.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).isPresent();
            softly.assertThat(actual.get()).usingRecursiveComparison()
                    .isEqualTo(product);
        });
    }

    @Test
    void 없는_상품_id로_조회하면_Optional_empty를_반환한다() {
        // when
        Optional<Product> actual = productDao.findById(0L);

        // then
        assertThat(actual).isEmpty();
    }
}
