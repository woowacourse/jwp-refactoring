package kitchenpos.application;

import static kitchenpos.Fixture.PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
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

    @DisplayName("제품을 생성한다.")
    @Test
    void create() {
        //given
        given(productDao.save(any(Product.class)))
                .willReturn(PRODUCT);

        //when
        ProductCreateRequest dto = new ProductCreateRequest("강정치킨", BigDecimal.valueOf(17000));
        ProductResponse savedProduct = productService.create(dto);

        //then
        assertAll(
                () -> assertThat(savedProduct.getId()).isEqualTo(PRODUCT.getId()),
                () -> assertThat(savedProduct.getName()).isEqualTo(PRODUCT.getName()),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(PRODUCT.getPrice())
        );
    }

    @DisplayName("제품 목록을 조회한다.")
    @Test
    void list() {
        //given
        given(productDao.findAll())
                .willReturn(List.of(PRODUCT));

        //when
        List<ProductResponse> products = productService.list();

        //then
        assertThat(products).hasSize(1);
    }
}
