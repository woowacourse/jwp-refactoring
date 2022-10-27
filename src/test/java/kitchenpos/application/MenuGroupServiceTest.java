package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.menu.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class MenuGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        final MenuGroup menuGroup1 = 메뉴_그룹_등록("메뉴그룹1");
        final MenuGroup menuGroup2 = 메뉴_그룹_등록("메뉴그룹2");

        final List<MenuGroup> menuGroups = 메뉴_그룹_전체_조회();

        assertThat(menuGroups).usingElementComparatorIgnoringFields()
                .contains(menuGroup1, menuGroup2);
    }

    @Test
    @DisplayName("메뉴 그룹 이름은 null일 수 없다.")
    void createWithNullName() {
        assertThatThrownBy(() -> 메뉴_그룹_등록(null))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
