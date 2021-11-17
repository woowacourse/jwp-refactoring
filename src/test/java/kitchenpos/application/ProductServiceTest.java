package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        //given
        Product product = new Product.ProductBuilder()
                            .setId(1L)
                            .setName("떡볶이")
                            .setPrice(new BigDecimal(3500))
                            .build();

        given(productDao.save(product))
                .willReturn(product);

        //when
        Product actual = productService.create(product);
        //then
        assertThat(actual).isEqualTo(product);
    }

    @DisplayName("상품을 등록 실패 - 유효하지 않은 가격일 경우")
    @Test
    void createFailInvalidPrice() {
        //given
        Product product = new Product.ProductBuilder()
                            .setId(1L)
                            .setName("떡볶이")
                            .setPrice(new BigDecimal(-3500))
                            .build();

        //when
        //then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 가격입니다.");
    }
}