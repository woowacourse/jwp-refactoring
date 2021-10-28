package kitchenpos.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

import static kitchenpos.application.Fixtures.PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("상품 등록")
    void createTest() {

        // when
        final Product product = productService.create(PRODUCT);

        // then
        assertThat(productDao.findById(1L).get()).isEqualTo(product);
    }

    @Test
    @DisplayName("상품 목록 조회")
    void listTest() {

        // given
        final Product product = productService.create(PRODUCT);

        // when
        final List<Product> products = productService.list();

        // then
        assertThat(products).contains(product);
    }
}
