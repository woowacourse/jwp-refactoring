package kitchenpos.acceptance;

import static io.restassured.RestAssured.*;
import static kitchenpos.ui.MenuRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

class MenuAcceptanceTest extends AcceptanceTest {
    /**
     * Feature: 메뉴 관리
     * <p>
     * Scenario: 메뉴를 관리한다.
     * <p>
     * Given: 메뉴 그룹이 등록되어 있다.
     *        상품이 등록되어 있다.
     * When: 메뉴를 등록한다.
     * Then: 메뉴가 등록된다.
     * <p>
     * When: 메뉴의 목록을 조회한다.
     * Then: 저장되어 있는 메뉴의 목록이 반환된다.
     */
    @DisplayName("메뉴를 관리한다")
    @TestFactory
    Stream<DynamicTest> manageMenu() throws JsonProcessingException {
        final Product setupProduct = createSetupProduct();
        final MenuGroup setupMenuGroup = createSetupMenuGroup();

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(setupProduct.getId());
        menuProduct.setQuantity(2L);

        final Menu menu = new Menu();
        menu.setName("마늘치킨");
        menu.setPrice(BigDecimal.valueOf(18000));
        menu.setMenuGroupId(setupMenuGroup.getId());
        menu.setMenuProducts(newArrayList(menuProduct));

        return Stream.of(
                dynamicTest(
                        "메뉴를 등록한다",
                        () -> {
                            final Menu createdMenu = createMenu(menu);
                            assertAll(
                                    () -> assertThat(createdMenu)
                                            .extracting(Menu::getName)
                                            .isEqualTo(menu.getName())
                                    ,
                                    () -> assertThat(createdMenu)
                                            .extracting(Menu::getPrice)
                                            .usingComparator(BigDecimal::compareTo)
                                            .isEqualTo(menu.getPrice())
                                    ,
                                    () -> assertThat(createdMenu)
                                            .extracting(Menu::getMenuGroupId)
                                            .isEqualTo(menu.getMenuGroupId())
                            );
                        }
                ),
                dynamicTest(
                        "메뉴의 목록을 조회한다",
                        () -> {
                            final List<Menu> menus = listMenu();
                            assertThat(menus)
                                    .usingElementComparatorOnFields("name", "menuGroupId")
                                    .usingComparatorForElementFieldsWithNames(
                                            BigDecimal::compareTo,
                                            "price"
                                    )
                                    .contains(menu);
                        }
                )
        );
    }

    private Menu createMenu(Menu menu) throws JsonProcessingException {
        final String request = objectMapper.writeValueAsString(menu);

        // @formatter:off
        return
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                .when()
                        .post(MENU_REST_API_URI)
                .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().as(Menu.class)
                ;
        // @formatter:on
    }

    private List<Menu> listMenu() {
        // @formatter:off
        return
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                        .get(MENU_REST_API_URI)
                .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .jsonPath().getList(".", Menu.class);
        // @formatter:on
    }
}
