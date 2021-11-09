package kitchenpos.integration.api;

import java.util.List;
import kitchenpos.integration.utils.MockMvcResponse;
import kitchenpos.integration.utils.MockMvcUtils;
import kitchenpos.menu.ui.request.MenuGroupCreateRequest;
import kitchenpos.menu.application.response.MenuGroupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupApi {

    private static final String BASE_URL = "/api/menu-groups";

    @Autowired
    private MockMvcUtils mockMvcUtils;

    public MockMvcResponse<MenuGroupResponse> 메뉴_그룹_등록(MenuGroupCreateRequest menuGroup) {
        return mockMvcUtils.request()
            .post(BASE_URL)
            .content(menuGroup)
            .asSingleResult(MenuGroupResponse.class);
    }

    public MockMvcResponse<MenuGroupResponse> 메뉴_그룹_등록(String name) {
        return 메뉴_그룹_등록(MenuGroupCreateRequest.create(name));
    }

    public MockMvcResponse<List<MenuGroupResponse>> 메뉴_그룹_조회() {
        return mockMvcUtils.request()
            .get(BASE_URL)
            .asMultiResult(MenuGroupResponse.class);
    }
}
