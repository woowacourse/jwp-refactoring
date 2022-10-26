package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @Test
    void create() {
        MenuGroup 메뉴그룹_한마리메뉴 = menuGroupService.create(메뉴그룹_한마리메뉴());

        List<MenuGroup> 메뉴그룹_목록 = menuGroupService.list();

        검증_필드비교_값포함(assertThat(메뉴그룹_목록), 메뉴그룹_한마리메뉴);
    }
}
