package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("햄버거");

        menuGroupService.create(menuGroup);

        List<MenuGroup> menuGroups = menuGroupService.list();
        List<String> menuGroupNames = menuGroups.stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toUnmodifiableList());
        assertAll(
                () -> assertThat(menuGroups).hasSize(5),
                () -> assertThat(menuGroupNames).contains("햄버거")
        );
    }
}