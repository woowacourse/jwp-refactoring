package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ProductDao 는 ")
@SpringTestWithData
class ProductDaoTest {

    @Autowired
    ProductDao productDao;

    @DisplayName("상품을 저장한다.")
    @Test
    void saveProduct() {
        final Product product = productDao.save(new Product("productName", BigDecimal.valueOf(1000L)));

        assertAll(
                () -> assertThat(product.getId()).isGreaterThanOrEqualTo(1L),
                () -> assertThat(product.getName()).isEqualTo("productName"),
                () -> assertThat(product.getPrice().longValue()).isEqualTo(1000L)
        );
    }

    @DisplayName("특정 상품을 조회한다.")
    @Test
    void findById() {
        final Product product = productDao.save(new Product("productName", BigDecimal.valueOf(1000L)));

        final Product actual = productDao.findById(product.getId())
                .get();

        assertThat(actual.getId()).isEqualTo(product.getId());
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void getProducts() {
        final Product product = productDao.save(new Product("productName", BigDecimal.valueOf(1000L)));

        final List<Product> products = productDao.findAll();

        assertAll(
                () -> assertThat(products.size()).isEqualTo(1),
                () -> assertThat(products.get(0).getId()).isEqualTo(product.getId())
        );
    }
}
