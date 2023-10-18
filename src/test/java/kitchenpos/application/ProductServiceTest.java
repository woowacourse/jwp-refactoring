package kitchenpos.application;

import kitchenpos.application.fixture.ProductServiceFixture;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ProductServiceFixture {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductDao productDao;

    @Test
    void 상품을_등록한다() {
        // given
        given(productDao.save(any(Product.class))).willReturn(저장된_상품);

        final Product product = new Product(상품_이름, 상품_가격);

        // when
        final Product actual = productService.create(product);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual).usingRecursiveComparison()
                          .isEqualTo(저장된_상품);
        });
    }

    @ParameterizedTest
    @NullSource
    void 상품_등록시_가격이_null이라면_예외를_반환한다(BigDecimal price) {
        // given
        final Product product = new Product(상품_이름, price);

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_등록시_가격이_음수라면_예외를_반환한다() {
        // given
        final Product product = new Product(상품_이름, 상품_가격이_음수);

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_목록을_조회한다() {
        // given
        given(productDao.findAll()).willReturn(저장된_상품들);

        // when
        final List<Product> actual = productService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).usingRecursiveComparison()
                          .isEqualTo(저장된_상품1);
            softAssertions.assertThat(actual.get(1)).usingRecursiveComparison()
                          .isEqualTo(저장된_상품2);
        });
    }
}
