package kitchenpos.application;

import fixture.ProductBuilder;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductServiceTest extends ServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductDao productDao;

    @Test
    void 제품을_저장한다() {
        Product product = ProductBuilder.init()
                .build();

        Product createdProduct = productService.create(product);

        assertThat(createdProduct.getId()).isNotNull();
    }

    @Test
    void 제품_가격이_음수면_예외를_발생한다() {
        Product product = ProductBuilder.init()
                .price(-100L)
                .build();

        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_제품을_조회한다() {
        List<Product> expected = productDao.findAll();

        List<Product> actual = productService.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
