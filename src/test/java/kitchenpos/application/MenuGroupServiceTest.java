package kitchenpos.application;

import static kitchenpos.DomainFixture.createMenuGroup;
import static kitchenpos.DomainFixture.메뉴그룹1;
import static kitchenpos.DomainFixture.메뉴그룹2;
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
        final MenuGroup menuGroup = createMenuGroup("새로운메뉴그룹");

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
        메뉴그룹등록(메뉴그룹1);
        메뉴그룹등록(메뉴그룹2);

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).hasSize(2);
    }
}
