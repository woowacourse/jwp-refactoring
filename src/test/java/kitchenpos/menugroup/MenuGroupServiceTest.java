package kitchenpos.menugroup;

import kitchenpos.ServiceTestHelper;
import kitchenpos.menugroup.MenuGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTestHelper {

    @Test
    void 메뉴그룹을_등록한다() {
        // given
        MenuGroup menuGroup1 = 메뉴_그룹_등록("메뉴그룹1");
        MenuGroup menuGroup2 = 메뉴_그룹_등록("메뉴그룹2");

        // when
        final List<MenuGroup> menuGroups = 메뉴_그룹_전체_조회();

        // then
        assertThat(menuGroups).usingElementComparatorIgnoringFields()
                .contains(menuGroup1, menuGroup2);
    }
}
