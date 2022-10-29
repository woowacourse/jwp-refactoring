package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.ui.dto.MenuGroupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;


    @Test
    void 메뉴집합을_생성한다() {
        MenuGroupRequest request = new MenuGroupRequest("주리링 추천 메뉴");

        MenuGroupResponse actual = menuGroupService.create(request);

        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 메뉴집합을_모두_조회한다() {
        메뉴집합_생성();

        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSizeGreaterThanOrEqualTo(1);
    }
}
