package kitchenpos.table.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.ui.dto.request.TableCreateDto;
import kitchenpos.table.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.table.ui.dto.response.TableGroupCreateResponse;
import kitchenpos.table.ui.TableGroupRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                Arrays.asList(new TableCreateDto(1L), new TableCreateDto(2L)));
        given(tableGroupService.create(any())).willReturn(
                new TableGroupCreateResponse(1L, LocalDateTime.now(), new ArrayList<>()));

        // when
        ResultActions perform = mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated());
    }

    @DisplayName("단체 지정을 생성할 때 주문테이블이 2개 이하라면 예외를 반환한다.")
    @Test
    void create_fail_if_orderTable_is_one() throws Exception {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                Collections.singletonList(new TableCreateDto(1L)));
        given(tableGroupService.create(any())).willReturn(
                new TableGroupCreateResponse(1L, LocalDateTime.now(), new ArrayList<>()));

        // when
        ResultActions perform = mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform.andExpect(status().isBadRequest());
    }

    @DisplayName("단체 지정을 생성할 때 빈 주문테이블이라면 예외를 반환한다.")
    @Test
    void create_fail_if_emptyOrderTable() throws Exception {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(new ArrayList<>());
        given(tableGroupService.create(any())).willReturn(
                new TableGroupCreateResponse(1L, LocalDateTime.now(), new ArrayList<>()));

        // when
        ResultActions perform = mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform.andExpect(status().isBadRequest());
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(delete("/api/table-groups/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print());

        // then
        perform.andExpect(status().isNoContent());
    }
}
