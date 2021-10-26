package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("Menu 인수 테스트")
class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("Menu를 생성한다.")
    @Test
    void create() {
        Menu menu = menu();
        Menu created = makeResponse("/api/menus", TestMethod.POST, menu)
            .as(Menu.class);

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getName()).isEqualTo("menu"),
            () -> assertThat(created.getMenuProducts().size()).isEqualTo(1)
        );
    }

    @DisplayName("Menu 생성 실패 - 금액이 0보다 작다.")
    @Test
    void create_fail_price() {
        Menu menu = menu();
        menu.setPrice(BigDecimal.valueOf(-500));
        ExtractableResponse<Response> response = makeResponse("/api/menus", TestMethod.POST, menu);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Menu 생성 실패 - 메뉴 그룹이 존재하지 않는다.")
    @Test
    void create_fail_menu_group_non_exist() {
        Menu menu = menu();
        menu.setMenuGroupId(999L);
        ExtractableResponse<Response> response = makeResponse("/api/menus", TestMethod.POST, menu);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Menu 생성 실패 - 메뉴 상품이 존재하지 않는다.")
    @Test
    void create_fail_menu_product_non_exist() {
        Menu menu = menu();
        menu.setMenuProducts(Arrays.asList(new MenuProduct(1L, null, 999L, 10)));
        ExtractableResponse<Response> response = makeResponse("/api/menus", TestMethod.POST, menu);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Menu 생성 실패 - 메뉴 가격이 메뉴 상품들 총액보다 높다.")
    @Test
    void create_fail_menu_price_more_than_total_menu_product_price() {
        Menu menu = menu();
        menu.setPrice(BigDecimal.valueOf(20000));
        ExtractableResponse<Response> response = makeResponse("/api/menus", TestMethod.POST, menu);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Menu 리스트를 불러온다.")
    @Test
    void list() {
        Menu menu = menu();
        makeResponse("/api/menus", TestMethod.POST, menu);

        List<Menu> menus = makeResponse("/api/menus", TestMethod.GET).jsonPath()
            .getList(".", Menu.class);
        assertThat(menus.size()).isEqualTo(1);
    }
}
