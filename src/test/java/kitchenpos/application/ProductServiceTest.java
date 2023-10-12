package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("제품 생성 테스트")
    public void createProductTest() {
        //given
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));
        given(productDao.save(any(Product.class))).willReturn(product);

        //when
        Product createdProduct = productService.create(product);

        //then
        assertThat(createdProduct).isEqualTo(product);
    }

    @Test
    @DisplayName("제품 생성 실패 테스트 (가격이 없음)")
    public void createProductWithoutPriceTest() {
        //given
        Product product = new Product();
        product.setName("Test Product");

        //when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("제품 목록 조회 테스트")
    public void listProductsTest() {
        //given
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));
        given(productDao.findAll()).willReturn(Collections.singletonList(product));

        //when
        List<Product> products = productService.list();

        //then
        assertThat(products).containsExactly(product);
    }
}
