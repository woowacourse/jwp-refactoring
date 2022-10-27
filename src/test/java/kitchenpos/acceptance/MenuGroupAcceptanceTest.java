package kitchenpos.acceptance;

import static kitchenpos.acceptance.fixture.MenuGroupStepDefinition.메뉴_그룹을_생성한다;
import static kitchenpos.acceptance.fixture.MenuGroupStepDefinition.메뉴_그룹을_조회한다;
import static kitchenpos.support.fixture.MenuGroupFixture.두마리메뉴;
import static kitchenpos.support.fixture.MenuGroupFixture.한마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 메뉴_그룹_목록을_조회할_수_있다() {
        // given
        메뉴_그룹을_생성한다("두마리메뉴");
        메뉴_그룹을_생성한다("한마리메뉴");

        // when
        List<MenuGroup> extract = 메뉴_그룹을_조회한다();

        // then
        assertThat(extract).hasSize(2);
    }
}
