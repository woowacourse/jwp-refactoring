package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.menu.MenuGroupCreateRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    class 메뉴_그룹_생성 {

        @Test
        void 정상_요청() {
            // given
            MenuGroupCreateRequest request = new MenuGroupCreateRequest("한식");

            // when
            MenuGroupResponse menuGroupResponse = menuGroupService.create(request);

            // then
            assertThat(menuGroupResponse.getName()).isEqualTo(request.getName());
        }
    }

    @Nested
    class 메뉴_그룹_전체_조회 {

        @Test
        void 정상_요청() {
            // given
            MenuGroupCreateRequest request = new MenuGroupCreateRequest("중식");
            MenuGroupResponse menuGroupResponse = menuGroupService.create(request);

            // when
            List<MenuGroup> menuGroups = menuGroupService.readAll();

            // then
            assertThat(menuGroups)
                    .extracting(MenuGroup::getName)
                    .contains(menuGroupResponse.getName());
        }
    }
}
