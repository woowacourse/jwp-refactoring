package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceTest {

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {
        @DisplayName("메뉴 그룹을 생성한다.")
        @Test
        void Should_CreateMenuGroup() {
            // given
            MenuGroupRequest request = new MenuGroupRequest("메뉴 그룹");

            // when
            MenuGroupResponse actual = menuGroupService.create(request);

            // then
            assertAll(() -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(actual.getName()).isEqualTo(request.getName());
            });
        }
    }

    @DisplayName("list 메소드는")
    @Nested
    class ListMethod {
        @DisplayName("생성된 메뉴 그룹 목록을 조회한다.")
        @Test
        void Should_ReturnMenuGroupList() {
            // given
            menuGroupService.create(new MenuGroupRequest("분식"));
            menuGroupService.create(new MenuGroupRequest("한식"));
            menuGroupService.create(new MenuGroupRequest("중식"));

            // when
            List<MenuGroupResponse> actual = menuGroupService.list();

            // then
            assertThat(actual).hasSize(3);
        }
    }
}
