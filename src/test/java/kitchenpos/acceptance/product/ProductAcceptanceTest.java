package kitchenpos.acceptance.product;

import static kitchenpos.acceptance.AcceptanceSteps.응답_상태를_검증한다;
import static kitchenpos.acceptance.AcceptanceSteps.조회_요청_결과를_검증한다;
import static kitchenpos.acceptance.product.ProductAcceptanceSteps.상품_등록_요청을_보낸다;
import static kitchenpos.acceptance.product.ProductAcceptanceSteps.상품_목록_조회_요청을_보낸다;

import kitchenpos.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest {

    @Nested
    class 상품_등록_API extends AcceptanceTest {

        @Test
        void 상품을_등록할_수_있다() {
            // when
            var 응답 = 상품_등록_요청을_보낸다("강정치킨", 19000);

            // then
            응답_상태를_검증한다(응답, 201);
        }

        @Test
        void 상품_등록_시_가격이_0보다_작으면_오류() {
            // when
            var 응답 = 상품_등록_요청을_보낸다("강정치킨", -1);

            // then
            응답_상태를_검증한다(응답, 500);
        }
    }

    @Nested
    class 상품_조회_API extends AcceptanceTest {

        @Test
        void 상품들을_조회할_수_있다() {
            // given
            상품_등록_요청을_보낸다("말랑치킨", 100_000);
            상품_등록_요청을_보낸다("강정치킨", 20_000);

            // when
            var 응답 = 상품_목록_조회_요청을_보낸다();

            // then
            조회_요청_결과를_검증한다(응답,
                    "[\n"
                            + "    {\n"
                            + "        \"id\": 1,\n"
                            + "        \"name\": \"말랑치킨\",\n"
                            + "        \"price\": 100000.00\n"
                            + "    },\n"
                            + "    {\n"
                            + "        \"id\": 2,\n"
                            + "        \"name\": \"강정치킨\",\n"
                            + "        \"price\": 20000.00\n"
                            + "    }\n"
                            + "]"
            );
        }
    }
}
