package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
    }

    @Test
    @DisplayName("제품 생성 테스트")
    public void createProductTest() {
        //given
        given(productDao.save(any(Product.class))).willReturn(product);

        //when
        Product createdProduct = productService.create(product);

        //then
        assertThat(createdProduct).isEqualTo(product);
    }

    @Test
    @DisplayName("제품 목록 조회 테스트")
    public void listProductsTest() {
        //given
        given(productDao.findAll()).willReturn(List.of(product));

        //when
        List<Product> products = productService.list();

        //then
        assertThat(products).containsExactly(product);
    }
}
