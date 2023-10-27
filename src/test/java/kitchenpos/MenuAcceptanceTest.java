package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ui.request.MenuProductCreateRequest;
import kitchenpos.ui.response.MenuGroupResponse;
import kitchenpos.ui.response.MenuResponse;
import kitchenpos.ui.response.ProductResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.step.MenuGroupStep.MENU_GROUP_REQUEST_일식;
import static kitchenpos.step.MenuGroupStep.메뉴_그룹_생성_요청하고_메뉴_그룹_반환;
import static kitchenpos.step.MenuStep.MENU_CREATE_REQUEST_스키야키;
import static kitchenpos.step.MenuStep.메뉴_생성_요청;
import static kitchenpos.step.MenuStep.메뉴_생성_요청하고_아이디_반환;
import static kitchenpos.step.MenuStep.메뉴_조회_요청;
import static kitchenpos.step.ProductStep.PRODUCT_CREATE_REQUEST_스키야키;
import static kitchenpos.step.ProductStep.상품_생성_요청하고_상품_반환;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

class MenuAcceptanceTest extends AcceptanceTest {

    @Nested
    class MenuCreateTest {

        @Test
        void 메뉴를_생성한다() {
            final MenuGroupResponse menuGroup = 메뉴_그룹_생성_요청하고_메뉴_그룹_반환(MENU_GROUP_REQUEST_일식);
            final ProductResponse product = 상품_생성_요청하고_상품_반환(PRODUCT_CREATE_REQUEST_스키야키);

            final ExtractableResponse<Response> response = 메뉴_생성_요청(
                    MENU_CREATE_REQUEST_스키야키(
                            BigDecimal.valueOf(11_900),
                            menuGroup.getId(),
                            List.of(new MenuProductCreateRequest(product.getId(), 1L))
                    )
            );

            assertThat(response.statusCode()).isEqualTo(CREATED.value());
        }

        @Test
        void 메뉴는_반드시_메뉴_그룹에_속해야_한다() {
            final ProductResponse product = 상품_생성_요청하고_상품_반환(PRODUCT_CREATE_REQUEST_스키야키);

            final ExtractableResponse<Response> response = 메뉴_생성_요청(
                    MENU_CREATE_REQUEST_스키야키(
                            BigDecimal.valueOf(11_900),
                            null,
                            List.of(new MenuProductCreateRequest(product.getId(), 1L))
                    )
            );

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @Test
        void 메뉴의_가격은_메뉴에_속하는_상품_곱하기_수량의_합_이하여야_한다() {
            final MenuGroupResponse menuGroup = 메뉴_그룹_생성_요청하고_메뉴_그룹_반환(MENU_GROUP_REQUEST_일식);
            final ProductResponse product = 상품_생성_요청하고_상품_반환(PRODUCT_CREATE_REQUEST_스키야키);

            final MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(product.getId(), 2L);
            final BigDecimal inappropriatePrice = product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())).add(BigDecimal.TEN);

            final ExtractableResponse<Response> response = 메뉴_생성_요청(
                    MENU_CREATE_REQUEST_스키야키(
                            inappropriatePrice,
                            menuGroup.getId(),
                            List.of(menuProductRequest)
                    )
            );

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }
    }

    @Test
    void 메뉴를_조회한다() {
        final MenuGroupResponse menuGroup = 메뉴_그룹_생성_요청하고_메뉴_그룹_반환(MENU_GROUP_REQUEST_일식);
        final ProductResponse product = 상품_생성_요청하고_상품_반환(PRODUCT_CREATE_REQUEST_스키야키);

        final Long menuId = 메뉴_생성_요청하고_아이디_반환(
                MENU_CREATE_REQUEST_스키야키(
                        BigDecimal.valueOf(11_900),
                        menuGroup.getId(),
                        List.of(new MenuProductCreateRequest(product.getId(), 1L))
                )
        );

        final ExtractableResponse<Response> response = 메뉴_조회_요청();
        final List<MenuResponse> result = response.jsonPath().getList("", MenuResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.get(0).getId()).isEqualTo(menuId)
        );
    }
}
