package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductService productService;
    @Mock
    private ProductDao productDao;
    private Product product = new Product();

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
        product.setId(1L);
        product.setName("product");
        product.setPrice(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("상품을 생성할 수 있어야 한다.")
    void create() {
        given(productDao.save(any())).willReturn(product);

        Product createdProduct = productService.create(product);

        assertThat(createdProduct.getId()).isEqualTo(product.getId());
        assertThat(createdProduct.getName()).isEqualTo(product.getName());
        assertThat(createdProduct.getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    @DisplayName("상품 목록을 불러올 수 있어야 한다.")
    void list() {
        Product product1 = new Product();
        Product product2 = new Product();
        product1.setName("product1");
        product2.setName("product2");
        product1.setPrice(BigDecimal.valueOf(1000));
        product2.setPrice(BigDecimal.valueOf(1000));
        List<Product> products = Arrays.asList(product1, product2);

        given(productDao.findAll()).willReturn(products);

        List<Product> savedProducts = productService.list();

        assertThat(savedProducts.size()).isEqualTo(products.size());
        assertThat(savedProducts.get(0)).isEqualTo(products.get(0));
    }
}
