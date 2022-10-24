package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import io.restassured.RestAssured;
import java.util.List;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;

public class MenuAcceptanceTest extends AcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("메뉴를 조회한다.")
    void getMenus() {
        // given
        long 한마리_메뉴 = 메뉴_그룹_생성("한마리 메뉴");
        long 두마리_메뉴 = 메뉴_그룹_생성("두마리 메뉴");

        long 후라이드치킨 = 메뉴_생성("후라이드치킨", 16000, 한마리_메뉴);
        long 양념치킨 = 메뉴_생성("양념치킨", 18000, 한마리_메뉴);
        long 반반치킨 = 메뉴_생성("반반치킨", 17000, 두마리_메뉴);

        // when
        List<Menu> menus = 메뉴_조회();

        // then
        assertThat(menus).extracting(Menu::getId, Menu::getName, Menu::getPrice, Menu::getMenuGroupId)
                .containsExactlyInAnyOrder(
                        tuple(후라이드치킨, "후라이드치킨", 16000, 한마리_메뉴),
                        tuple(양념치킨, "양념치킨", 18000, 한마리_메뉴),
                        tuple(반반치킨, "반반치킨", 17000, 두마리_메뉴)
                );
    }
}
