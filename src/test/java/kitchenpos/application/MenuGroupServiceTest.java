package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceIntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        // given
        final MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest("추천메뉴");

        // when
        final MenuGroupResponse result = menuGroupService.create(menuGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isNotNull();
            softly.assertThat(result.getName()).isEqualTo(menuGroup.getName());
        });
    }

    @Test
    void list() {
        // given
        final MenuGroupResponse menuGroup1 = menuGroupService.create(new MenuGroupCreateRequest("Group1"));
        final MenuGroupResponse menuGroup2 = menuGroupService.create(new MenuGroupCreateRequest("Group2"));

        // when
        final List<MenuGroupResponse> result = menuGroupService.list();

        // then
        assertThat(result).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(menuGroup1, menuGroup2);
    }
}
