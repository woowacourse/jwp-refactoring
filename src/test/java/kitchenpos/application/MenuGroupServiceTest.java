package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.application.fixture.MenuGroupFixture.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ServiceIntegrationTest
@DisplayName("메뉴 그룹 서비스")
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Nested
    @DisplayName("생성 메서드는")
    class CreateMenuGroup {
        private MenuGroup request;

        private MenuGroup subject() {
            return menuGroupService.create(request);
        }

        @Nested
        @DisplayName("메뉴 그룹명이 주어지면")
        class WithName {
            @BeforeEach
            void setUp() {
                request = createMenuGroupRequest("추천메뉴");
            }

            @Test
            @DisplayName("메뉴 그룹을 생성한다")
            void createMenuGroups() {
                MenuGroup result = subject();

                assertAll(
                        () -> assertThat(result).isEqualToIgnoringGivenFields(request, "id"),
                        () -> assertThat(result.getId()).isNotNull()
                );
            }
        }
    }

    @Nested
    @DisplayName("조회 메서드는")
    class FindMenuGroup {
        private List<MenuGroup> subject() {
            return menuGroupService.list();
        }

        @Nested
        @DisplayName("메뉴 그룹이 저장되어 있으면")
        class WithName {
            private List<MenuGroup> menuGroups;

            @BeforeEach
            void setUp() {
                menuGroups = Arrays.asList(
                        createMenuGroup(null, "추천 메뉴"),
                        createMenuGroup(null, "맛잇는 메뉴")
                );
                menuGroupDao.save(menuGroups.get(0));
                menuGroupDao.save(menuGroups.get(1));
            }

            @Test
            @DisplayName("메뉴 그룹을 조회한다")
            void findMenuGroups() {
                List<MenuGroup> result = subject();

                assertAll(
                        () -> assertThat(result).usingElementComparatorIgnoringFields("id").containsAll(menuGroups),
                        () -> assertThat(result).extracting(MenuGroup::getId).doesNotContainNull()
                );
            }
        }
    }
}