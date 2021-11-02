package kitchenpos;

import io.restassured.RestAssured;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

    protected MenuGroup 기본_메뉴_그룹 = new MenuGroup();
    protected Menu 기본_메뉴 = new Menu();

    @LocalServerPort
    protected int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        기본_메뉴_그룹.setName("기본메뉴그룹");
    }
}
