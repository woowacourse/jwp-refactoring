package kitchenpos;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("MenuGroupService 테스트")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 추가")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = MenuGroupFixture.create();
        //when
        MenuGroup create = menuGroupService.create(menuGroup);
        //then
        assertThat(create.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 반환")
    @Test
    void list() {
        //given
        MenuGroup menuGroup = MenuGroupFixture.create();
        menuGroupService.create(menuGroup);
        //when
        List<MenuGroup> menuGroups = menuGroupService.list();
        //then
        assertThat(menuGroups).hasSize(1);
    }
}
