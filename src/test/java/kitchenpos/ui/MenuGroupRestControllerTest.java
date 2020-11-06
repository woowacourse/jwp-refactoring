package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;

@DisplayName("Menu Group controller 클래스")
@WebMvcTest(controllers = MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    private final MenuGroup menuGroup = MenuGroupFixture.createWithId(1L);

    @DisplayName("Menu group을 DB에 저장하고, 아이디를 포함하여 반환한다.")
    @Test
    void it_returns_with_id() throws Exception {
        when(menuGroupService.create(any())).thenReturn(menuGroup);

        mockMvc.perform(post("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(menuGroup))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("location", "/api/menu-groups/" + menuGroup.getId()))
            .andExpect(jsonPath("id").value(menuGroup.getId()));
    }

    @DisplayName("DB에 저장된 모든 Menu Group을 리턴한다")
    @Test
    void it_returns_all_groups() throws Exception {
        when(menuGroupService.list()).thenReturn(Collections.singletonList(menuGroup));

        mockMvc.perform(get("/api/menu-groups")
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].id").value(menuGroup.getId()));
    }

}
