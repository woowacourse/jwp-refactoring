package kitchenpos.repository;

import static kitchenpos.constants.Constants.TEST_MENU_GROUP_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupRepositoryTest extends KitchenPosRepositoryTest {

    @DisplayName("MenuGroup 저장 - 성공")
    @Test
    void save_Success() {
        MenuGroup menuGroup = MenuGroup.entityOf(TEST_MENU_GROUP_NAME);

        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo(TEST_MENU_GROUP_NAME);
    }

    @DisplayName("MenuGroup ID로 MenuGroup 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnMenuGroup() {
        MenuGroup menuGroup = MenuGroup.entityOf(TEST_MENU_GROUP_NAME);
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        MenuGroup foundMenuGroup = menuGroupRepository.findById(savedMenuGroup.getId())
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 MenuGroup이 없습니다."));

        assertThat(foundMenuGroup.getId()).isEqualTo(savedMenuGroup.getId());
        assertThat(foundMenuGroup.getName()).isEqualTo(savedMenuGroup.getName());
    }

    @DisplayName("MenuGroup ID로 MenuGroup 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        MenuGroup menuGroup = MenuGroup.entityOf(TEST_MENU_GROUP_NAME);
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        Optional<MenuGroup> foundMenuGroup = menuGroupRepository
            .findById(savedMenuGroup.getId() + 1);

        assertThat(foundMenuGroup.isPresent()).isFalse();
    }

    @DisplayName("전체 MenuGroup 조회 - 성공")
    @Test
    void findAll_Success() {
        MenuGroup menuGroup = MenuGroup.entityOf(TEST_MENU_GROUP_NAME);
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        assertThat(menuGroups).isNotNull();
        assertThat(menuGroups).isNotEmpty();

        List<Long> menuGroupIds = menuGroups.stream()
            .map(MenuGroup::getId)
            .collect(Collectors.toList());

        assertThat(menuGroupIds).contains(savedMenuGroup.getId());
    }

    @DisplayName("MenuGroup ID 존재여부 확인 - True, ID 존재")
    @Test
    void existsById_ExistsId_ReturnTrue() {
        MenuGroup menuGroup = MenuGroup.entityOf(TEST_MENU_GROUP_NAME);
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        boolean existsMenuGroup = menuGroupRepository.existsById(savedMenuGroup.getId());

        assertThat(existsMenuGroup).isTrue();
    }

    @DisplayName("MenuGroup ID 존재여부 확인 - False, ID 존재하지 않음")
    @Test
    void existsById_NotExistsId_ReturnFalse() {
        MenuGroup menuGroup = MenuGroup.entityOf(TEST_MENU_GROUP_NAME);
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        boolean existsMenuGroup = menuGroupRepository.existsById(savedMenuGroup.getId() + 1);

        assertThat(existsMenuGroup).isFalse();
    }
}
