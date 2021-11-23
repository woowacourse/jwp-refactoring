package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.testtool.RequestBuilder;
import kitchenpos.ui.dto.request.MenuGroupRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class MenuGroupFixture extends DefaultFixture {

    public MenuGroupFixture(RequestBuilder requestBuilder) {
        super(requestBuilder);
    }

    public MenuGroupRequest 메뉴그룹_생성_요청(String name) {
        return new MenuGroupRequest(name);
    }

    public MenuGroup 메뉴그룹_생성(String name) {
        return MenuGroup.create(name);
    }

    public List<MenuGroupResponse> 메뉴그룹_응답_리스트_생성(MenuGroupResponse... menuGroupResponses) {
        return Arrays.asList(menuGroupResponses);
    }

    public MenuGroupResponse 메뉴그룹_등록(MenuGroupRequest request) {
        return request()
                .post("/api/menu-groups", request)
                .build()
                .convertBody(MenuGroupResponse.class);
    }

    public List<MenuGroupResponse> 메뉴그룹_리스트_조회() {
        return request()
                .get("/api/menu-groups")
                .build()
                .convertBodyToList(MenuGroupResponse.class);
    }

}
