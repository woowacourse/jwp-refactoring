package kitchenpos.application;

import kitchenpos.execute.ServiceIntegrateTest;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.menu_group.application.MenuGroupService;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.domain.repository.MenuGroupRepository;
import kitchenpos.menu_group.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class MenuGroupServiceIntegrateTest extends ServiceIntegrateTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    class 메뉴_그룹_생성 {
        @Test
        void 모든_메뉴_그룹을_생성한다() {
            // given
            MenuGroup 인기_메뉴 = MenuGroupFixture.인기_메뉴_생성();

            // when, then
            Assertions.assertDoesNotThrow(() -> menuGroupService.create(인기_메뉴.getName()));
        }

    }

    @Nested
    class 메뉴_그룹_조회 {

        @BeforeEach
        void setUp() {
            MenuGroup 인기_메뉴 = MenuGroupFixture.인기_메뉴_생성();
            menuGroupRepository.save(인기_메뉴);
        }

        @Test
        void 모든_메뉴_그룹을_조회한다() {
            // when
            List<MenuGroupResponse> menuGroups = menuGroupService.list();

            // then
            Assertions.assertAll(
                    () -> org.assertj.core.api.Assertions.assertThat(menuGroups).hasSize(1),
                    () -> org.assertj.core.api.Assertions.assertThat(menuGroups).extracting(MenuGroupResponse::getName)
                            .contains("인기 메뉴")
            );
        }

    }

}