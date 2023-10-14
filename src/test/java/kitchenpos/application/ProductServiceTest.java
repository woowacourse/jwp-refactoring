package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class ProductServiceTest extends ServiceTest{

    @Autowired
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp(){
        product = makeProduct();
    }
    @Test
    void 상품을_생성한다() {
        Mockito.when(productDao.save(any(Product.class)))
                .thenReturn(product);

        Product saved = productService.create(product);
        assertThat(saved.getName()).isEqualTo(product.getName());
    }

    @Test
    void 전체_상품_목록을_조회한다() {
        Mockito.when(productDao.findAll())
                .thenReturn(List.of(makeProduct(), makeProduct()));

        assertThat(productService.list().size()).isEqualTo(2);
    }

    private Product makeProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("상품1");
        product.setPrice(BigDecimal.ONE);
        return product;
    }
}
