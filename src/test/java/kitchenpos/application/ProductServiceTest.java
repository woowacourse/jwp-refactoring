package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class ProductServiceTest {
    @MockBean
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("Product를 저장할 수 있다.")
    void create() {
        // given
        Product product = new Product();
        product.setId(1L);
        product.setName("product");
        product.setPrice(BigDecimal.valueOf(10000L));

        given(productDao.save(any(Product.class)))
                .willReturn(product);

        // when
        Product actual = productService.create(product);

        // then
        assertThat(actual).isEqualTo(product);
    }

    @Test
    @DisplayName("저장하려는 상품의 가격이 0보다 작으면 예외를 발생시킨다.")
    void negativePriceWillReturnException() {
        // given
        Product product = new Product();
        product.setId(1L);
        product.setName("product");
        product.setPrice(BigDecimal.valueOf(-10000L));

        given(productDao.save(any(Product.class)))
                .willReturn(product);

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장하려는 상품의 가격이 null일 경우 예외를 발생시킨다.")
    void nullPriceWillReturnException() {
        // given
        Product product = new Product();
        product.setId(1L);
        product.setName("product");
        product.setPrice(null);

        given(productDao.save(any(Product.class)))
                .willReturn(product);

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록된 상품들을 불러올 수 있다.")
    void list() {
        // given
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("product1");
        product1.setPrice(BigDecimal.valueOf(10000L));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("product2");
        product2.setPrice(BigDecimal.valueOf(10000L));

        List<Product> expected = Arrays.asList(product1, product2);
        given(productDao.findAll())
                .willReturn(expected);

        // when
        List<Product> actual = productService.list();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
