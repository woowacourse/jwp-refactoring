package kitchenpos.integration.api;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.integration.utils.MockMvcResponse;
import kitchenpos.integration.utils.MockMvcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupApi {

    private static final String BASE_URL = "/api/menu-groups";

    @Autowired
    private MockMvcUtils mockMvcUtils;

    public MockMvcResponse<MenuGroup> 메뉴_그룹_등록(MenuGroup menuGroup) {
        return mockMvcUtils.request()
            .post(BASE_URL)
            .content(menuGroup)
            .asSingleResult(MenuGroup.class);
    }

    public MockMvcResponse<MenuGroup> 메뉴_그룹_등록(String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return 메뉴_그룹_등록(menuGroup);
    }

    public MockMvcResponse<List<MenuGroup>> 메뉴_그룹_조회() {
        return mockMvcUtils.request()
            .get(BASE_URL)
            .asMultiResult(MenuGroup.class);
    }
}
