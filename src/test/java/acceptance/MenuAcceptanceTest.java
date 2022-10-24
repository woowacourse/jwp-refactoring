package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴를 조회한다.")
    void getMenus() {
        // given
        long 후라이드 = 상품_생성("후라이드", 16000);
        long 양념치킨 = 상품_생성("양념치킨", 18000);
        long 반반치킨 = 상품_생성("반반치킨", 17000);

        long 한마리_메뉴 = 메뉴_그룹_생성("한마리 메뉴");
        long 두마리_메뉴 = 메뉴_그룹_생성("두마리 메뉴");

        long 후라이드_두마리_메뉴 = 메뉴_생성("후라이드+후라이드", 30000, 두마리_메뉴, List.of(후라이드), 2);
        long 양념치킨_두마리_메뉴 = 메뉴_생성("양념치킨+양념치킨", 34000, 두마리_메뉴, List.of(양념치킨), 2);
        long 반반치킨_메뉴 = 메뉴_생성("반반치킨", 17000, 한마리_메뉴, List.of(반반치킨), 1);

        // when
        List<Menu> menus = 메뉴_조회();

        // then
        assertThat(menus)
                .extracting(Menu::getId, Menu::getName, menu -> menu.getPrice().intValueExact(), Menu::getMenuGroupId)
                .containsExactlyInAnyOrder(
                        tuple(후라이드_두마리_메뉴, "후라이드+후라이드", 30000, 두마리_메뉴),
                        tuple(양념치킨_두마리_메뉴, "양념치킨+양념치킨", 34000, 두마리_메뉴),
                        tuple(반반치킨_메뉴, "반반치킨", 17000, 한마리_메뉴)
                );
    }
}
