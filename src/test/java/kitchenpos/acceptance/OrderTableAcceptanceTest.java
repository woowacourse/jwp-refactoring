package kitchenpos.acceptance;

import static kitchenpos.acceptance.support.RequestUtil.get;
import static kitchenpos.acceptance.support.RequestUtil.post;
import static kitchenpos.acceptance.support.RequestUtil.put;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

public class OrderTableAcceptanceTest extends AcceptanceTest {

    static ExtractableResponse<Response> 테이블을_생성한다(final int numberOfGuests, final boolean empty) {
        return post("/api/tables", Map.of(
                "numberOfGuests", 0,
                "empty", empty
        ));
    }

    static ExtractableResponse<Response> 모든_테이블을_조회한다() {
        return get("/api/tables");
    }

    static ExtractableResponse<Response> 테이블의_빈_여부를_변경한다(final Long id, final boolean empty) {
        return put("/api/tables/" + id + "/empty", Map.of(
                "empty", empty
        ));
    }

    static ExtractableResponse<Response> 테이블의_방문한_손님수를_변경한다(final Long id, final int numberOfGuests) {
        return put("/api/tables/" + id + "/number-of-guests", Map.of(
                "numberOfGuests", numberOfGuests
        ));
    }

    @DisplayName("주문 테이블을 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageOrderTable() {
        return Stream.of(
                dynamicTest("테이블을 생성한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 테이블을_생성한다(10, true);

                    // then
                    상태코드를_검증한다(response, HttpStatus.CREATED);
                    필드가_Null이_아닌지_검증한다(response, "id");
                }),
                dynamicTest("모든 테이블을 조회한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 모든_테이블을_조회한다();

                    // then
                    상태코드를_검증한다(response, HttpStatus.OK);
                    리스트_길이를_검증한다(response, ".", 1);
                }),
                dynamicTest("테이블의 빈 여부를 변경한다.", () -> {
                    // when
                    long 생성된_테이블_ID = 모든_테이블을_조회한다().jsonPath().getLong("[0].id");
                    ExtractableResponse<Response> response = 테이블의_빈_여부를_변경한다(생성된_테이블_ID, false);

                    // then
                    상태코드를_검증한다(response, HttpStatus.OK);
                    Boolean_필드값을_검증한다(response, "empty", false);
                }),
                dynamicTest("테이블의 방문한 손님수를 변경한다.", () -> {
                    // when
                    long 생성된_테이블_ID = 모든_테이블을_조회한다().jsonPath().getLong("[0].id");
                    ExtractableResponse<Response> response = 테이블의_방문한_손님수를_변경한다(생성된_테이블_ID, 20);

                    // then
                    상태코드를_검증한다(response, HttpStatus.OK);
                    숫자_필드값을_검증한다(response, "numberOfGuests", 20);
                })
        );
    }
}
