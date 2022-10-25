package acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Documented;
import java.util.List;
import javax.annotation.processing.SupportedOptions;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SupportedOptions("")
public class MenuAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴를 조회한다.")
    void getMenus() {
        메뉴를_생성한다("후라이드", 30000, 메뉴_그룹("후라이드+후라이드"), List.of(상품("후라이드", 16000)), 2);
        메뉴를_생성한다("양념", 34000, 메뉴_그룹("양념치킨+양념치킨"), List.of(상품("양념치킨", 18000)), 2);
        메뉴를_생성한다("반반", 32000, 메뉴_그룹("후라이드+양념치킨"), List.of(상품("반반치킨", 17000)), 2);

        List<Menu> menus = 메뉴를_조회한다();

        assertThat(menus).hasSize(3);
    }

    private long 상품(String name, int price) {
        return 상품을_생성한다(name, price);
    }

    private long 메뉴_그룹(String name) {
        return 메뉴_그룹을_생성한다(name);
    }
}
