package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void create() {
    }

    @DisplayName("상품의 가격은 0 원 이상이어야 한다")
    @Test
    void createExceptionPriceUnderZero() {
    }

    @DisplayName("상품의 목록을 조회할 수 있다")
    @Test
    void list() {

    }
}
