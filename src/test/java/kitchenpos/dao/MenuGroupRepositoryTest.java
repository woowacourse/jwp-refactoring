package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MenuGroupRepositoryTest {

    private MenuGroupRepository menuGroupRepository;

    @Autowired
    public MenuGroupRepositoryTest(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Test
    void save() {
        // given
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");

        // when
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹"));

        // when
        Optional<MenuGroup> foundMenuGroup = menuGroupRepository.findById(savedMenuGroup.getId());

        // then
        assertThat(foundMenuGroup).isPresent();
    }

    @Test
    void findAll() {
        // given
        menuGroupRepository.save(new MenuGroup("메뉴그룹A"));
        menuGroupRepository.save(new MenuGroup("메뉴그룹B"));

        // when
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        // then
        int defaultSize = 4;
        assertThat(menuGroups).hasSize(defaultSize + 2);
    }

    @Test
    void existsById() {
        // given
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹"));

        // when
        boolean exists = menuGroupRepository.existsById(savedMenuGroup.getId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByIdWithEmptyId() {
        // given
        long invalidId = 999L;

        // when
        boolean exists = menuGroupRepository.existsById(invalidId);

        // then
        assertThat(exists).isFalse();
    }
}
