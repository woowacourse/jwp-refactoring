package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.IntegrationTest;
import kitchenpos.common.fixture.TMenuGroup;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

class MenuGroupServiceTest extends IntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다. - 성공")
    @ParameterizedTest
    @MethodSource("getParametersForCreate")
    void create(String name) {
        MenuGroup menuGroup = TMenuGroup.builder()
            .name(name)
            .build();

        assertThatCode(() -> menuGroupService.create(menuGroup))
            .doesNotThrowAnyException();
    }

    @DisplayName("메뉴 그룹 이름은 중복일 수 있다. - 성공")
    @Test
    void create_duplicate() {
        MenuGroup menuGroup = TMenuGroup.builder()
            .randomName()
            .build();

        menuGroupService.create(menuGroup);
        assertThatCode(() -> menuGroupService.create(menuGroup))
            .doesNotThrowAnyException();

    }

    @DisplayName("메뉴 그룹 이름은 null일 수 없다. - 실패")
    @Test
    void create_groupNameCanNotBeNull() {
        MenuGroup menuGroup = TMenuGroup.builder()
            .build();

        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("메뉴 그룹 이름은 255byte를 넘을 수 없다. - 실패")
    @Test
    void create_groupNameCanNotOver255Bytes() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 256; i++) {
            sb.append("a");
        }

        MenuGroup menuGroup = TMenuGroup.builder()
            .name(sb.toString())
            .build();
        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("전체 메뉴그룹을 불러온다. - 성공")
    @Test
    void list() {
        List<MenuGroup> menuGroups = TMenuGroup.multiBuilder()
            .randomName(50)
            .build();

        menuGroups.forEach(menuGroupService::create);

        assertThat(menuGroupService.list()).hasSize(50);
    }
}
