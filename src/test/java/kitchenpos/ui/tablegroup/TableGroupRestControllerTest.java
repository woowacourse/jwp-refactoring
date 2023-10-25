package kitchenpos.ui.tablegroup;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupRestControllerTest extends TableGroupAcceptanceTestUtil {

    @Test
    void 테이블_그룹화() {
        // given
        var 테이블1 = 테이블_생성_id();
        var 테이블2 = 테이블_생성_id();
        var 그룹화할_테이블_id_요청 = 그룹화할_테이블id_요청(테이블1, 테이블2);
        var 그룹화_요청 = 그룹화_요청(그룹화할_테이블_id_요청);

        // when
        var 응답 = 테이블을_그룹화한다(그룹화_요청);

        // then
        테이블이_그룹화됨(그룹화_요청, 응답);
    }

    @Test
    void 테이블_비그룹화() {
        // given
        var 테이블1 = 테이블_생성_id();
        var 테이블2 = 테이블_생성_id();
        var 그룹화할_테이블_id_요청 = 그룹화할_테이블id_요청(테이블1, 테이블2);
        var 그룹화_요청 = 그룹화_요청(그룹화할_테이블_id_요청);

        var 그룹화_응답 = 테이블을_그룹화한다(그룹화_요청);
        테이블이_그룹화됨(그룹화_요청, 그룹화_응답);
        var 그룹테이블_id = 그룹테이블_id_조회(그룹화_응답);

        // when
        var 응답 = 테이블을_비그룹화한다(그룹테이블_id);

        // then
        테이블이_비그룹화됨(응답);
    }
}
