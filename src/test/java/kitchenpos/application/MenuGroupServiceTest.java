package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        MenuGroupRequest request = new MenuGroupRequest("메뉴그룹1");

        MenuGroupResponse actual = menuGroupService.create(request);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getName()).isEqualTo("메뉴그룹1");
        });
    }

    @Test
    void 전체_메뉴_그룹을_조회할_수_있다() {
        MenuGroup menuGroup1 = new MenuGroup("메뉴그룹1");
        MenuGroup menuGroup2 = new MenuGroup("메뉴그룹2");

        menuGroupRepository.save(menuGroup1);
        menuGroupRepository.save(menuGroup2);

        List<MenuGroupResponse> actual = menuGroupService.list();

        assertThat(actual).hasSize(2);
    }
}
