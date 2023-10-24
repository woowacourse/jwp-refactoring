package kitchenpos.ui.menu;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupRestControllerTest extends MenuGroupAcceptanceTestUtil {

    @Test
    void 메뉴그룹_생성() {
        // given
        var 요청 = 메뉴그룹_생성요청();

        // when
        var 응답 = 메뉴그룹을_생성한다(요청);

        // then
        메뉴그룹이_생성됨(요청, 응답);
    }

    @Test
    void 메뉴그룹_목록_조회() {
        // given
        var 요청 = 메뉴그룹_생성요청();
        메뉴그룹을_생성한다(요청);

        // when
        var 응답 = 메뉴그룹_목록을_조회한다();

        // then
        메뉴그룹_목록이_조회됨(응답);
    }
}
