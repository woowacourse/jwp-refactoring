package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.KitchenPosTestFixture;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = TableGroupRestController.class)
@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends KitchenPosTestFixture {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    private TableGroup firstTableGroup;
    private OrderTable firstOrderTable;
    private OrderTable secondOrderTable;

    @BeforeEach
    void setUp() {
        firstOrderTable = 주문_테이블을_저장한다(1L, 1L, 3, false);
        secondOrderTable = 주문_테이블을_저장한다(2L, 1L, 4, false);
        firstTableGroup = 테이블_그룹을_저장한다(1L, LocalDateTime.now(), Arrays.asList(firstOrderTable, secondOrderTable));
    }

    @Test
    void create() throws Exception {
        // given
        // when
        given(tableGroupService.create(any(TableGroup.class))).willReturn(firstTableGroup);

        // then
        mvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstTableGroup)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(firstTableGroup.getId().intValue())))
                .andExpect(jsonPath("$.createdDate", is(firstTableGroup.getCreatedDate().toString())))
                .andExpect(jsonPath("$.orderTables", hasSize(2)));
    }

    @Test
    void ungroup() throws Exception {
        // given
        // when
        doNothing().when(tableGroupService).ungroup(any(Long.class));

        // then
        mvc.perform(delete("/api/table-groups/{tableGroupId}", 1L))
                .andExpect(status().isNoContent());
    }
}