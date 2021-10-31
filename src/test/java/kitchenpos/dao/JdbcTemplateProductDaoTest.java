package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static kitchenpos.testutils.TestDomainBuilder.productBuilder;
import static org.assertj.core.api.Assertions.assertThat;

@Import(JdbcTemplateProductDao.class)
class JdbcTemplateProductDaoTest extends AbstractJdbcTemplateDaoTest {

    private static final Long NON_EXISTENT_ID = 987654321L;

    @Autowired
    private ProductDao productDao;

    private Product friedChicken, seasoningChicken;

    @BeforeAll
    void beforeAll() {
        friedChicken = productDao.save(
                productBuilder()
                        .name("후라이드")
                        .price(BigDecimal.valueOf(16000))
                        .build()
        );
        seasoningChicken = productDao.save(
                productBuilder()
                        .name("양념치킨")
                        .price(BigDecimal.valueOf(16000))
                        .build()
        );
    }

    @DisplayName("상품을 저장한다.")
    @Test
    void save() {
        // given
        String name = "반반치킨";
        BigDecimal price = BigDecimal.valueOf(16000);
        Product newProduct = productBuilder()
                .name(name)
                .price(price)
                .build();

        // when
        Product product = productDao.save(newProduct);

        // then
        assertThat(product.getId()).isNotNull();
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualByComparingTo(price);
    }

    @DisplayName("존재하는 상품을 조회한다.")
    @Test
    void findByIdWhenExistent() {
        // given
        Long id = friedChicken.getId();

        // when
        Optional<Product> optionalProduct = productDao.findById(id);

        // then
        assertThat(optionalProduct).get()
                .usingRecursiveComparison()
                .isEqualTo(friedChicken);
    }

    @DisplayName("존재하지 않는 상품을 조회한다.")
    @Test
    void findByIdWhenNonexistent() {
        // when
        Optional<Product> optionalProduct = productDao.findById(NON_EXISTENT_ID);

        // then
        assertThat(optionalProduct).isEmpty();
    }

    @DisplayName("전체 상품을 조회한다.")
    @Test
    void findAll() {
        // when
        List<Product> products = productDao.findAll();

        // then
        assertThat(products)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(friedChicken, seasoningChicken);
    }
}
