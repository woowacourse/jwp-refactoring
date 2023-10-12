package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:truncate.sql")
@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹을 정상적으로 생성한다.")
    void create() {
        // given
        final MenuGroup menuGroup = new MenuGroup(null, "치킨");

        // when
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isEqualTo(1L),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo("치킨")
        );
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 정상적으로 조회한다.")
    void list() {
        // given
        final MenuGroup menuGroupA = new MenuGroup(1L, "치킨");
        final MenuGroup menuGroupB = new MenuGroup(2L, "피자");
        menuGroupDao.save(menuGroupA);
        menuGroupDao.save(menuGroupB);

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        final MenuGroup savedMenuGroupA = menuGroups.get(0);
        final MenuGroup savedMenuGroupB = menuGroups.get(1);
        assertAll(
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(savedMenuGroupA.getId()).isEqualTo(1L),
                () -> assertThat(savedMenuGroupA.getName()).isEqualTo("치킨"),
                () -> assertThat(savedMenuGroupB.getId()).isEqualTo(2L),
                () -> assertThat(savedMenuGroupB.getName()).isEqualTo("피자")
        );
    }
}
