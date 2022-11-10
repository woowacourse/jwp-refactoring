package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.세트메뉴_REQUEST;
import static kitchenpos.fixture.MenuGroupFixture.할인메뉴_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.FakeMenuGroupDao;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @BeforeEach
    void beforeEach() {
        menuGroupService = new MenuGroupService(new FakeMenuGroupDao());
    }

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        // when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(할인메뉴_REQUEST);

        // then
        assertThat(menuGroupResponse.getId()).isEqualTo(1L);
        assertThat(menuGroupResponse.getName()).isEqualTo(할인메뉴_REQUEST.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void list() {
        // given
        menuGroupService.create(할인메뉴_REQUEST);
        menuGroupService.create(세트메뉴_REQUEST);

        // when
        List<MenuGroupResponse> menuGroupResponses = menuGroupService.list();

        // then
        assertThat(menuGroupResponses.size()).isEqualTo(2);
    }
}
