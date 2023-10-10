package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest
@Transactional
@Sql("classpath:cleanup.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup");

        // when
        MenuGroup created = menuGroupService.create(menuGroup);

        // then
        assertThat(created.getId()).isNotNull();
    }

    @Test
    void 모든_메뉴_그룹을_조회할_수_있다() {
        // given
        MenuGroup menuGroup1 = new MenuGroup();
        MenuGroup menuGroup2 = new MenuGroup();
        menuGroup1.setName("menuGroup1");
        menuGroup2.setName("menuGroup2");

        menuGroupService.create(menuGroup1);
        menuGroupService.create(menuGroup2);

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(2);
    }
}