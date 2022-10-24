package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/init_schema.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹_등록_메소드는_입력받은_메뉴_그룹을_저장한다() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천 메뉴");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        final List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups).extracting(MenuGroup::getId, MenuGroup::getName)
                .contains(tuple(savedMenuGroup.getId(), "추천 메뉴"));
    }

    @Test
    void 메뉴_그룹_목록_조회_메소드는_모든_메뉴_그룹의_id_이름을_조회한다() {
        // given
        MenuGroup menuGroup1 = 메뉴_그룹을_생성한다("추천 메뉴 1");
        MenuGroup menuGroup2 = 메뉴_그룹을_생성한다("추천 메뉴 2");

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).extracting(MenuGroup::getId, MenuGroup::getName)
                .contains(tuple(menuGroup1.getId(), "추천 메뉴 1"), tuple(menuGroup2.getId(), "추천 메뉴 2"));
    }

    private MenuGroup 메뉴_그룹을_생성한다(final String name) {
        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName(name);

        return menuGroupService.create(menuGroup1);
    }
}