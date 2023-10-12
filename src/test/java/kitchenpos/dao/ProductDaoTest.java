package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProductDaoTest extends DaoTest {

    @Autowired
    ProductDao productDao;

    @DisplayName("Product 저장 - 성공")
    @Test
    void save_Success() {
        // given && when
        final Product saveProduct = saveProduct("상품", 300);

        // then
        assertThat(saveProduct.getId()).isNotNull();
    }

    @DisplayName("Product ID로 Product 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnProduct() {
        // given
        final Product saveProduct = saveProduct("상품", 300);

        // when
        final Product foundProduct = productDao.findById(saveProduct.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 Product가 없습니다."));

        // then
        assertThat(foundProduct.getId()).isEqualTo(saveProduct.getId());
    }

    @DisplayName("Product ID로 Product 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        // given
        final Product saveProduct = saveProduct("상품", 300);

        // when
        final Optional<Product> foundProduct = productDao.findById(saveProduct.getId() + 1);

        // then
        assertThat(foundProduct.isPresent()).isFalse();
    }

    @DisplayName("전체 Product 조회 - 성공")
    @Test
    void findAll_Success() {
        // given
        final Product saveProduct1 = saveProduct("상품1", 300);
        final Product saveProduct2 = saveProduct("상품2", 300);

        // when
        final List<Product> products = productDao.findAll();

        // then
        assertThat(products).hasSize(2);
    }
}
