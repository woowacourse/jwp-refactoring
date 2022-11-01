package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.getMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuGroupIntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴그룹을_생성한다() {
        final MenuGroup menuGroupRequest = getMenuGroupRequest();
        final MenuGroup menuGroup = menuGroupService.create(menuGroupRequest);
        assertThat(menuGroup.getId()).isNotNull();
    }

    @Test
    void 메뉴그룹목록을_조회한다() {
        final List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups).hasSize(4);
    }
}
