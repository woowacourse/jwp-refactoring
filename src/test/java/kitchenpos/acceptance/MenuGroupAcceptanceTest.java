package kitchenpos.acceptance;

import static io.restassured.RestAssured.*;
import static kitchenpos.ui.MenuGroupRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import kitchenpos.domain.MenuGroup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenuGroupAcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("메뉴 그룹 관리")
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
                                    .isEqualTo(menuGroup.getName());
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

    private MenuGroup createMenuGroup(MenuGroup menuGroup) throws JsonProcessingException {
        final String request = objectMapper.writeValueAsString(menuGroup);

        // @formatter:off
        return
                given()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                .when()
                        .post(MENU_GROUP_REST_API_URI)
                .then()
                        .log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().as(MenuGroup.class)
                ;
        // @formatter:on
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
