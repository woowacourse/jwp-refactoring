package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.menugroup.application.dto.response.MenuGroupResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuGroupServiceTest extends ServiceTestConfig {

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 그룹 생성")
    @Nested
    class Create {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final MenuGroupCreateRequest request = new MenuGroupCreateRequest("메뉴 그룹");

            // when
            final MenuGroupResponse actual = menuGroupService.create(request);

            // then
            assertThat(actual.getName()).isEqualTo(request.getName());
        }
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Nested
    class ReadAll {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final MenuGroup savedMenuGroup = saveMenuGroup();

            // when
            final List<MenuGroupResponse> actual = menuGroupService.list();

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
                softly.assertThat(actual.get(0).getId()).isEqualTo(savedMenuGroup.getId());
            });
        }
    }
}
