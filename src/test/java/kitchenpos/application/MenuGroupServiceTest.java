package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.분식;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.ui.dto.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.ui.dto.MenuGroupRequest;
import kitchenpos.support.ServiceTestBase;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTestBase {

    @Test
    void 메뉴_그룹_정상_생성() {
        // given
        MenuGroupRequest request = 분식.toRequest();

        // when
        MenuGroupResponse response = menuGroupService.create(request);

        // then
        boolean actual = menuGroupDao.existsById(response.getId());
        assertThat(actual).isTrue();
    }

    @Test
    void 메뉴_그룹_이름을_null_값으로_생성() {
        // given
        MenuGroupRequest request = new MenuGroupRequest(null);

        // when & then
        assertThatThrownBy(() -> menuGroupService.create(request))
                .isInstanceOf(Exception.class);
    }

    @Test
    void 메뉴_그룹_목록_조회() {
        // given
        List<MenuGroup> menuGroups = 메뉴_그룹_목록_생성();

        // when
        List<MenuGroupResponse> actual = menuGroupService.list();

        // then
        assertThat(actual.size()).isEqualTo(menuGroups.size());
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(toResponse(menuGroups));
    }

    private List<MenuGroupResponse> toResponse(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }
}
