package kitchenpos.application.menu;

import static kitchenpos.support.fixture.MenuGroupFixture.createSaleMenuGroup;
import static kitchenpos.support.fixture.MenuGroupFixture.createSuggestionMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends IntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹을 등록할 수 있다.")
    @Test
    void create() {
        final MenuGroupRequest menuGroupRequest = new MenuGroupRequest("할인메뉴");

        final MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);

        final Optional<MenuGroup> savedMenuGroup = menuGroupDao.findById(menuGroupResponse.getId());
        assertThat(savedMenuGroup).isPresent();
    }

    @DisplayName("메뉴그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        final int originSize = menuGroupDao.findAll().size();
        menuGroupDao.save(createSaleMenuGroup());
        menuGroupDao.save(createSuggestionMenuGroup());

        final List<MenuGroupResponse> menuGroupResponses = menuGroupService.list();
        final int afterSize = menuGroupResponses.size();

        assertThat(afterSize - originSize).isEqualTo(2);
    }
}
