package kitchenpos.application;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Proudct 비즈니스 흐름 테스트")
@Transactional
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품 저장 - 성공")
    @CustomParameterizedTest
    @CsvSource(value = {"후라이드치킨, 16000", "후라이드치킨, 16000", "간장치킨, 17000"}, delimiter = ',')
    void create(@AggregateWith(ProductAggregator.class) Product expect) {
        //given
        //when
        final Product actual = productService.create(expect);
        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(expect.getName());
        assertThat(actual.getPrice()).isCloseTo(expect.getPrice(), Percentage.withPercentage(0));
    }

    @DisplayName("상품 저장 - 실패 - 상품이 가격이 null")
    @CustomParameterizedTest
    @ValueSource(strings = {"후라이드치킨", "후라이드치킨", "간장치킨"})
    void createFailureWhenNullPrice(@ConvertWith(ProductConverter.class) Product product) {
        //given
        //when
        //then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 저장 - 실패 - 상품이 가격이 음수")
    @CustomParameterizedTest
    @CsvSource(value = {"후라이드치킨, -1", "후라이드치킨, -0.1", "간장치킨, -0.999"}, delimiter = ',')
    void createFailure(@ConvertWith(ProductConverter.class) Product product) {
        //given
        //when
        //then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 조회 - 성공 - 전체 상품 조회")
    @Test
    void findAll() {
        //given
        //when
        final List<Product> list = productService.list();
        //then
        assertThat(list).extracting(Product::getName)
                .containsAnyElementsOf(ProductFixture.productsName());
    }

    static class ProductConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertThat(targetType).isEqualTo(Product.class);
            final Product product = new Product();
            product.setName(source.toString());
            return product;
        }
    }

    static class ProductAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            final Product product = new Product();
            product.setName(accessor.getString(0));
            product.setPrice(BigDecimal.valueOf(accessor.getLong(1)));
            return product;
        }
    }
}