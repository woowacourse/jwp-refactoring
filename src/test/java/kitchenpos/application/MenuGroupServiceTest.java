package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import support.IntegrationTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("[메뉴 그룹 추가] - 성공")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menu");

        //when
        MenuGroup actual = menuGroupService.create(menuGroup);

        //then
        assertThat(actual.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("[메뉴 그룹 조회] - 성공")
    @Test
    void list() {
        //when
        List<MenuGroup> actual = menuGroupService.list();

        //then
        assertThat(actual).hasSize(4);
    }
}
