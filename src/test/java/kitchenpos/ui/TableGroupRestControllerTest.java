package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.Constructor;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends Constructor {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("단체석을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        OrderTable tableA = orderTableConstructor(1L, null, 4, true);
        OrderTable tableB = orderTableConstructor(2L, null, 2, true);
        OrderTable tableC = orderTableConstructor(3L, null, 6, true);
        List<OrderTable> orderTables = Arrays.asList(tableA, tableB, tableC);
        TableGroup tableGroup = tableGroupConstructor(orderTables);
        TableGroup expected = tableGroupConstructor(1L, LocalDateTime.now(), orderTables);
        given(tableGroupService.create(any(TableGroup.class))).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectToJson(tableGroup))
        );

        //then
        response.andExpect(status().isCreated())
            .andExpect(header().string("Location", String.format("/api/table-groups/%s", expected.getId())))
            .andExpect(content().json(objectToJson(expected)));
    }

    @DisplayName("단체석을 해제한다.")
    @Test
    void ungroup() throws Exception {
        //given
        Long tableGroupId = 10L;
        willDoNothing().given(tableGroupService).ungroup(tableGroupId);

        //when
        ResultActions response = mockMvc.perform(delete(String.format("/api/table-groups/%s", tableGroupId)));

        //then
        response.andExpect(status().isNoContent());
    }
}
