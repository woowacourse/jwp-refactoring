package kitchenpos.application;

import static kitchenpos.common.fixture.MenuGroupFixture.메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        MenuGroup menuGroup = 메뉴_그룹();

        // when
        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(createdMenuGroup).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(메뉴_그룹());
    }

    @Test
    void 전체_메뉴_그룹을_조회한다() {
        // given
        Long menuGroupId = menuGroupDao.save(메뉴_그룹()).getId();

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).usingRecursiveComparison()
                .isEqualTo(List.of(메뉴_그룹(menuGroupId)));
    }
}
