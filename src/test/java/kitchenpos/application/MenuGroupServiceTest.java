package kitchenpos.application;

import static kitchenpos.Fixture.메뉴집합;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;


    @Test
    void 메뉴집합을_생성한다() {
        MenuGroup actual = menuGroupService.create(메뉴집합());
        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 메뉴집합을_모두_조회한다() {
        메뉴집합_생성();

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSizeGreaterThanOrEqualTo(1);
    }
}
