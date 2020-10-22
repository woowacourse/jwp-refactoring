package kitchenpos.acceptance;

import static java.util.Collections.*;
import static kitchenpos.ui.MenuRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuAcceptanceTest extends AcceptanceTest {
    /**
     * 메뉴를 관리한다.
     * <p>
     * When 메뉴 생성 요청.
     * Then 메뉴가 생성 된다.
     * <p>
     * Given 메뉴가 생성 되어 있다.
     * When 메뉴 전체 조회 요청.
     * Then 전체 메뉴를 반환한다.
     */
    @DisplayName("메뉴 관리")
    @TestFactory
    Stream<DynamicTest> manageMenu() throws Exception {
        // 메뉴 생성
        Long menuId = createMenu();
        assertThat(menuId).isNotNull();

        return Stream.of(
                dynamicTest("메뉴 전체 조회", () -> {
                    List<Menu> menus = getAll(Menu.class, API_MENUS);
                    Menu lastOrder = getLastItem(menus);

                    assertThat(lastOrder.getId()).isEqualTo(menuId);
                })
        );
    }

    private Long createMenu() throws Exception {
        MenuProduct menuProduct = menuProductFactory.create(1L, 2);
        Menu menu = menuFactory.create("후라이드+후라이드", BigDecimal.valueOf(17_000L), 1L,
                singletonList(menuProduct));

        String request = objectMapper.writeValueAsString(menu);
        return post(request, API_MENUS);
    }
}
