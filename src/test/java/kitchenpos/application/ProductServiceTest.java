package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductDao productDao;

    @Test
    void 상품을_생성한다() {
        // given
        Product product = new Product();
        product.setName("짜장면");
        product.setPrice(BigDecimal.valueOf(8000.0));

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(productDao.findById(savedProduct.getId())).isPresent();
    }

    @Test
    void list() {
        // given
        Product product = new Product();
        product.setName("짜장면");
        product.setPrice(BigDecimal.valueOf(8000.0));
        Product savedProduct = productService.create(product);

        // when
        List<Product> result = productService.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(savedProduct));
    }
}
