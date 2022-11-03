package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹을 저장한다.")
    void save() {
        final MenuGroup menuGroup = new MenuGroup("메뉴그룹");

        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo("메뉴그룹")
        );
    }

    @Test
    @DisplayName("id로 메뉴 그룹을 찾는다.")
    void findById() {
        final MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final MenuGroup findMenuGroup = menuGroupRepository.findById(savedMenuGroup.getId())
                .orElseThrow();

        assertAll(
                () -> assertThat(findMenuGroup.getId()).isEqualTo(savedMenuGroup.getId()),
                () -> assertThat(findMenuGroup.getName()).isEqualTo(savedMenuGroup.getName())
        );
    }

    @Test
    @DisplayName("모든 메뉴 그룹을 조회한다.")
    void findAll() {
        final MenuGroup menuGroup1 = new MenuGroup("메뉴그룹1");
        final MenuGroup savedMenuGroup1 = menuGroupRepository.save(menuGroup1);
        final MenuGroup menuGroup2 = new MenuGroup("메뉴그룹2");
        final MenuGroup savedMenuGroup2 = menuGroupRepository.save(menuGroup2);

        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        assertAll(
                () -> assertThat(menuGroups).extracting("id")
                        .contains(savedMenuGroup1.getId(), savedMenuGroup2.getId()),
                () -> assertThat(menuGroups).extracting("name")
                        .contains(savedMenuGroup1.getName(), savedMenuGroup2.getName())
        );
    }

    @Test
    @DisplayName("id로 해당 메뉴그룹이 존재하는지 확인한다.")
    void existsById() {
        final MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final boolean existTrue = menuGroupRepository.existsById(savedMenuGroup.getId());
        final boolean existFalse = menuGroupRepository.existsById(-1L);

        assertAll(
                () -> assertThat(existTrue).isTrue(),
                () -> assertThat(existFalse).isFalse()
        );
    }
}