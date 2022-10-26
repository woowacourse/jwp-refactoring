package kitchenpos.acceptance;

import static kitchenpos.DomainFixtures.면_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 메뉴_그룹을_추가한다() {
        MenuGroup menuGroup = testRestTemplate.postForObject("http://localhost:" + port + "/api/menu-groups",
                면_메뉴_그룹(), MenuGroup.class);

        assertThat(menuGroup.getName()).isEqualTo("면");
        assertThat(menuGroup.getId()).isNotZero();
    }

    @Test
    void 메뉴_그룹을_조회한다() {
        testRestTemplate.postForObject("http://localhost:" + port + "/api/menu-groups",
                면_메뉴_그룹(), MenuGroup.class);
        testRestTemplate.postForObject("http://localhost:" + port + "/api/menu-groups",
                new MenuGroup("육류"), MenuGroup.class);

        List<MenuGroup> groups = Arrays.asList(
                testRestTemplate.getForObject("http://localhost:" + port + "/api/menu-groups", MenuGroup[].class));

        assertThat(groups).hasSize(2);
    }
}
