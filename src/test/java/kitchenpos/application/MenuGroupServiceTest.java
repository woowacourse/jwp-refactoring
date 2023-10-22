package kitchenpos.application;

import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.ui.request.MenuGroupRequest;
import kitchenpos.ui.response.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴그룹을_생성한다() {
        final MenuGroupRequest request = new MenuGroupRequest(null, "abc");

        final MenuGroupResponse created = menuGroupService.create(request);

        assertThat(created).isNotNull();
    }

    @Test
    void 모든_메뉴그룹을_조회한다() {
        final List<MenuGroupResponse> expected = menuGroupRepository.findAll().stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());

        final List<MenuGroupResponse> actual = menuGroupService.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
