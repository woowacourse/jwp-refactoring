package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateProductDaoTest extends JdbcTemplateTest {

    private ProductDao productDao;
    private Product product;

    @BeforeEach
    void setUp() {
        productDao = new JdbcTemplateProductDao(dataSource);
        product = makeProduct();
    }

    @Test
    void 상품을_저장한다() {
        Product saved = productDao.save(product);

        assertThat(saved.getName()).isEqualTo(product.getName());
        assertThat(saved.getPrice().longValue()).isEqualTo(product.getPrice().longValue());
    }

    private Product makeProduct() {
        Product product = new Product();
        product.setName("상품1");
        product.setPrice(BigDecimal.ONE);
        return product;
    }

    @Test
    void 식별자로_상품을_조회한다() {
        Product product = productDao.findById(1L).get();

        assertThat(product.getId()).isEqualTo(1L);
    }

    @Test
    void 모든_상품을_조회한다() {
        List<Product> products = productDao.findAll();

        assertThat(products.size()).isEqualTo(6);
    }
}
