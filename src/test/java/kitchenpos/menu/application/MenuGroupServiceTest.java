package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.ui.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu.ui.dto.response.MenuGroupCreateResponse;
import kitchenpos.menu.ui.dto.response.MenuGroupFindAllResponse;
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
        MenuGroupCreateResponse response = menuGroupService.create(request);

        // then
        MenuGroup dbMenuGroup = menuGroupRepository.findById(response.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(dbMenuGroup.getName()).isEqualTo(request.getName());
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list_success() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));

        // when
        List<MenuGroupFindAllResponse> responses = menuGroupService.list();

        // then
        List<String> menuGroupNames = responses.stream()
                .map(MenuGroupFindAllResponse::getName)
                .collect(Collectors.toList());
        assertThat(menuGroupNames).contains(menuGroup.getName());
    }
}
