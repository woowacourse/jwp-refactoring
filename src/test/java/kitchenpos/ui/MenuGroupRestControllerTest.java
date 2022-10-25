package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupMapper;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class MenuGroupRestControllerTest extends RestControllerTest {

    @Autowired
    private MenuGroupMapper menuGroupMapper;

    @Test
    void 메뉴_그룹_생성에_성공한다() throws Exception {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("메뉴 그룹");
        when(menuGroupService.create(menuGroupMapper.menuGroupCreateRequestToMenuGroup(menuGroupCreateRequest)))
                .thenReturn(new MenuGroup(1L, "메뉴 그룹"));

        mockMvc.perform(post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(menuGroupCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    void 메뉴_그룹_목록_조회에_성공한다() throws Exception {
        MenuGroup expected = new MenuGroup(1L, "메뉴 그룹");
        when(menuGroupService.list()).thenReturn(List.of(expected));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<MenuGroupResponse> menuGroupResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(), new TypeReference<List<MenuGroupResponse>>() {
                });

        assertThat(menuGroupResponses).hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactly(new MenuGroupResponse(expected.getId(), expected.getName()));
    }
}
