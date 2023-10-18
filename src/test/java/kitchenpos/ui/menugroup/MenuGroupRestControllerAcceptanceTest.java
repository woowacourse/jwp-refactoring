package kitchenpos.ui.menugroup;

import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupRestControllerAcceptanceTest extends MenuGroupRestControllerAcceptanceTestFixture {

    private MenuGroup 메뉴_그룹;

    @BeforeEach
    void setup() {
        메뉴_그룹 = MenuGroupFixture.메뉴_그룹_생성();
    }

    @Test
    void 메뉴_그룹을_생성한다() {
        // when
        var 메뉴_그룹_생성_결과 = 메뉴_그룹을_생성한다("/api/menu-groups", 메뉴_그룹);

        // then
        메뉴_그룹이_성공적으로_생성된다(메뉴_그룹_생성_결과, 메뉴_그룹);
    }

    @Test
    void 메뉴_그룹들을_조회한다() {
        // given
        var 메뉴_그룹 = 메뉴_그룹_데이터_생성();

        // when
        var 조회_결과 = 메뉴_그룹을_전체_조회한다("/api/menu-groups");

        // then
        메뉴_그룹들이_성공적으로_생성된다(조회_결과, 메뉴_그룹);
    }
}
