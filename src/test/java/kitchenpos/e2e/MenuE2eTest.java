package kitchenpos.e2e;

import static kitchenpos.support.E2eTest.AssertionPair.row;
import static kitchenpos.support.AssertionsSupport.assertAll;
import static kitchenpos.support.fixture.MenuGroupFixture.단짜_두_마리_메뉴;
import static kitchenpos.support.fixture.MenuProductFixture.menuProductRequestBuilder;
import static kitchenpos.support.fixture.ProductFixture.간장_치킨_요청_DTO;
import static kitchenpos.support.fixture.ProductFixture.양념_치킨_요청_DTO;
import static kitchenpos.support.fixture.ProductFixture.후라이드_치킨_요청_DTO;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.presentation.dto.request.MenuRequest;
import kitchenpos.menu.presentation.dto.response.MenuProductResponse;
import kitchenpos.menu.presentation.dto.response.MenuResponse;
import kitchenpos.support.KitchenPosE2eTest;
import kitchenpos.support.fixture.MenuFixture.WrapMenuRequest.WrapMenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuE2eTest extends KitchenPosE2eTest {

    private MenuGroup 메뉴그룹 = null;
    private Product 후라이드_치킨 = null;
    private Product 간장_치킨 = null;
    private Product 양념_치킨 = null;

    private MenuRequest 메뉴_요청바디_1;
    private MenuRequest 메뉴_요청바디_2;

    @BeforeEach
    void menuSetUp() {

        메뉴그룹 = 메뉴_그룹_생성(단짜_두_마리_메뉴);
        후라이드_치킨 = 상품_생성(후라이드_치킨_요청_DTO);
        간장_치킨 = 상품_생성(간장_치킨_요청_DTO);
        양념_치킨 = 상품_생성(양념_치킨_요청_DTO);

        메뉴_요청바디_1 = MenuRequest.builder()
                .name("후라이드 + 후라이드")
                .intPrice(30_000)
                .menuProductRequests(
                        menuProductRequestBuilder()
                                .product(후라이드_치킨)
                                .quantity(2L)
                                .build())
                .menuGroup(메뉴그룹)
                .build();

        메뉴_요청바디_2 = MenuRequest.builder()
                .name("양념 + 간장")
                .intPrice(30_000)
                .menuProductRequests(
                        menuProductRequestBuilder()
                                .product(후라이드_치킨)
                                .quantity(2L)
                                .build(),
                        menuProductRequestBuilder()
                                .product(간장_치킨)
                                .quantity(1L)
                                .build())
                .menuGroup(메뉴그룹)
                .build();
    }

    @Test
    void create() {

        // given :: setUp 메서드

        // when
        ExtractableResponse<Response> 응답 = POST_요청(MENU_URL, 메뉴_요청바디_1);
        WrapMenuResponse 저장된_메뉴 = 응답.body().as(WrapMenuResponse.class);
        MenuProductResponse 저장된_메뉴상품 = 저장된_메뉴.getMenuProducts().get(0);

        // then
        assertAll(
                HTTP_STATUS_검증(HttpStatus.CREATED, 응답),
                NOT_NULL_검증(저장된_메뉴.id()),
                단일_검증(저장된_메뉴.intPrice(), 30_000),
                단일_검증(저장된_메뉴상품.getProductId(), 후라이드_치킨.getId()),
                단일_검증(저장된_메뉴상품.getQuantity(), 2L)
        );
    }

    @Test
    void list() {

        // given :: 일부 setUp 메서드
        POST_요청(MENU_URL, 메뉴_요청바디_1);
        POST_요청(MENU_URL, 메뉴_요청바디_2);

        // when
        // TODO TypeRef를 이용해서 Mock객체로 불러오기, 가격정보만 Validation해서 들고오는 순간 체크
        ExtractableResponse<Response> 응답 = GET_요청(MENU_URL);

        List<MenuResponse> 메뉴_응답_리스트 = 응답.body().as(new TypeRef<>() {
        }); // TODO Refactor
        List<MenuProductResponse> 메뉴상품_리스트_1 = 메뉴_응답_리스트.get(0).getMenuProducts();
        List<MenuProductResponse> 메뉴상품_리스트_2 = 메뉴_응답_리스트.get(1).getMenuProducts();

        // TODO 가격 추가
        assertAll(
                HTTP_STATUS_검증(HttpStatus.OK, 응답),
                () -> assertThat(메뉴_응답_리스트).hasSize(2),
                () -> assertThat(메뉴상품_리스트_1).hasSize(1),
                () -> assertThat(메뉴상품_리스트_2).hasSize(2),
                리스트_검증(메뉴_응답_리스트,
                        row("name", "후라이드 + 후라이드", "양념 + 간장"),
                        row("menuGroupId", 메뉴그룹.getId(), 메뉴그룹.getId().longValue()))
        );
    }
}
