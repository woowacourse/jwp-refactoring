package kitchenpos.application;

import static kitchenpos.Fixtures.메뉴그룹_한마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @Test
    void create() {
        MenuGroup 메뉴그룹_한마리메뉴 = menuGroupService.create(메뉴그룹_한마리메뉴());

        List<MenuGroup> 메뉴그룹_목록 = menuGroupService.list();

        assertThat(메뉴그룹_목록)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(메뉴그룹_한마리메뉴);
    }
}
