package kitchenpos.application;

import static kitchenpos.DomainFixture.세트_메뉴;
import static kitchenpos.DomainFixture.인기_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성하고_결과를_반환한다() {
        // when
        final var createdMenuGroup = menuGroupService.create(인기_메뉴);

        // then
        assertAll(
                () -> assertThat(createdMenuGroup.getId()).isNotNull(),
                () -> assertThat(createdMenuGroup.getName()).isEqualTo(인기_메뉴.getName())
        );
    }

    @Test
    void 메뉴_그룹_목록을_조회한다() {
        // given
        menuGroupService.create(인기_메뉴);
        menuGroupService.create(세트_메뉴);

        // when
        final var foundMenuGroups = menuGroupService.list();

        // then
        assertThat(foundMenuGroups).hasSizeGreaterThanOrEqualTo(2);
    }
}
