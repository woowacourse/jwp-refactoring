package kitchenpos.dao;

import kitchenpos.domain.product.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateProductRepositoryTest {

    @Autowired
    private JdbcTemplateProductRepository productDao;

    private Product product;

    @BeforeEach
    void setUp() {
        product = ProductFixture.아메리카노();
    }

    @Test
    void 상품을_등록한다() {
        // when
        Product savedProduct = productDao.save(product);

        // then
        assertThat(savedProduct).usingRecursiveComparison()
                .ignoringFields("id")
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(product);
    }

    @Test
    void 상품id로_상품을_조회한다() {
        // when
        Product savedProduct = productDao.save(product);
        Product foundProduct = productDao.findById(savedProduct.getId()).get();

        // then
        assertThat(foundProduct).usingRecursiveComparison()
                .ignoringFields("id")
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(product);
    }

    @Test
    void 존재하지_않는_상품id로_상품_조회시_예외가_발생한다() {
        // when
        productDao.save(product);
        Long notExistId = -1L;

        Optional<Product> foundProduct = productDao.findById(notExistId);

        // then
        assertThat(foundProduct).isEmpty();
    }

    @Test
    void 전체_상품_목록을_조회한다() {
        // given
        int originSize = productDao.findAll().size();

        // when
        productDao.save(product);
        List<Product> savedProducts = productDao.findAll();

        // then
        assertThat(savedProducts).hasSize(originSize + 1);
    }
}
