package kitchenpos.application;

import static kitchenpos.fixture.domain.MenuGroupFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴그룹을 생성한다.")
    @Test
    void create_success() {
        // given
        MenuGroup menuGroup = createMenuGroup("추천메뉴");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        MenuGroup dbMenuGroup = menuGroupRepository.findById(savedMenuGroup.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(dbMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list_success() {
        // given
        MenuGroup menuGroup = menuGroupService.create(createMenuGroup("추천메뉴"));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        List<String> menuGroupNames = menuGroups.stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());
        assertThat(menuGroupNames).contains(menuGroup.getName());
    }
}
