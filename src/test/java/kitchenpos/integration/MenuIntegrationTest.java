package kitchenpos.integration;

import static kitchenpos.integration.api.texture.ProductTexture.*;

import java.util.Collections;
import java.util.List;
import kitchenpos.integration.api.MenuApi;
import kitchenpos.integration.api.MenuGroupApi;
import kitchenpos.integration.api.ProductApi;
import kitchenpos.integration.utils.MockMvcResponse;
import kitchenpos.ui.request.MenuProductRequest;
import kitchenpos.application.response.MenuResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class MenuIntegrationTest extends IntegrationTest {

    @Autowired
    private MenuApi menuApi;
    @Autowired
    private MenuGroupApi menuGroupApi;
    @Autowired
    private ProductApi productApi;

    @Test
    public void 메뉴_등록_성공() {
        //given
        final Long menuGroupId = menuGroupApi.메뉴_그룹_등록("존맛탱").getContent().getId();
        final Long productId = productApi.상품_등록(민초치킨).getContent().getId();

        final MenuProductRequest menuProduct = MenuProductRequest.create(productId, 1L);

        //when
        final MockMvcResponse<MenuResponse> result = menuApi.메뉴_등록(민초치킨,
            menuGroupId,
                Collections.singletonList(menuProduct));

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(result.getContent().getName()).isEqualTo(민초치킨.getProduct().getName());
    }

    @Test
    public void 메뉴_등록_실패_notFoundMenuGroup() {
        //given
        final Long productId = productApi.상품_등록(민초치킨).getContent().getId();

        final MenuProductRequest menuProduct = MenuProductRequest.create(productId, 1L);

        //when
        final MockMvcResponse<MenuResponse> result = menuApi.메뉴_등록(민초치킨, Long.MAX_VALUE, Collections.singletonList(menuProduct));

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void 메뉴_등록_실패_notFoundProduct() {
        //given
        final Long menuGroupId = menuGroupApi.메뉴_그룹_등록("존맛탱").getContent().getId();
        final MenuProductRequest menuProduct = MenuProductRequest.create(Long.MAX_VALUE, 1L);

        // when
        final MockMvcResponse<MenuResponse> result = menuApi.메뉴_등록(민초치킨, menuGroupId,
            Collections.singletonList(menuProduct));

        // then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("메뉴 등록 실패 - 상품 가격보다 메뉴 가격이 더 클 때")
    public void 메뉴_등록_실패_wrongPrice() {
        //given
        final Long menuGroupId = menuGroupApi.메뉴_그룹_등록("존맛탱").getContent().getId();
        final MenuProductRequest menuProduct = MenuProductRequest.create(Long.MAX_VALUE, 1L);

        // when
        final MockMvcResponse<MenuResponse> result = menuApi.메뉴_등록(
            민초치킨,
            menuGroupId,
            Collections.singletonList(menuProduct));

        // then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void 메뉴_조회() {
        //when
        final MockMvcResponse<List<MenuResponse>> result = menuApi.메뉴_조회();

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(result.getContent()).hasSize(6);
        Assertions.assertThat(result.getContent())
            .extracting(MenuResponse::getName)
            .containsExactlyInAnyOrder("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
    }
}
