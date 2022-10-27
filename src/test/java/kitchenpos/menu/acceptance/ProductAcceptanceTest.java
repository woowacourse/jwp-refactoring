package kitchenpos.menu.acceptance;

import static kitchenpos.support.DomainFixture.뿌링클;
import static kitchenpos.support.ProductRestAssuredFixture.상품_목록_조회_요청;
import static kitchenpos.support.ProductRestAssuredFixture.상품_생성_요청;
import static kitchenpos.support.SimpleRestAssured.toObjectList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.menu.ui.dto.ProductCreateRequest;
import kitchenpos.menu.ui.dto.ProductResponse;
import kitchenpos.support.AcceptanceTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    void 상품_생성을_요청한다() {
        // given
        final var request = new ProductCreateRequest(
                뿌링클.getName(),
                뿌링클.getPrice().intValue()
        );

        // when
        final var response = 상품_생성_요청(request);

        // then
        final var created = response.as(ProductResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location").split("/products/")[1]).isNotNull(),
                () -> assertThat(created.getId()).isNotNull(),
                () -> assertThat(created.getName()).isEqualTo(request.getName()),
                () -> assertThat(created.getPrice().intValue()).isEqualTo(request.getPrice())
        );
    }

//    @Test
//    void 상품_생성_요청시_이름이_없으면_BAD_REQUEST를_응답한다() {
//        // given
//        final var request = new ProductCreateRequest(
//                null,
//                뿌링클.getPrice().intValue()
//        );
//
//        // when
//        final var response = 상품_생성_요청(request);
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//    }

    @Test
    void 상품_목록_조회를_요청한다() {
        // given
        final var request = new ProductCreateRequest(
                뿌링클.getName(),
                뿌링클.getPrice().intValue()
        );
        상품_생성_요청(request);
        상품_생성_요청(request);

        // when
        final var response = 상품_목록_조회_요청();

        // then
        final var found = toObjectList(response, ProductResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(found).hasSize(2)
        );
    }
}
