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

@SpringBootTest
@Transactional
@Sql("/schema.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹을 생성한다")
    void create() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("메뉴그룹");
        final MenuGroup createMenuGroup = menuGroupService.create(menuGroup);

        assertThat(createMenuGroup.getName())
                .isEqualTo("메뉴그룹");
    }

    @Test
    @DisplayName("메뉴 그룹 전체를 조회한다")
    void list() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("메뉴그룹");
        menuGroupDao.save(menuGroup);

        final List<MenuGroup> actual = menuGroupService.list();

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual)
                        .extracting("id")
                        .containsExactly(1L)
        );
    }
}
