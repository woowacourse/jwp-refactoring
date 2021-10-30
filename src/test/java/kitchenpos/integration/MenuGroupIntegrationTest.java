package kitchenpos.integration;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.integration.api.MenuGroupApi;
import kitchenpos.integration.utils.MockMvcResponse;
import kitchenpos.ui.response.MenuGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class MenuGroupIntegrationTest extends IntegrationTest {

    @Autowired
    private MenuGroupApi menuGroupApi;

    @Test
    public void 메뉴_그룹_등록_성공() {
        //given
        final String menuGroupName = "추천메뉴";

        //when
        final MockMvcResponse<MenuGroupResponse> result = menuGroupApi.메뉴_그룹_등록(menuGroupName);

        //then
        Assertions.assertThat(result.getContent().getName()).isEqualTo(menuGroupName);
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void 메뉴_그룹_조회() {
        //given

        //when
        final MockMvcResponse<List<MenuGroupResponse>> result = menuGroupApi.메뉴_그룹_조회();

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(result.getContent())
            .extracting(MenuGroupResponse::getName)
            .containsExactlyInAnyOrder("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴");
    }
}
