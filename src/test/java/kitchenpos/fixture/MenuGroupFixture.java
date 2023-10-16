package kitchenpos.fixture;

import kitchenpos.application.dto.request.CreateMenuGroupRequest;
import kitchenpos.application.dto.response.CreateMenuGroupResponse;
import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    private MenuGroupFixture() {
    }

    public static class REQUEST {

        public static CreateMenuGroupRequest 메뉴_그룹_치킨_생성_요청() {
            return CreateMenuGroupRequest.builder()
                    .id(1L)
                    .name("치킨")
                    .build();
        }
    }


    public static class RESPONSE {

        public static CreateMenuGroupResponse 메뉴_그룹_치킨_생성_응답() {
            return CreateMenuGroupResponse.builder()
                    .id(1L)
                    .name("치킨")
                    .build();
        }
    }

    public static class MENU_GROUP {

        public static MenuGroup 메뉴_그룹_치킨() {
            return MenuGroup.builder()
                    .id(1L)
                    .name("치킨")
                    .build();
        }
    }
}
