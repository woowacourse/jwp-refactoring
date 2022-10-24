package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.Fixtures;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @Test
    void create() {
        MenuGroup 메뉴그룹_한마리메뉴 = Fixtures.메뉴그룹_한마리메뉴();

        MenuGroup saved = menuGroupService.create(메뉴그룹_한마리메뉴);

        assertThat(menuGroupService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(saved);
    }
}
