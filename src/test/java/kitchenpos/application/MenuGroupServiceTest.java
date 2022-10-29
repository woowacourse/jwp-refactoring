package kitchenpos.application;

import kitchenpos.application.dao.FakeMenuGroupDao;
import kitchenpos.application.request.menugroup.MenuGroupRequest;
import kitchenpos.application.response.ResponseAssembler;
import kitchenpos.application.response.menugroup.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.newMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest {

    private final ResponseAssembler responseAssembler = new ResponseAssembler();
    private final FakeMenuGroupDao menuGroupDao = new FakeMenuGroupDao();
    private final MenuGroupService menuGroupService = new MenuGroupService(menuGroupDao, responseAssembler);

    @BeforeEach
    void clear() {
        menuGroupDao.clear();
    }

    @DisplayName("메뉴 그룹을 추가한다")
    @Test
    void create() {
        final var request = new MenuGroupRequest("양식");
        final var actual = menuGroupService.create(request);
        assertThat(actual.getId()).isPositive();
    }

    @DisplayName("메뉴 그룹을 전체 조회한다")
    @Test
    void list() {
        final var expectedSize = 4;
        saveMenuGroupForTimes(expectedSize);

        final List<MenuGroupResponse> actual = menuGroupService.list();
        assertThat(actual).hasSize(expectedSize);
    }

    private void saveMenuGroupForTimes(final int times) {
        for (int i = 0; i < times; i++) {
            menuGroupDao.save(newMenuGroup("메뉴 그룹" + i));
        }
    }
}