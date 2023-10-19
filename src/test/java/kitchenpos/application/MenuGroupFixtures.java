package kitchenpos.application;

import kitchenpos.application.dto.MenuGroupCreateRequest;
import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtures {

    public static MenuGroup 분식메뉴그룹() {
        return new MenuGroup("분식");
    }

    public static MenuGroupCreateRequest 분식메뉴그룹_요청() {
        MenuGroupCreateRequest request = new MenuGroupCreateRequest();
        request.setName("분식");
        return request;
    }
}
