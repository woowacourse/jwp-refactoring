package kitchenpos.product.application;

import kitchenpos.MockServiceTest;
import kitchenpos.product.application.dto.CreateProductDto;
import kitchenpos.product.application.dto.ProductDto;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.exception.ProductPriceException;
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
    private ProductRepository productRepository;

    @Test
    void 상품_목록을_조회한다() {
        // given
        ProductDto expectedFirstProduct = new ProductDto(
                1L,
                "pizza",
                BigDecimal.valueOf(18000L));
        ProductDto expectedSecondProduct = new ProductDto(
                2L,
                "chicken",
                BigDecimal.valueOf(21000L));

        List<ProductDto> expected = List.of(
                expectedFirstProduct,
                expectedSecondProduct
        );

        Product mockFirstProduct = new Product(
                1L,
                new ProductName("pizza"),
                new ProductPrice(BigDecimal.valueOf(18000L)));
        Product mockSecondProduct = new Product(
                2L,
                new ProductName("chicken"),
                new ProductPrice(BigDecimal.valueOf(21000L)));

        BDDMockito.given(productRepository.findAll())
                .willReturn(List.of(mockFirstProduct, mockSecondProduct));

        // when
        List<ProductDto> actual = productService.list();

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 상품을_추가한다() {
        // given
        ProductDto expected = new ProductDto(
                1L,
                "pizza",
                BigDecimal.valueOf(18000L));

        CreateProductDto createProductDto = new CreateProductDto(
                "pizza",
                BigDecimal.valueOf(18000L));

        Product mockReturnProduct = new Product(
                1L,
                new ProductName("pizza"),
                new ProductPrice(BigDecimal.valueOf(18000L)));

        BDDMockito.given(productRepository.save(BDDMockito.any(Product.class)))
                .willReturn(mockReturnProduct);

        // when
        ProductDto actual = productService.create(createProductDto);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 상품을_추가할_때_가격이_0_일_수_있다() {
        // given
        CreateProductDto createProductDto = new CreateProductDto(
                "pizza",
                BigDecimal.valueOf(0L));
        BDDMockito.given(productRepository.save(BDDMockito.any(Product.class)))
                .willReturn(new Product(
                        1L,
                        new ProductName("pizza"),
                        new ProductPrice(BigDecimal.valueOf(0L))));

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> productService.create(createProductDto));
    }

    @Test
    void 상품을_추가할_때_이름이_공백_일_수_있다() {
        // given
        CreateProductDto createProductDto = new CreateProductDto(
                "",
                BigDecimal.valueOf(1000L));
        BDDMockito.given(productRepository.save(BDDMockito.any(Product.class)))
                .willReturn(new Product(
                        1L,
                        new ProductName(""),
                        new ProductPrice(BigDecimal.valueOf(1000L))));

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> productService.create(createProductDto));
    }

    @Test
    void 상품을_추가할_때_가격이_null_이면_예외를_던진다() {
        // given
        CreateProductDto createProductDto = new CreateProductDto(
                "pizza",
                null);

        // when, then
        Assertions.assertThatThrownBy(() -> productService.create(createProductDto))
                .isInstanceOf(ProductPriceException.class);
    }

    @Test
    void 상품을_추가할_때_가격이_음수면_예외를_던진다() {
        // given
        CreateProductDto createProductDto = new CreateProductDto(
                "pizza",
                BigDecimal.valueOf(-100L));

        // when, then
        Assertions.assertThatThrownBy(() -> productService.create(createProductDto))
                .isInstanceOf(ProductPriceException.class);
    }
}
