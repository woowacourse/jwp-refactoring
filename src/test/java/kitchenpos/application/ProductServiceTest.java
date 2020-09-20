package kitchenpos.application;

import static kitchenpos.domain.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("상품을 생성할 수 있어야 한다.")
    void create() {
        Product product = createProduct("product", BigDecimal.valueOf(1000));
        product.setId(1L);

        given(productDao.save(any())).willReturn(product);

        Product createdProduct = productService.create(product);

        assertAll(
            () -> assertThat(createdProduct.getId()).isEqualTo(product.getId()),
            () -> assertThat(createdProduct.getName()).isEqualTo(product.getName()),
            () -> assertThat(createdProduct.getPrice()).isEqualTo(product.getPrice())
        );
    }

    @Test
    @DisplayName("상품의 가격은 양수여야 한다.")
    void createFail() {
        Product product = createProduct("product", BigDecimal.valueOf(-1));
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 목록을 불러올 수 있어야 한다.")
    void list() {
        Product product1 = createProduct("product1", BigDecimal.valueOf(1000));
        Product product2 = createProduct("product2", BigDecimal.valueOf(1000));
        List<Product> products = Arrays.asList(product1, product2);

        given(productDao.findAll()).willReturn(products);

        List<Product> savedProducts = productService.list();

        assertThat(savedProducts.size()).isEqualTo(products.size());
    }
}
