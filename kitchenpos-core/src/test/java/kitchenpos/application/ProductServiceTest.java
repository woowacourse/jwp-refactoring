package kitchenpos.application;

import kitchenpos.application.fixture.ProductServiceFixture;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.CreateProductRequest;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
class ProductServiceTest extends ProductServiceFixture {

    @Autowired
    private ProductService productService;

    @Nested
    class 상품_등록 {

        @Test
        void 상품을_등록한다() {
            final CreateProductRequest 등록_요청_상품_dto = new CreateProductRequest("치킨", BigDecimal.valueOf(10_000));

            final Product actual = productService.create(등록_요청_상품_dto);

            assertThat(actual.getId()).isPositive();
        }

        @Test
        void 상품_가격이_입력되지_않은_경우_예외가_발생한다() {
            final CreateProductRequest 가격이_입력되지_않은_상품 = new CreateProductRequest("가격이 입력되지 않은 상품", null);

            assertThatThrownBy(() -> productService.create(가격이_입력되지_않은_상품))
                      .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품_가격이_0_보다_작은_경우_예외가_발생한다() {
            final CreateProductRequest 가격이_0보다_작은_상품 = new CreateProductRequest("가격이 0보다 작은 상품", BigDecimal.valueOf(-1));

            assertThatThrownBy(() -> productService.create(가격이_0보다_작은_상품))
                      .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 상품_조회 {

        @Test
        void 모든_상품을_조회한다() {
            모든_상품을_조회한다_픽스처_생성();

            final List<Product> actual = productService.list();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.get(0).getName()).isEqualTo("치킨");
                softAssertions.assertThat(actual.get(1).getName()).isEqualTo("피자");
                softAssertions.assertThat(actual.get(2).getName()).isEqualTo("마라탕");

            });
        }
    }
}
