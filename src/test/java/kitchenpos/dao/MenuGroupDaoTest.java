package kitchenpos.dao;

import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP_FIXTURE_1;
import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP_FIXTURE_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupDaoTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void save() {
        MenuGroup menuGroup = MENU_GROUP_FIXTURE_1;

        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        assertAll(
            () -> assertThat(persistMenuGroup.getId()).isNotNull(),
            () -> assertThat(persistMenuGroup.getName()).isEqualTo(menuGroup.getName())
        );
    }

    @Test
    void findById() {
        MenuGroup persistMenuGroup = menuGroupDao.save(MENU_GROUP_FIXTURE_1);

        MenuGroup findGroup = menuGroupDao.findById(persistMenuGroup.getId()).get();

        assertThat(findGroup.getId()).isEqualTo(persistMenuGroup.getId());
    }

    @Test
    void findAll() {
        menuGroupDao.save(MENU_GROUP_FIXTURE_1);
        menuGroupDao.save(MENU_GROUP_FIXTURE_2);

        List<MenuGroup> menuGroups = menuGroupDao.findAll();
        List<String> menuNames = menuGroups.stream()
            .map(MenuGroup::getName)
            .collect(Collectors.toList());

        assertThat(menuNames).contains("고기", "국");
    }

    @Test
    void existsById() {
        Long menuGroupId = menuGroupDao.save(MENU_GROUP_FIXTURE_1).getId();

        boolean isExist = menuGroupDao.existsById(menuGroupId);

        assertThat(isExist).isTrue();
    }
}
