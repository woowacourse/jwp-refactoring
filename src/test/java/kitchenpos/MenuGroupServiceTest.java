package kitchenpos;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("MenuGroupService 테스트")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 추가")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = MenuGroup.builder()
                .name("이달의 메뉴")
                .build();
        //when
        MenuGroup create = menuGroupService.create(menuGroup);
        //then
        assertThat(create.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 반환")
    @Test
    void list() {
        //given
        //when
        List<MenuGroup> menuGroups = menuGroupService.list();
        //then
        assertThat(menuGroups).isNotEmpty();
    }
}
