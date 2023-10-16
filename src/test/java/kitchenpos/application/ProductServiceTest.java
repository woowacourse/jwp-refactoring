package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;

class ProductServiceTest extends MockServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @Test
    void 상품_목록을_조회한다() {
        // given
        Product expectedFirstProduct = new Product();
        expectedFirstProduct.setId(1L);
        expectedFirstProduct.setName("pizza");
        expectedFirstProduct.setPrice(BigDecimal.valueOf(18000L));

        Product expectedSecondProduct = new Product();
        expectedSecondProduct.setId(2L);
        expectedSecondProduct.setName("chicken");
        expectedSecondProduct.setPrice(BigDecimal.valueOf(21000L));

        List<Product> expected = List.of(
                expectedFirstProduct,
                expectedSecondProduct
        );

        Product mockFirstProduct = new Product();
        mockFirstProduct.setId(1L);
        mockFirstProduct.setName("pizza");
        mockFirstProduct.setPrice(BigDecimal.valueOf(18000L));

        Product mockSecondProduct = new Product();
        mockSecondProduct.setId(2L);
        mockSecondProduct.setName("chicken");
        mockSecondProduct.setPrice(BigDecimal.valueOf(21000L));

        BDDMockito.given(productDao.findAll())
                .willReturn(List.of(mockFirstProduct, mockSecondProduct));

        // when
        List<Product> actual = productService.list();

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 상품을_추가한다() {
        // given
        Product expected = new Product();
        expected.setId(1L);
        expected.setName("pizza");
        expected.setPrice(BigDecimal.valueOf(18000L));

        Product argumentProduct = new Product();
        argumentProduct.setName("pizza");
        argumentProduct.setPrice(BigDecimal.valueOf(18000L));

        Product mockReturnProduct = new Product();
        mockReturnProduct.setId(1L);
        mockReturnProduct.setName("pizza");
        mockReturnProduct.setPrice(BigDecimal.valueOf(18000L));

        BDDMockito.given(productDao.save(argumentProduct))
                .willReturn(mockReturnProduct);

        // when
        Product actual = productService.create(argumentProduct);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 상품을_추가할_때_가격이_0_일_수_있다() {
        // given
        Product argumentProduct = new Product();
        argumentProduct.setName("pizza");
        argumentProduct.setPrice(BigDecimal.valueOf(0L));

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> productService.create(argumentProduct));
    }

    @Test
    void 상품을_추가할_때_이름이_공백_일_수_있다() {
        // given
        Product argumentProduct = new Product();
        argumentProduct.setName("");
        argumentProduct.setPrice(BigDecimal.valueOf(1000L));

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> productService.create(argumentProduct));
    }

    @Test
    void 상품을_추가할_때_가격이_null_이면_예외를_던진다() {
        // given
        Product argumentProduct = new Product();
        argumentProduct.setName("pizza");
        argumentProduct.setPrice(null);

        // when, then
        Assertions.assertThatThrownBy(() -> productService.create(argumentProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품을_추가할_때_가격이_음수면_예외를_던진다() {
        // given
        Product argumentProduct = new Product();
        argumentProduct.setName("pizza");
        argumentProduct.setPrice(BigDecimal.valueOf(-100L));

        // when, then
        Assertions.assertThatThrownBy(() -> productService.create(argumentProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
