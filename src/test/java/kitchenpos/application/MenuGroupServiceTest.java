package kitchenpos.application;

import kitchenpos.application.test.IntegrateServiceTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.domain.fixture.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MenuGroupServiceTest extends IntegrateServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    class 메뉴_그룹_생성 {
        @Test
        void 모든_메뉴_그룹을_생성한다() {
            // given
            MenuGroup 인기_메뉴 = 인기_메뉴_생성();

            // when, then
            assertDoesNotThrow(() -> menuGroupService.create(인기_메뉴));
        }
    }

    @Nested
    class 메뉴_그룹_조회 {

        @BeforeEach
        void setUp() {
            MenuGroup 인기_메뉴 = 인기_메뉴_생성();
            menuGroupDao.save(인기_메뉴);
        }

        @Test
        void 모든_메뉴_그룹을_조회한다() {
            // when
            List<MenuGroup> menuGroups = menuGroupService.list();

            // then
            assertAll(
                    () -> assertThat(menuGroups).hasSize(1),
                    () -> assertThat(menuGroups).extracting(MenuGroup::getName)
                            .contains("인기 메뉴")
            );
        }

    }

}