package kitchenpos.ui.order;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderRestControllerTest extends OrderAcceptanceTestUtil {

    @Test
    void 주문_생성() {
        // given
        var 요청 = 주문_생성_요청();

        // when
        var 응답 = 주문을_생성한다(요청);

        // then
        주문이_생성됨(요청, 응답);
    }

    @Test
    void 주문목록_조회() {
        // given
        var 주문_생성_요청 = 주문_생성_요청();
        주문을_생성한다(주문_생성_요청);

        // when
        var 응답 = 주문목록을_조회한다();

        // then
        주문목록이_조회됨(응답);
    }

    @Test
    void changeOrderStatus() {
        // given
        var 주문_생성_요청 = 주문_생성_요청();
        var 주문_생성_응답 = 주문을_생성한다(주문_생성_요청);

        var orderId = 주문id_조회(주문_생성_응답);
        var 요청 = 주문상태_변경_요청();

        // when
        var 응답 = 주문_상태를_변경한다(orderId, 요청);

        // then
        주문_상태가_변경됨(요청, 응답);
    }
}
