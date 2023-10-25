package kitchenpos.ui;

import static kitchenpos.fixture.OrderTableFixture.손님_정보;
import static kitchenpos.fixture.OrderTableFixture.주문_태이블_손님_수_변경_요청;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_빈자리_변경_요청;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repositroy.OrderTableRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableRestControllerTest extends ControllerTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 주문_테이블을_등록한다() {
        // given
        final OrderTableCreateRequest request = 주문_테이블_생성_요청(10, false);
        final RequestSpecification given = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request);

        // when
        final ExtractableResponse<Response> result = given
                .when().log().all()
                .post("/api/tables")
                .then().extract();
        final OrderTableResponse response = result.as(OrderTableResponse.class);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.statusCode()).isEqualTo(201);
            softly.assertThat(response.getId()).isNotNull();
            softly.assertThat(response.getTableGroupId()).isNull();
            softly.assertThat(response.isEmpty()).isFalse();
            softly.assertThat(response.getNumberOfGuests()).isEqualTo(10);
        });
    }

    @Test
    void 주문_테이블을_전체_조회한다() {
        // given
        orderTableRepository.save(주문_테이블(손님_정보(10, false)));
        orderTableRepository.save(주문_테이블(손님_정보(5, false)));

        // when
        final ExtractableResponse<Response> result = RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();

        final List<OrderTableResponse> response = result
                .jsonPath()
                .getList(".", OrderTableResponse.class);

        // then
        assertThat(response).hasSize(2);
    }

    @Test
    void 주문이_없는_주문_테이블을_빈_상태로_변경한다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블(손님_정보(10, false)));
        final OrderTableChangeEmptyRequest request = 주문_테이블_빈자리_변경_요청(true);

        final RequestSpecification given = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request);

        // when
        final ExtractableResponse<Response> result = given
                .when().log().all()
                .put("/api/tables/{orderTableId}/empty", orderTable.getId())
                .then().log().all().extract();
        final OrderTableResponse response = result.as(OrderTableResponse.class);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.statusCode()).isEqualTo(200);
            softly.assertThat(response.isEmpty()).isTrue();
        });
    }

    @Test
    void 주문_테이블의_손님_수를_변경한다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블(손님_정보(1, false)));
        final OrderTableChangeNumberOfGuestsRequest request = 주문_태이블_손님_수_변경_요청(10);

        final RequestSpecification given = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request);

        // when
        final ExtractableResponse<Response> result = given
                .when().log().all()
                .put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
                .then().log().all().extract();
        final OrderTableResponse response = result.as(OrderTableResponse.class);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.statusCode()).isEqualTo(200);
            softly.assertThat(response.getNumberOfGuests()).isEqualTo(10);
        });
    }

    @Test
    void 주문_테이블의_손님_수를_음수로_변경하면_예외가_발생한다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블(손님_정보(1, false)));
        final OrderTableChangeNumberOfGuestsRequest request = 주문_태이블_손님_수_변경_요청(-1);

        final RequestSpecification given = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request);

        // when
        final ExtractableResponse<Response> result = given
                .when().log().all()
                .put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
                .then().log().all().extract();

        // then
        assertThat(result.statusCode()).isEqualTo(500);

    }

    @Test
    void 이미_빈_테이블의_주문_테이블의_손님_수를_변경하면_예외가_발생한다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블(손님_정보(1, true)));
        final OrderTableChangeNumberOfGuestsRequest request = 주문_태이블_손님_수_변경_요청(11);

        final RequestSpecification given = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request);

        // when
        final ExtractableResponse<Response> result = given
                .when().log().all()
                .put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
                .then().log().all().extract();

        // then
        assertThat(result.statusCode()).isEqualTo(500);
    }
}
