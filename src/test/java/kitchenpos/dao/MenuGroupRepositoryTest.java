package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("MenuGroup 은 ")
@SpringTestWithData
class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("MenuGroup을 저장한다.")
    @Test
    void saveMenuGroup() {
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroupName"));

        assertAll(
                () -> assertThat(menuGroup.getId()).isGreaterThanOrEqualTo(1L),
                () -> assertThat(menuGroup.getName()).isEqualTo("menuGroupName")
        );
    }

    @DisplayName("MenuGroup 목록을 가져온다.")
    @Test
    void getManuGroups() {
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroupName"));

        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        assertAll(
                () -> assertThat(menuGroups.size()).isEqualTo(1),
                () -> assertThat(menuGroups.get(0).getId()).isEqualTo(menuGroup.getId())
        );
    }

    @DisplayName("특정 MenuGroup 목록을 가져온다.")
    @Test
    void getMenuGroup() {
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroupName"));

        MenuGroup actual = menuGroupRepository.findById(menuGroup.getId())
                .get();

        assertThat(actual.getId()).isEqualTo(menuGroup.getId());
    }
}
