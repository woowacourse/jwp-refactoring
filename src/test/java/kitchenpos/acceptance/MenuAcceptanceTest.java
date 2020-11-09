package kitchenpos.acceptance;

import static kitchenpos.adapter.presentation.web.MenuRestController.*;
import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.application.response.MenuResponse;

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
                    List<MenuResponse> menus = getAll(MenuResponse.class, API_MENUS);
                    MenuResponse lastOrder = getLastItem(menus);

                    assertThat(lastOrder.getId()).isEqualTo(menuId);
                })
        );
    }

    private Long createMenu() throws Exception {
        String request = objectMapper.writeValueAsString(MENU_REQUEST);
        return post(request, API_MENUS);
    }
}
