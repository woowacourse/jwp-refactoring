package kitchenpos.dao;

import static kitchenpos.utils.TestObjectUtils.NOT_EXIST_VALUE;
import static kitchenpos.utils.TestObjectUtils.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductDaoTest extends DaoTest {

    @Autowired
    private ProductDao productDao;

    private String productName;
    private BigDecimal productPrice;

    @BeforeEach
    void setUp() {
        productName = "강정치킨";
        productPrice = BigDecimal.valueOf(17000L);
    }

    @DisplayName("상품 save - 성공")
    @Test
    void save() {
        Product product = createProduct(productName, productPrice);
        Product savedProduct = productDao.save(product);

        assertAll(() -> {
            assertThat(savedProduct.getId()).isNotNull();
            assertThat(savedProduct.getName()).isEqualTo(productName);
            assertThat(savedProduct.getPrice()).isEqualByComparingTo(productPrice);
        });
    }

    @DisplayName("상품 findById - 성공")
    @Test
    void findById() {
        Product product = createProduct(productName, productPrice);
        Product savedProduct = productDao.save(product);
        Optional<Product> foundProduct = productDao.findById(savedProduct.getId());

        assertThat(foundProduct.isPresent()).isTrue();
    }

    @DisplayName("상품 findById - 예외, 빈 데이터에 접근하려고 하는 경우")
    @Test
    void findById_EmptyResultDataAccess_ThrownException() {
        Optional<Product> foundProduct = productDao.findById(NOT_EXIST_VALUE);

        assertThat(foundProduct.isPresent()).isFalse();
    }

    @DisplayName("상품 findAll - 성공")
    @Test
    void findAll() {
        List<Product> products = productDao.findAll();

        assertThat(products).hasSize(6);
    }
}
