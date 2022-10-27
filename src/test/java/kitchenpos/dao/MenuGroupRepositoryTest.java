package kitchenpos.dao;

import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME2;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("MenuGroup을 저장한다.")
    void save() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));

        assertThat(menuGroup).isEqualTo(menuGroupRepository.findById(menuGroup.getId()).orElseThrow());
    }

    @Test
    @DisplayName("MenuGroup을 모두 조회한다.")
    void findAll() {
        MenuGroup menuGroup1 = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
        MenuGroup menuGroup2 = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME2));

        assertThat(menuGroupRepository.findAll()).containsExactly(menuGroup1, menuGroup2);
    }

    @Test
    @DisplayName("MenuGroup이 존재하는지 확인한다.")
    void existsById() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));

        assertThat(menuGroupRepository.existsById(menuGroup.getId())).isTrue();
    }
}
