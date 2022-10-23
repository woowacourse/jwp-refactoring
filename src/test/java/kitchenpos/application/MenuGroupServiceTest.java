package kitchenpos.application;

import static kitchenpos.application.fixture.MenuGroupFixture.MENUGROUP_세트메뉴A;
import static kitchenpos.application.fixture.MenuGroupFixture.MENUGROUP_세트메뉴B;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        // given
        final MenuGroup menuGroup = MENUGROUP_세트메뉴A.create();

        // when
        final MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        // then
        final Long createdMenuGroupId = createdMenuGroup.getId();
        assertAll(
                () -> assertThat(createdMenuGroupId).isNotNull(),
                () -> assertThat(menuGroupDao.findById(createdMenuGroupId)).isPresent()
        );
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회한다.")
    void list() {
        // given
        menuGroupDao.save(MENUGROUP_세트메뉴A.create());
        menuGroupDao.save(MENUGROUP_세트메뉴B.create());

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).hasSize(2);
    }
}
