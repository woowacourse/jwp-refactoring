//package kitchenpos.ui;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.List;
//import kitchenpos.application.MenuGroupService;
//import kitchenpos.domain.MenuGroup;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(MenuGroupRestController.class)
//class MenuGroupRestControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private MenuGroupService menuGroupService;
//
//    @Test
//    void create() throws Exception {
//        // given
//        final MenuGroup result = mock(MenuGroup.class);
//        given(menuGroupService.create(any())).willReturn(result);
//        given(result.getId()).willReturn(1L);
//        final MenuGroup request = new MenuGroup("chicken-group");
//
//        // when
//        mockMvc.perform(post("/api/menu-groups")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(request)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(header().string("location", "/api/menu-groups/1"));
//    }
//
//    @Test
//    void list() throws Exception {
//        // given
//        final MenuGroup resultA = new MenuGroup("chicken-group");
//        final MenuGroup resultB = new MenuGroup("chicken-group");
//        given(menuGroupService.list()).willReturn(List.of(resultA, resultB));
//
//        // when
//        mockMvc.perform(get("/api/menu-groups"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(objectMapper.writeValueAsString(List.of(resultA, resultB))));
//    }
//}
