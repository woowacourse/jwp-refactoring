package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = "/data-initialization-h2.sql")
@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @DisplayName("[메뉴 그룹 생성] 메뉴 그룹을 정상적으로 생성한다.")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = TestFixtureFactory.메뉴그룹_생성("인기 메뉴");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo("인기 메뉴");
    }

    @DisplayName("[메뉴 그룹 전체 조회] 메뉴 그룹 전체를 조회한다.")
    @Test
    void list() {
        // given
        MenuGroup menuGroup1 = TestFixtureFactory.메뉴그룹_생성("인기 메뉴");
        MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);
        MenuGroup menuGroup2 = TestFixtureFactory.메뉴그룹_생성("추천 메뉴");
        MenuGroup savedMenuGroup2 = menuGroupService.create(menuGroup2);

        // when
        List<MenuGroup> findMenuGroups = menuGroupService.list();

        // then
        assertThat(findMenuGroups).hasSize(2);
        isSameMenuGroup(findMenuGroups.get(0), savedMenuGroup1);
        isSameMenuGroup(findMenuGroups.get(1), savedMenuGroup2);
    }

    private void isSameMenuGroup(MenuGroup menuGroup, MenuGroup otherMenuGroup) {
        assertThat(menuGroup.getId()).isEqualTo(otherMenuGroup.getId());
        assertThat(menuGroup.getName()).isEqualTo(otherMenuGroup.getName());
    }
}