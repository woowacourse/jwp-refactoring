package kitchenpos.acceptance;

import static io.restassured.RestAssured.*;
import static kitchenpos.ui.MenuGroupRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import kitchenpos.domain.MenuGroup;

class MenuGroupAcceptanceTest extends AcceptanceTest {
    /**
     * Feature: 메뉴 그룹 관리
     * <p>
     * Scenario: 메뉴 그룹을 관리한다.
     * <p>
     * When: 메뉴 그룹을 등록한다.
     * Then: 메뉴 그룹이 등록된다.
     * <p>
     * When: 메뉴 그룹의 목록을 조회한다.
     * Then: 저장되어 있는 메뉴 그룹의 목록이 반환된다.
     */
    @DisplayName("메뉴 그룹을 관리한다")
    @TestFactory
    Stream<DynamicTest> manageMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("세마리 메뉴");

        return Stream.of(
                dynamicTest(
                        "메뉴 그룹을 등록한다",
                        () -> {
                            final MenuGroup createdMenuGroup = createMenuGroup(menuGroup);
                            assertThat(createdMenuGroup)
                                    .extracting(MenuGroup::getName)
                                    .isEqualTo(menuGroup.getName())
                            ;
                        }
                ),
                dynamicTest(
                        "메뉴 그룹의 목록을 조회한다",
                        () -> {
                            final List<MenuGroup> menuGroups = listMenuGroup();
                            assertAll(
                                    assertThat(menuGroups)::isNotEmpty
                                    ,
                                    () -> assertThat(menuGroups)
                                            .usingElementComparatorOnFields("name")
                                            .contains(menuGroup)
                            );
                        }
                )
        );
    }

    private List<MenuGroup> listMenuGroup() {

        // @formatter:off
        return
                given()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                        .get(MENU_GROUP_REST_API_URI)
                .then()
                        .log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .jsonPath().getList(".", MenuGroup.class)
                ;
        // @formatter:on
    }
}
