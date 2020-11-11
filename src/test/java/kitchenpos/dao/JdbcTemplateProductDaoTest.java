package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DaoTest
class JdbcTemplateProductDaoTest {
    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("상품 엔티티를 저장하면 id가 부여되고, 엔티티의 필드인 메뉴 상품 리스트는 저장되지 않는다")
    void insert() {
        Product product = createProduct(null, "강정치킨", BigDecimal.ONE);

        Product result = productDao.save(product);

        assertAll(
                () -> assertThat(result).isEqualToIgnoringGivenFields(result, "id"),
                () -> assertThat(result.getId()).isNotNull()
        );
    }


    @Test
    @DisplayName("존재하는 id로 엔티티를 조회하면 저장되어있는 엔티티가 조회된다")
    void findExist() {
        Product menuGroup = createProduct(null, "강정치킨", BigDecimal.ONE);
        Product persisted = productDao.save(menuGroup);

        Product result = productDao.findById(persisted.getId()).get();

        assertThat(result).isEqualToComparingFieldByField(persisted);
    }

    @Test
    @DisplayName("저장되어있지 않은 엔티티를 조회하면 빈 optional 객체가 반환된다")
    void findNotExist() {
        assertThat(productDao.findById(0L)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("모든 엔티티를 조회하면 저장되어 있는 엔티티들이 반환된다")
    void findAll() {
        productDao.save(createProduct(null, "강정치킨", BigDecimal.ONE));
        productDao.save(createProduct(null, "땅땅치킨", BigDecimal.ONE));
        productDao.save(createProduct(null, "정강치킨", BigDecimal.ONE));

        assertThat(productDao.findAll()).hasSize(3);
    }
}