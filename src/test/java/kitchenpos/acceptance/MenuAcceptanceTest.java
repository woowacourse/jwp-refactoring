package kitchenpos.acceptance;

import static kitchenpos.KitchenPosFixtures.까르보치킨_생성요청;
import static kitchenpos.KitchenPosFixtures.두_마리_메뉴_생성요청;
import static kitchenpos.KitchenPosFixtures.메뉴_URL;
import static kitchenpos.KitchenPosFixtures.메뉴그룹_URL;
import static kitchenpos.KitchenPosFixtures.짜장치킨_생성요청;
import static kitchenpos.KitchenPosFixtures.프로덕트_URL;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuGroupResponse 두_마리_메뉴;
    private Product 생성된_까르보치킨;
    private Product 생성된_짜장치킨;

    @BeforeEach
    void setupMenuGroup() {
        두_마리_메뉴 = 생성요청(메뉴그룹_URL, 두_마리_메뉴_생성요청).body().as(MenuGroupResponse.class);
        생성된_까르보치킨 = 생성요청(프로덕트_URL, 까르보치킨_생성요청).body().as(Product.class);
        생성된_짜장치킨 = 생성요청(프로덕트_URL, 짜장치킨_생성요청).body().as(Product.class);
    }

    @Test
    void 신규_메뉴를_생성할_수_있다() {
        // given
        final var 메뉴명 = "까르보 한 마리 + 짜장 한 마리";
        final var 메뉴가격 = new BigDecimal(30000);
        final var 메뉴_생성요청_데이터 = Map.of(
                "name", 메뉴명,
                "price", 메뉴가격,
                "menuGroupId", 두_마리_메뉴.getId(),
                "menuProducts", List.of(
                        Map.of("productId", 생성된_까르보치킨.getId(), "quantity", 1),
                        Map.of("productId", 생성된_짜장치킨.getId(), "quantity", 1)
                )
        );

        // when
        final var 메뉴_생성응답 = 생성요청(메뉴_URL, 메뉴_생성요청_데이터);
        final var 생성된_메뉴 = 메뉴_생성응답.body().as(Menu.class);
        final var 메뉴_내_상품들 = 생성된_메뉴.getMenuProducts();

        // then
        assertAll(
                응답일치(메뉴_생성응답, HttpStatus.CREATED),
                단일_데이터_검증(생성된_메뉴.getId(), 1L),
                단일_데이터_검증(생성된_메뉴.getName(), 메뉴명),
                단일_데이터_검증(생성된_메뉴.getPrice().doubleValue(), 메뉴가격.doubleValue()),
                단일_데이터_검증(생성된_메뉴.getMenuGroupId(), 두_마리_메뉴.getId()),
                리스트_데이터_검증(메뉴_내_상품들, "menuId", 1L, 1L),
                리스트_데이터_검증(메뉴_내_상품들, "productId", 생성된_까르보치킨.getId(), 생성된_짜장치킨.getId()),
                리스트_데이터_검증(메뉴_내_상품들, "quantity", 1L, 1L)
        );
    }

    @Test
    void 전체_메뉴를_조회할_수_있다() {
        // given
        final var 메뉴명 = "까르보 한 마리 + 짜장 한 마리";
        final var 메뉴가격 = new BigDecimal(30000);
        final var 메뉴_생성요청_데이터 = Map.of(
                "name", 메뉴명,
                "price", 메뉴가격,
                "menuGroupId", 두_마리_메뉴.getId(),
                "menuProducts", List.of(
                        Map.of("productId", 생성된_까르보치킨.getId(), "quantity", 1),
                        Map.of("productId", 생성된_짜장치킨.getId(), "quantity", 1)
                )
        );

        final var 메뉴명2 = "까르보 두 마리";
        final var 메뉴가격2 = new BigDecimal(31000);
        final var 메뉴_생성요청_데이터2 = Map.of(
                "name", 메뉴명2,
                "price", 메뉴가격2,
                "menuGroupId", 두_마리_메뉴.getId(),
                "menuProducts", List.of(
                        Map.of("productId", 생성된_까르보치킨.getId(), "quantity", 2)
                )
        );

        생성요청(메뉴_URL, 메뉴_생성요청_데이터);
        생성요청(메뉴_URL, 메뉴_생성요청_데이터2);

        // when
        final var 메뉴_조회응답 = 조회요청(메뉴_URL);
        final var 메뉴_응답_데이터 = 메뉴_조회응답.body().as(List.class);

        // then
        assertAll(
                응답일치(메뉴_조회응답, HttpStatus.OK),
                리스트_데이터_검증(메뉴_응답_데이터, "id", 1, 2),
                리스트_데이터_검증(메뉴_응답_데이터, "name", 메뉴명, 메뉴명2),
                리스트_데이터_검증(메뉴_응답_데이터, "price", 메뉴가격.doubleValue(), 메뉴가격2.doubleValue()),
                리스트_데이터_검증(메뉴_응답_데이터, "menuGroupId", 두_마리_메뉴.getId().intValue(), 두_마리_메뉴.getId().intValue())
        );
    }
}
