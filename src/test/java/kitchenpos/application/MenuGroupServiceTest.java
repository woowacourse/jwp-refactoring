package kitchenpos.application;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.MenuGroup;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends ServiceTest {

    private final MenuGroup menuGroup = new MenuGroup(null, "메뉴 그룹");

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹 생성")
    void createTest() {

        // when
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(menuGroupService.list()).contains(savedMenuGroup);
    }
}
