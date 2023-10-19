package kitchenpos.ui.table;

import kitchenpos.ordertable.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.application.dto.OrderTableChangeNumberOfGuestRequest;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableRestControllerAcceptanceTest extends TableRestControllerAcceptanceTestFixture {

    protected OrderTable 주문_테이블;

    @BeforeEach
    void setup() {
        주문_테이블 = 주문_테이블_생성(null, 10, false);
    }

    @Test
    void 주문_테이블을_생성한다() {
        // when
        var 생성_결과 = 주문_테이블을_생성한다("/api/tables", 주문_테이블);

        // then
        주문_테이블이_성공적으로_생성된다(생성_결과, 주문_테이블);
    }

    @Test
    void 주문_테이블을_모두_조회한다() {
        // given
        var 주문_테이블_데이터 = 주문_테이블_데이터를_생성한다();

        // when
        var 조회_결과 = 주문_테이블을_전체_조회한다("/api/tables");

        // then
        주문_테이블들이_성공적으로_조회된다(조회_결과, 주문_테이블_데이터);
    }

    @Test
    void 주문_테이블의_상태를_변경한다() {
        // given
        var 주문_테이블_데이터 = 주문_테이블_데이터를_생성한다();
        var 주문_테이블_비어있도록_변경_요청_데이터 = new OrderTableChangeEmptyRequest(true);
        var 주문_테이블_id = 주문_테이블_데이터.getId();

        // when
        var 변경_결과 = 주문_테이블의_상태를_변경한다("/api/tables/" + 주문_테이블_id + "/empty", 주문_테이블_비어있도록_변경_요청_데이터);

        // then
        주문_테이블의_상태가_성공적으로_변경된다(변경_결과, 주문_테이블_데이터);
    }

    @Test
    void 주문_테이블의_손님_수를_변경한다() {
        // given
        var 주문_테이블_데이터 = 주문_테이블_데이터를_생성한다();
        var 손님의_수를_변경_요청_데이터 = new OrderTableChangeNumberOfGuestRequest(3);
        var 주문_테이블_id = 주문_테이블_데이터.getId();

        // when
        var 변경_결과 = 주문_테이블의_손님_수를_변경한다("/api/tables/" + 주문_테이블_id + "/number-of-guests", 손님의_수를_변경_요청_데이터);

        // then
        주문_테이블의_손님_수가_성공적으로_변경된다(변경_결과, 주문_테이블_데이터, 손님의_수를_변경_요청_데이터);
    }
}
