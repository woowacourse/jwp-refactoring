package kitchenpos.ui.order;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableRestControllerTest extends TableAcceptanceTestUtil {

    @Test
    void 테이블_생성() {
        // given
        var 요청 = 테이블생성_요청();

        // when
        var 응답 = 테이블을_생성한다(요청);

        // then
        테이블이_생성됨(요청, 응답);
    }

    @Test
    void 테이블_목록_조회() {
        //given
        var 요청 = 테이블생성_요청();
        테이블을_생성한다(요청);

        // when
        var 응답 = 테이블_목록을_조회한다();

        // then
        테이블_목록이_조회됨(응답);
    }

    @Test
    void 테이블_비움상태_변경() {
        //given
        var 생성요청 = 테이블생성_요청();
        var 생성응답 = 테이블을_생성한다(생성요청);
        var 기존테이블id = 기존_테이블을_가져온다(생성응답);

        var 비움_요청 = 테이블_비움_요청();

        // when
        var 비움_응답 = 테이블을_비운다(기존테이블id, 비움_요청);

        // then
        테이블이_비워짐(비움_요청, 비움_응답);
    }

    @Test
    void changeNumberOfGuests() {
        //given
        var 생성요청 = 채운_테이블생성_요청();
        var 생성응답 = 테이블을_생성한다(생성요청);
        var 기존테이블id = 기존_테이블을_가져온다(생성응답);

        var 손님수_변경_요청 = 테이블_손님수_변경_요청();

        // when
        var 손님수_변경_응답 = 테이블_손님수를_변경한다(기존테이블id, 손님수_변경_요청);

        //  then
        테이블_손님수가_변경됨(손님수_변경_요청, 손님수_변경_응답);
    }
}
