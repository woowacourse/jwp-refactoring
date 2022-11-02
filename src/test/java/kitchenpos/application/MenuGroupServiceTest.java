package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
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
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("추천메뉴");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(request);

        // then
        MenuGroup dbMenuGroup = menuGroupRepository.findById(savedMenuGroup.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(dbMenuGroup.getName()).isEqualTo(request.getName());
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list_success() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        List<String> menuGroupNames = menuGroups.stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());
        assertThat(menuGroupNames).contains(menuGroup.getName());
    }
}
