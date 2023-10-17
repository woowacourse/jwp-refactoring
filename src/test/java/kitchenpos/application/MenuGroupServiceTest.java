package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_추가한다() {
        //given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("일식");

        //when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);

        //then
        assertSoftly(softly -> {
            softly.assertThat(menuGroupResponse.getId()).isNotNull();
            softly.assertThat(menuGroupResponse.getName()).isEqualTo(menuGroupRequest.getName());
        });
    }

    @Nested
    class 메뉴_그룹_조회_시 {

        @Test
        void 모든_메뉴_그룹_목록을_조회한다() {
            //given
            MenuGroupRequest menuGroupRequestA = new MenuGroupRequest("일식");
            MenuGroupRequest menuGroupRequestB = new MenuGroupRequest("중식");
            MenuGroupResponse menuGroupResponseA = menuGroupService.create(menuGroupRequestA);
            MenuGroupResponse menuGroupResponseB = menuGroupService.create(menuGroupRequestB);

            //when
            List<MenuGroupResponse> menuGroups = menuGroupService.list();

            //then
            assertThat(menuGroups).usingRecursiveComparison()
                    .isEqualTo(List.of(menuGroupResponseA, menuGroupResponseB));
        }

        @Test
        void 메뉴_그룹이_존재하지_않으면_목록이_비어있다() {
            //given, when
            List<MenuGroupResponse> menuGroups = menuGroupService.list();

            //then
            assertThat(menuGroups).isEmpty();
        }
    }
}
