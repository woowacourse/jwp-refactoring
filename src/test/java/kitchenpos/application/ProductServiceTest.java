package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    void 상품_이름과_가격을_받아서_상품정보를_등록할_수_있다() {
        //given
        ProductDto productRequest = new ProductDto("강정치킨", BigDecimal.valueOf(17000));

        Product savedProduct = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        given(productDao.save(any(Product.class))).willReturn(savedProduct);

        //when
        ProductDto result = productService.create(productRequest);

        //then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("강정치킨");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(17000));
    }

    @Test
    void 상품_가격이_입력되지_않으면_예외처리한다() {
        //given
        ProductDto productRequest = new ProductDto("강정치킨", null);

        //when, then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_가격이_0_미만이면_예외처리한다() {
        //given
        ProductDto productRequest = new ProductDto("강정치킨", BigDecimal.valueOf(-1000));

        //when, then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록된_전체_상품_정보를_조회할_수_있다() {
        //given
        Product savedProduct1 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        Product savedProduct2 = new Product(2L, "양념치킨", BigDecimal.valueOf(20000));
        given(productDao.findAll())
                .willReturn(List.of(savedProduct1, savedProduct2));

        //when
        List<ProductDto> result = productService.list();

        //then
        assertThat(result).hasSize(2);
    }
}
