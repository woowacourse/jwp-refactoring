package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @DisplayName("메뉴그룹을 추가한다.")
    @Test
    void create() {
        MenuGroupResponse 메뉴그룹_한마리메뉴 = menuGroupService.create(메뉴그룹요청_한마리메뉴());

        List<MenuGroupResponse> 메뉴그룹_목록 = menuGroupService.list();

        검증_필드비교_값포함(assertThat(메뉴그룹_목록), 메뉴그룹_한마리메뉴);
    }

    @DisplayName("메뉴그룹 목록을 조회한다.")
    @Test
    void list() {
        MenuGroupResponse 메뉴그룹_한마리메뉴 = menuGroupService.create(메뉴그룹요청_한마리메뉴());
        MenuGroupResponse 메뉴그룹_두마리메뉴 = menuGroupService.create(메뉴그룹요청_두마리메뉴());

        List<MenuGroupResponse> 메뉴그룹_목록 = menuGroupService.list();

        검증_필드비교_동일_목록(메뉴그룹_목록, List.of(메뉴그룹_한마리메뉴, 메뉴그룹_두마리메뉴));
    }
}
