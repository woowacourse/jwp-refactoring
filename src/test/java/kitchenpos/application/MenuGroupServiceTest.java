package kitchenpos.application;

import static kitchenpos.helper.MenuGroupHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.MenuGroup;

@SpringBootTest
@Sql("/truncate.sql")
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        // given
        String 메뉴그룹이름 = "추천메뉴";
        MenuGroup 추가할_메뉴_그룹 = createMenuGroup(메뉴그룹이름);

        // when
        MenuGroup 메뉴_그룹 = menuGroupService.create(추가할_메뉴_그룹);

        // then
        assertAll(
                () -> assertThat(메뉴_그룹.getId()).isNotNull(),
                () -> assertThat(메뉴_그룹.getName()).isEqualTo(메뉴그룹이름)
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        // given
        MenuGroup 추가할_메뉴_그룹_1 = createMenuGroup("추천메뉴1");
        MenuGroup 추가할_메뉴_그룹_2 = createMenuGroup("추천메뉴2");
        MenuGroup 메뉴_그룹_1 = menuGroupService.create(추가할_메뉴_그룹_1);
        MenuGroup 메뉴_그룹_2 = menuGroupService.create(추가할_메뉴_그룹_2);

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(menuGroups.get(0).getName()).isEqualTo(메뉴_그룹_1.getName()),
                () -> assertThat(menuGroups.get(1).getName()).isEqualTo(메뉴_그룹_2.getName())
        );
    }
}
