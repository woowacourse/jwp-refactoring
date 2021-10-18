package kitchenpos.application;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("product 생성")
    @Test
    void create() {
        Product product = new Product();
        product.setId(1L);
        product.setName("후라이드치킨");
        product.setPrice(BigDecimal.valueOf(16000.00));

        productService.create(product);

        verify(productDao).save(product);
    }

    @DisplayName("product 불러오기")
    @Test
    void list() {
        productService.list();

        verify(productDao).findAll();
    }

}
