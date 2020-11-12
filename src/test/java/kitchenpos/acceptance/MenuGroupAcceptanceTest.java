package kitchenpos.acceptance;

import static kitchenpos.ui.MenuGroupRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.domain.MenuGroup;

class MenuGroupAcceptanceTest extends AcceptanceTest {
    /*
     * Feature: 메뉴 그룹 관리
     *
     * Scenario: 메뉴 그룹을 관리한다.
     *
     * When: 메뉴 그룹을 등록한다.
     * Then: 메뉴 그룹이 등록된다.
     *
     * Given: 메뉴 그룹이 등록되어 있다.
     * When: 메뉴 그룹의 목록을 조회한다.
     * Then: 저장되어 있는 메뉴 그룹의 목록이 반환된다.
     */
    @DisplayName("메뉴 그룹을 관리한다")
    @TestFactory
    Stream<DynamicTest> manageMenuGroup() {
        return Stream.of(
                dynamicTest(
                        "메뉴 그룹을 등록한다",
                        () -> {
                            // When
                            final MenuGroup menuGroup = new MenuGroup();
                            menuGroup.setName("세마리 메뉴");

                            final MenuGroup createdMenuGroup = create(MENU_GROUP_REST_API_URI,
                                    menuGroup, MenuGroup.class);

                            // Then
                            assertAll(
                                    () -> assertThat(createdMenuGroup)
                                            .extracting(MenuGroup::getId)
                                            .isNotNull()
                                    ,
                                    () -> assertThat(createdMenuGroup)
                                            .extracting(MenuGroup::getName)
                                            .isEqualTo(menuGroup.getName())
                            );
                        }
                ),
                dynamicTest(
                        "메뉴 그룹의 목록을 조회한다",
                        () -> {
                            // Given
                            final MenuGroup menuGroup = new MenuGroup();
                            menuGroup.setName("사이드 메뉴");

                            final MenuGroup createdMenuGroup = create(MENU_GROUP_REST_API_URI,
                                    menuGroup, MenuGroup.class);

                            // When
                            final List<MenuGroup> menuGroups = list(MENU_GROUP_REST_API_URI,
                                    MenuGroup.class);

                            // Then
                            assertAll(
                                    assertThat(menuGroups)::isNotEmpty
                                    ,
                                    () -> assertThat(menuGroups)
                                            .extracting(MenuGroup::getId)
                                            .contains(createdMenuGroup.getId())
                            );
                        }
                )
        );
    }
}
