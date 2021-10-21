package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품Dao 테스트")
public class JdbcTemplateProductDaoTest extends DaoTest {
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new JdbcTemplateProductDao(dataSource);
    }

    @DisplayName("상품을 저장한다.")
    @Test
    void save() {
        // given
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(17000.00));

        // when
        Product savedProduct = productDao.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getPrice().compareTo(product.getPrice())).isEqualTo(0);
    }

    @DisplayName("id로 주문을 조회한다.")
    @Test
    void findById() {
        // given
        long productId = SAVE_PRODUCT_RETURN_ID();

        // when
        Optional<Product> findProduct = productDao.findById(productId);

        // then
        assertThat(findProduct).isPresent();
        Product product = findProduct.get();
        assertThat(product.getId()).isEqualTo(productId);
    }

    @DisplayName("모든 상품을 조회한다.")
    @Test
    void findAll() {
        // given
        SAVE_PRODUCT_RETURN_ID();

        // when
        List<Product> products = productDao.findAll();

        // then
        // 초기화를 통해 등록된 메뉴 6개 + 새로 추가한 메뉴 1개
        assertThat(products).hasSize(6 + 1);

    }

    private long SAVE_PRODUCT_RETURN_ID() {
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(17000.00));

        // when
        Product savedProduct = productDao.save(product);
        return savedProduct.getId();
    }
}
