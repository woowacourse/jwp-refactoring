package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final MenuGroup menuGroup = new MenuGroup("두마리메뉴");

        // when
        final MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(createdMenuGroup).isNotNull();
        assertThat(createdMenuGroup.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups)
                .hasSize(4)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴"
                );
    }
}
