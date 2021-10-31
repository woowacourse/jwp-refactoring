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
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixtures.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록 할 수 있다.")
    @Test
    void testCreateProduct() {
        //given
        given(productDao.save(any())).willReturn(맛있는뿌링클());

        //when
        Product actual = productService.create(맛있는뿌링클());

        //then
        assertThat(actual.getId()).isEqualTo(1L);
    }

    @DisplayName("상품의 가격은 존재해야하고, 음수가 아니어야 한다.")
    @Test
    void testCreateProductPriceNegative() {
        //given
        Product product = new Product();
        product.setPrice(new BigDecimal(-1));

        //when, then
        assertThatThrownBy(() -> {
            productService.create(product);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 리스트 조회할 수 있다.")
    @Test
    void testList() {
        //given
        given(productDao.findAll()).willReturn(Arrays.asList(맛있는뿌링클(), 알리오갈리오(), 쫀득쫀득치즈볼(),
                시원한콜라(), 치킨무(), 아메리카노()));

        //when
        List<Product> actual = productService.list();

        //then
        verify(productDao, times(1)).findAll();
        assertThat(actual).hasSize(6);
    }
}
