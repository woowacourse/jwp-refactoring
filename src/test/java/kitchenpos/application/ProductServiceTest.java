package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.ProductCreateDto;
import kitchenpos.domain.Product;
import kitchenpos.domain.exception.ProductException.NoPriceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_등록할_수_있다() {
        // given
        final ProductCreateDto productCreateDto = new ProductCreateDto("테스트 상품",
            new BigDecimal(2000));

        // when
        final Product result = productService.create(productCreateDto);

        // then
        assertThat(result.getName()).isEqualTo(productCreateDto.getName());
        assertThat(result.getPrice()).isEqualByComparingTo(productCreateDto.getPrice());
    }

    @NullSource
    @ValueSource(strings = {"-1"})
    @ParameterizedTest
    void 상품가격이_없거나_0보다_작을경우_예외가_발생한다(final BigDecimal price) {
        // given
        final ProductCreateDto productCreateDto = new ProductCreateDto("테스트 상품", price);

        // given when then
        assertThatThrownBy(() -> productService.create(productCreateDto))
            .isInstanceOf(NoPriceException.class);
    }

    @Test
    void 상품조회를_할_수_있다() {
        // given
        final ProductCreateDto productCreateDto = new ProductCreateDto("테스트 상품",
            new BigDecimal(2000));
        productService.create(productCreateDto);

        // when
        final List<Product> results = productService.list();

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo(productCreateDto.getName());
        assertThat(results.get(0).getPrice()).isEqualByComparingTo(productCreateDto.getPrice());
    }
}
