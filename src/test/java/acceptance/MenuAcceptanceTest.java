package acceptance;

import static acceptance.MenuGroupAcceptanceTest.createMenuGroup;
import static acceptance.ProductAcceptanceTest.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.Application;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
public class MenuAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    public static Menu givenMenu(String name, int price, long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuProduct givenMenuProduct(long productId11, int quantity) {
        MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(productId11);
        menuProduct1.setQuantity(quantity);
        return menuProduct1;
    }

    public static long createMenu(String name, int price, long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = givenMenu(name, price, menuGroupId, menuProducts);
        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(menu)
                .when().log().all()
                .post("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void findMenus() {
        long menuGroupId = createMenuGroup("라라 메뉴");

        long productId1 = createProduct("후라이드", 9000);
        long productId2 = createProduct("돼지국밥", 7000);
        long productId3 = createProduct("피자", 12000);
        long productId4 = createProduct("수육", 18000);

        MenuProduct menuProduct1 = givenMenuProduct(productId1, 1);
        MenuProduct menuProduct2 = givenMenuProduct(productId2, 1);
        MenuProduct menuProduct3 = givenMenuProduct(productId3, 1);
        MenuProduct menuProduct4 = givenMenuProduct(productId4, 1);

        long menuId1 = createMenu("해장 세트", 15000, menuGroupId, List.of(menuProduct1, menuProduct2));
        long menuId2 = createMenu("아재 세트", 13000, menuGroupId, List.of(menuProduct3, menuProduct2));
        long menuId3 = createMenu("피자치킨 세트", 12000, menuGroupId, List.of(menuProduct1, menuProduct3));
        long menuId4 = createMenu("국밥 수육 메뉴", 27000, menuGroupId, List.of(menuProduct3, menuProduct4));

        List<Menu> menus = getMenus();

        assertThat(menus).extracting(Menu::getId, Menu::getName, menu -> menu.getPrice().intValueExact())
                .containsExactlyInAnyOrder(
                        tuple(menuId1, "해장 세트", 15000),
                        tuple(menuId2, "아재 세트", 13000),
                        tuple(menuId3, "피자치킨 세트", 12000),
                        tuple(menuId4, "국밥 수육 메뉴", 27000)
                );
    }

    private List<Menu> getMenus() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", Menu.class);
    }
}
