package com.kitchenpos.ui.table;

import com.kitchenpos.application.TableService;
import com.kitchenpos.application.dto.OrderTableChangeNumberOfGuestRequest;
import com.kitchenpos.application.dto.OrderTableCreateRequest;
import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.domain.OrderTableRepository;
import com.kitchenpos.helper.IntegrationTestHelper;
import com.kitchenpos.ui.dto.OrderTableResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static com.kitchenpos.fixture.OrderTableFixture.주문_테이블_생성_요청;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class TableRestControllerAcceptanceTestFixture extends IntegrationTestHelper {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    protected OrderTable 주문_테이블;

    @BeforeEach
    void setup() {
        주문_테이블 = orderTableRepository.save(주문_테이블_생성(10, false));
    }

    protected <T> ExtractableResponse 주문_테이블을_생성한다(final String url, final T request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    protected void 주문_테이블이_성공적으로_생성된다(final ExtractableResponse response, final OrderTable orderTable) {
        OrderTableResponse result = response.as(OrderTableResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            softly.assertThat(result.isEmpty()).isEqualTo(orderTable.isEmpty());
        });
    }

    protected ExtractableResponse 주문_테이블을_전체_조회한다(final String url) {
        return RestAssured.given().log().all()
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }

    protected void 주문_테이블들이_성공적으로_조회된다(final ExtractableResponse response, final OrderTable orderTable) {
        List<OrderTableResponse> result = response.jsonPath()
                .getList("", OrderTableResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(2);
            softly.assertThat(result.get(0).getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            softly.assertThat(result.get(0).isEmpty()).isEqualTo(orderTable.isEmpty());
        });
    }

    protected OrderTable 주문_테이블_데이터를_생성한다() {
        OrderTable orderTable = 주문_테이블_생성(10, false);
        OrderTableCreateRequest req = 주문_테이블_생성_요청(orderTable);

        return tableService.create(req);
    }

    protected <T> ExtractableResponse 주문_테이블의_상태를_변경한다(final String url, final T request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .put(url)
                .then().log().all()
                .extract();
    }

    protected void 주문_테이블의_상태가_성공적으로_변경된다(final ExtractableResponse response, final OrderTable orderTable) {
        OrderTableResponse result = response.as(OrderTableResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(orderTable.getId());
            softly.assertThat(result.isEmpty()).isEqualTo(!orderTable.isEmpty());
        });
    }

    protected <T> ExtractableResponse 주문_테이블의_손님_수를_변경한다(final String url, final T request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .put(url)
                .then().log().all()
                .extract();
    }

    protected void 주문_테이블의_손님_수가_성공적으로_변경된다(final ExtractableResponse response, final OrderTable orderTable, OrderTableChangeNumberOfGuestRequest req) {
        OrderTableResponse result = response.as(OrderTableResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(orderTable.getId());
            softly.assertThat(result.getNumberOfGuests()).isEqualTo(req.getNumberOfGuests());
        });
    }
}
