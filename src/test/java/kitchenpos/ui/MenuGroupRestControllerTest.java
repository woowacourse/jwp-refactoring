package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuGroupRestController.class)
@AutoConfigureRestDocs
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuGroupService menuGroupService;

    private static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris()
                        .scheme("http")
                        .host("localhost")
                        .removePort(),
                prettyPrint());
    }

    private static RestDocumentationResultHandler toDocument(String title) {
        return document(title, getDocumentRequest(), preprocessResponse(prettyPrint()));
    }

    @DisplayName("메뉴 그룹 저장 - 성공")
    @Test
    void menu_group_create() throws Exception {
        //given
        final MenuGroup requestMenuGroup = new MenuGroup();
        requestMenuGroup.setName("검프의 신메뉴");
        final MenuGroup responseMenuGroup = new MenuGroup();
        responseMenuGroup.setId(1L);
        responseMenuGroup.setName("검프의 신메뉴");
        //when
        willReturn(responseMenuGroup).given(menuGroupService).create(any(MenuGroup.class));
        final ResultActions result = 메뉴_그룹_저장_요청(requestMenuGroup);
        //then
        메뉴_그룹_저장_성공함(result, responseMenuGroup);
    }

    @DisplayName("메뉴 그룹 조회 - 성공")
    @Test
    void menu_group_findAll() throws Exception {
        //given
        final MenuGroup responseMenuGroup = new MenuGroup();
        responseMenuGroup.setId(1L);
        responseMenuGroup.setName("검프의 신메뉴");
        final MenuGroup responseMenuGroup2 = new MenuGroup();
        responseMenuGroup2.setId(2L);
        responseMenuGroup2.setName("검프의 구메뉴");
        final MenuGroup responseMenuGroup3 = new MenuGroup();
        responseMenuGroup3.setId(3L);
        responseMenuGroup3.setName("검프의 뀨메뉴");

        final List<MenuGroup> menuGroups = Arrays.asList(responseMenuGroup, responseMenuGroup2, responseMenuGroup3);
        //when
        willReturn(menuGroups).given(menuGroupService).list();
        final ResultActions result = 메뉴_그룹_조회_요청();
        //then
        메뉴_그룹_조회_성공함(result, menuGroups);
    }

    private ResultActions 메뉴_그룹_저장_요청(MenuGroup requestMenuGroup) throws Exception {
        return mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(requestMenuGroup))
        );
    }

    private ResultActions 메뉴_그룹_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private ResultActions 메뉴_그룹_저장_성공함(ResultActions result, MenuGroup responseMenuGroup) throws Exception {
        return result.andExpect(status().isCreated())
                .andExpect(content().json(toJson(responseMenuGroup)))
                .andExpect(header().string("Location", "/api/menu-groups/" + responseMenuGroup.getId()))
                .andDo(print())
                .andDo(toDocument("menu-group-create"));
    }

    private void 메뉴_그룹_조회_성공함(ResultActions result, List<MenuGroup> responseMenuGroup) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(responseMenuGroup)))
                .andDo(print())
                .andDo(toDocument("menu-group-findAll"));
    }

    private String toJson(Object object) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("직렬화 오류입니당");
        }
    }
}