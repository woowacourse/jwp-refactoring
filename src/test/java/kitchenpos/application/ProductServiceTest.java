package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.application.mapper.ProductMapper;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("제품을 성공적으로 생성한다")
    void testCreateSuccess() {
        //given
        final Product product = new Product(1L, "test", BigDecimal.valueOf(1000));

        final ProductCreateRequest productCreateRequest = new ProductCreateRequest("test", BigDecimal.valueOf(1000));

        when(productDao.save(any()))
                .thenReturn(product);

        //when
        final ProductResponse result = productService.create(productCreateRequest);

        //then
        assertThat(result).isEqualTo(ProductMapper.mapToResponse(product));
    }

    @Test
    @DisplayName("제품 생성 시 가격이 null일 경우 예외가 발생한다")
    void testCreateWhenPriceNullFailure() {
        //given
        final ProductCreateRequest productCreateRequest = new ProductCreateRequest("test", null);

        //when
        //then
        assertThatThrownBy(() -> productService.create(productCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("제품 생성 시 가격이 0보다 작을 경우 예외가 발생한다")
    void testCreateWhenPriceLowerThanZeroFailure() {
        //given
        final ProductCreateRequest productCreateRequest = new ProductCreateRequest("test", BigDecimal.valueOf(-1));

        //when
        //then
        assertThatThrownBy(() -> productService.create(productCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("제품 리스트를 성공적으로 조회한다")
    void testListSuccess() {
        //given
        final Product product1 = new Product(1L, "test1", BigDecimal.valueOf(1000));
        final Product product2 = new Product(2L, "test2", BigDecimal.valueOf(1000));
        final Product product3 = new Product(3L, "test3", BigDecimal.valueOf(1000));

        when(productDao.findAll())
                .thenReturn(List.of(product1, product2, product3));

        //when
        final List<ProductResponse> results = productService.list();

        //then
        final List<ProductResponse> expected = Stream.of(product1, product2, product3)
                .map(ProductMapper::mapToResponse)
                .collect(Collectors.toList());
        assertThat(results).isEqualTo(expected);
    }
}
