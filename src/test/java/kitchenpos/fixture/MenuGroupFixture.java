package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.request.MenuGroupRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;

public class MenuGroupFixture {

    public MenuGroupRequest 메뉴그룹_생성_요청(String name) {
        return new MenuGroupRequest(name);
    }

    public MenuGroup 메뉴그룹_생성(String name) {
        return MenuGroup.create(name);
    }

    public MenuGroup 메뉴그룹_생성(Long id, String name) {
        return MenuGroup.create(id, name);
    }

    public List<MenuGroupResponse> 메뉴그룹_응답_리스트_생성(MenuGroupResponse... menuGroupResponses) {
        return Arrays.asList(menuGroupResponses);
    }
}
