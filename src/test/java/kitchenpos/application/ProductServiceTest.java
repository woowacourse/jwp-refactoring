package kitchenpos.application;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import kitchenpos.Fixtures;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @DisplayName("product 생성")
    @Test
    void create() {
        Product product = Fixtures.makeProduct();

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
