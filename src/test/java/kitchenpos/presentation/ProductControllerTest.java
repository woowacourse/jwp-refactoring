package kitchenpos.presentation;

import static kitchenpos.application.fixture.dto.ProductDtoFixture.짜장면_요청;
import static kitchenpos.application.fixture.dto.ProductDtoFixture.짜장면_응답;
import static kitchenpos.application.fixture.dto.ProductDtoFixture.짬뽕_응답;
import static kitchenpos.application.fixture.dto.ProductDtoFixture.탕수육_응답;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class ProductControllerTest extends ControllerTest {

    @Test
    @DisplayName("상품을 등록한다.")
    void create() throws Exception {
        // given
        final String request = objectMapper.writeValueAsString(짜장면_요청);
        given(productService.create(any()))
                .willReturn(짜장면_응답);

        // when
        final ResultActions perform = mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("id", notNullValue()));
    }

    @Test
    @DisplayName("상품을 조회한다.")
    void list() throws Exception {
        // given
        given(productService.list())
                .willReturn(List.of(짜장면_응답, 짬뽕_응답, 탕수육_응답));

        // when
        final ResultActions perform = mockMvc.perform(get("/api/products"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
}
