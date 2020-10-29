package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductDaoTest extends DaoTest {

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<Product> products = productDao.findAll();

        assertAll(
            () -> assertThat(products).hasSize(2),
            () -> assertThat(products.get(0)).usingRecursiveComparison().isEqualTo(PRODUCT_1),
            () -> assertThat(products.get(1)).usingRecursiveComparison().isEqualTo(PRODUCT_2)
        );
    }

    @DisplayName("단건조회 예외 테스트: id에 해당하는 제품이 존재하지 않을때")
    @Test
    void findByIdFailByNotExistTest() {
        Optional<Product> product = productDao.findById(-1L);

        assertThat(product).isEmpty();
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        Product product = productDao.findById(PRODUCT_ID_1).get();

        assertThat(product).usingRecursiveComparison().isEqualTo(PRODUCT_1);
    }
}