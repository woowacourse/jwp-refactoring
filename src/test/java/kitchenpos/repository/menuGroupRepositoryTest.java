package kitchenpos.repository;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class menuGroupRepositoryTest {
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹을 저장할 수 있다.")
    @Test
    void save() {
        MenuGroup menuGroup = createMenuGroup(null, "2+1메뉴");

        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        assertAll(
            () -> assertThat(savedMenuGroup).isNotNull(),
            () -> assertThat(savedMenuGroup).isEqualToIgnoringGivenFields(menuGroup, "id")
        );
    }

    @DisplayName("메뉴 그룹 아이디로 메뉴 그룹을 조회할 수 있다.")
    @Test
    void findById() {
        MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup(null, "2+1메뉴"));

        Optional<MenuGroup> foundMenuGroup = menuGroupRepository.findById(menuGroup.getId());

        assertThat(foundMenuGroup.get()).isEqualToComparingFieldByField(menuGroup);
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        List<MenuGroup> savedMenuGroups = Arrays.asList(
            menuGroupRepository.save(createMenuGroup(null, "메뉴그룹1")),
            menuGroupRepository.save(createMenuGroup(null, "메뉴그룹2")),
            menuGroupRepository.save(createMenuGroup(null, "메뉴그룹3"))
        );

        List<MenuGroup> allMenuGroups = menuGroupRepository.findAll();

        assertThat(allMenuGroups).usingFieldByFieldElementComparator().containsAll(savedMenuGroups);
    }

    @DisplayName("메뉴 그룹 아이디로 메뉴 그룹 존재 여부를 확인할 수 있다.")
    @Test
    void existsById() {
        Long menuGroupId = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹1")).getId();

        assertAll(
            () -> assertThat(menuGroupRepository.existsById(menuGroupId)).isTrue(),
            () -> assertThat(menuGroupRepository.existsById(menuGroupId + 1)).isFalse()
        );
    }
}
