//package kitchenpos.application;
//
//import static kitchenpos.fixtures.ProductFixture.PRODUCT;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import java.math.BigDecimal;
//import java.util.List;
//import kitchenpos.domain.Product;
//import org.junit.jupiter.api.DisplayNameGeneration;
//import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.NullSource;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
//
//@SuppressWarnings("NonAsciiCharacters")
//@DisplayNameGeneration(ReplaceUnderscores.class)
//@Sql("/truncate.sql")
//@SpringBootTest
//class ProductServiceTest {
//
//    @Autowired
//    private ProductService productService;
//
//    @Test
//    void 상품을_생성한다() {
//        // given
//        final Product product = PRODUCT();
//
//        // when
//        final Product actual = productService.create(product);
//
//        // then성
//        final List<Product> products = productService.list();
//        assertThat(products).usingRecursiveFieldByFieldElementComparatorIgnoringFields()
//                            .contains(actual);
//    }
//
//    @ParameterizedTest(name = "가격이 {0}일때, 예외")
//    @NullSource
//    @ValueSource(ints = {-1, -2})
//    void 상품을_생성할_때_가격이_null_이거나_음수라면_예외가_발생한다(final Integer price) {
//        // given
//        Product product;
//
//        if (price == null) {
//            product = PRODUCT(null);
//        } else {
//            product = PRODUCT(BigDecimal.valueOf(price));
//        }
//
//        // when & then
//        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 상품에_대해_전체_조회한다() {
//        // given
//        final Product product = PRODUCT();
//        productService.create(product);
//
//        // when
//        final List<Product> products = productService.list();
//
//        // then
//        assertThat(products).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "price")
//                            .contains(product);
//    }
//}