package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceTest {

    @Test
    void 메뉴_그룹_등록_메소드는_입력받은_메뉴_그룹을_저장한다() {
        // given
        MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest("추천 메뉴");

        // when
        MenuGroupResponse response = menuGroupService.create(menuGroup);

        // then
        assertAll(() -> {
            assertThat(response.getId()).isNotNull();
            assertThat(response)
                    .extracting(MenuGroupResponse::getName)
                    .isEqualTo(menuGroup.getName());
        });
    }

    @Test
    void 메뉴_그룹_목록_조회_메소드는_모든_메뉴_그룹의_id_이름을_조회한다() {
        // given
        MenuGroup menuGroup1 = 메뉴_그룹을_저장한다("추천 메뉴 1");
        MenuGroup menuGroup2 = 메뉴_그룹을_저장한다("추천 메뉴 2");

        // when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups)
                .extracting(MenuGroupResponse::getId, MenuGroupResponse::getName)
                .contains(tuple(menuGroup1.getId(), menuGroup1.getName()),
                        tuple(menuGroup2.getId(), menuGroup2.getName()));
    }
}
