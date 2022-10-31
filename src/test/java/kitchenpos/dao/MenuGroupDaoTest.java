package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("MenuGroup 은 ")
@SpringTestWithData
class MenuGroupDaoTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("MenuGroup을 저장한다.")
    @Test
    void saveMenuGroup() {
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroupName"));

        assertAll(
                () -> assertThat(menuGroup.getId()).isGreaterThanOrEqualTo(1L),
                () -> assertThat(menuGroup.getName()).isEqualTo("menuGroupName")
        );
    }

    @DisplayName("MenuGroup 목록을 가져온다.")
    @Test
    void getManuGroups() {
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroupName"));

        final List<MenuGroup> menuGroups = menuGroupDao.findAll();

        assertAll(
                () -> assertThat(menuGroups.size()).isEqualTo(1),
                () -> assertThat(menuGroups.get(0).getId()).isEqualTo(menuGroup.getId())
        );
    }
}
