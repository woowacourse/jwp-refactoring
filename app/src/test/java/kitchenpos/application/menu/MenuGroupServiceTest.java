package kitchenpos.application.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.IntegrationTest;
import kitchenpos.menu.application.dto.CreateMenuGroupCommand;
import kitchenpos.menu.application.dto.CreateMenuGroupResponse;
import kitchenpos.menu.application.dto.SearchMenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends IntegrationTest {

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        CreateMenuGroupCommand command = new CreateMenuGroupCommand("추천메뉴");

        // when
        CreateMenuGroupResponse result = menuGroupService.create(command);

        // then
        assertAll(
                () -> assertThat(result.id()).isPositive(),
                () -> assertThat(result.name()).isEqualTo("추천메뉴")
        );
    }

    @Test
    void 메뉴_그룹들을_조회한다() {
        // given
        menuGroupRepository.save(new MenuGroup("추천메뉴"));
        menuGroupRepository.save(new MenuGroup("인기메뉴"));

        // when
        List<SearchMenuGroupResponse> result = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).id()).isPositive(),
                () -> assertThat(result.get(0).name()).isEqualTo("추천메뉴"),
                () -> assertThat(result.get(1).id()).isPositive(),
                () -> assertThat(result.get(1).name()).isEqualTo("인기메뉴")
        );
    }
}
