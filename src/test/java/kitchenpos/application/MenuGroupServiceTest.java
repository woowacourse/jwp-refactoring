package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.application.dto.request.MenuGroupRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.menugroup.service.MenuGroupService;
import kitchenpos.supports.MenuGroupFixture;
import kitchenpos.supports.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("메뉴 그룹 서비스 테스트")
@IntegrationTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다")
    @Test
    void createMenuGroup() {
        // given
        final MenuGroupRequest request = new MenuGroupRequest("주인장 최애 메뉴");

        // when
        final MenuGroupResponse savedMenuGroup = menuGroupService.create(request);

        // then
        assertSoftly(softly -> {
            assertThat(savedMenuGroup.getId()).isPositive();
            assertThat(savedMenuGroup.getName()).isEqualTo(request.getName());
        });
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다")
    @Test
    void findAllMenuGroups() {
        // given
        final MenuGroupResponse group1 = menuGroupService.create(MenuGroupFixture.create());
        final MenuGroupResponse group2 = menuGroupService.create(MenuGroupFixture.create());

        // when
        final List<MenuGroupResponse> list = menuGroupService.list();

        // then
        assertThat(list).hasSize(2);
        assertSoftly(softly -> {
            assertThat(list).hasSize(2);
            assertThat(list)
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(group1, group2));
        });
    }
}
