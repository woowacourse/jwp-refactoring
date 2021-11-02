package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ProductService 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private final ProductFixture productFixture = new ProductFixture();

    @Test
    @DisplayName("상품 생성 테스트 - 성공")
    public void createTest() throws Exception {
        // given
        Product 치킨 = productFixture.상품_생성("치킨", BigDecimal.TEN);
        Product expected = productFixture.상품_생성(1L, "치킨", BigDecimal.TEN);
        given(productDao.save(치킨)).willReturn(expected);

        // when
        Product actual = productService.create(치킨);

        // then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("상품 리스트 조회 테스트 - 성공")
    public void listTest() throws Exception {
        // given
        Product 치킨 = productFixture.상품_생성("치킨", BigDecimal.TEN);
        Product 피자 = productFixture.상품_생성("피자", BigDecimal.valueOf(20));
        List<Product> expected = productFixture.상품_리스트_생성(치킨, 피자);
        given(productDao.findAll()).willReturn(expected);

        // when
        List<Product> actual = productService.list();

        // then
        assertIterableEquals(expected, actual);
    }
}
