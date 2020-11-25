package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.TableGroupService;
import kitchenpos.dao.TableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TableDao tableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void create() throws Exception {
        Table table1 = tableDao.save(new Table(4, true));
        Table table2 = tableDao.save(new Table(5, true));
        Table table3 = tableDao.save(new Table(6, true));

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(table1.getId(), table2.getId(), table3.getId()));

        String response = mockMvc.perform(post("/api/table-groups")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        TableGroupResponse responseTableGroup = mapper.readValue(response, TableGroupResponse.class);
        List<Long> responseTableIds = responseTableGroup.getTables()
            .stream()
            .map(TableResponse::getId)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(responseTableIds).isEqualTo(request.getTableIds()),
            () -> assertThat(responseTableGroup.getTables())
                .allSatisfy(orderTable -> assertThat(orderTable.isEmpty()).isFalse())
        );
    }

    @Test
    void create_emptyBody_exception() throws Exception {
        TableGroupCreateRequest request = new TableGroupCreateRequest(new ArrayList<>());

        mockMvc.perform(post("/api/table-groups")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void create_alreadyGrouped_exception() throws Exception {
        Table table1 = tableDao.save(new Table(4, true));
        Table table2 = tableDao.save(new Table(5, true));
        Table table3 = tableDao.save(new Table(6, true));
        TableGroupCreateRequest before = new TableGroupCreateRequest(
            Arrays.asList(table1.getId(), table2.getId()));
        tableGroupService.create(before);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(table1.getId(), table2.getId(), table3.getId()));

        mockMvc.perform(post("/api/table-groups")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void ungroup() throws Exception {
        Table table1 = tableDao.save(new Table(4, true));
        Table table2 = tableDao.save(new Table(5, true));
        Table table3 = tableDao.save(new Table(6, true));

        TableGroup tableGroup = new TableGroup(LocalDateTime.of(2020, 1, 1, 1, 1),
            Arrays.asList(table1, table2, table3));
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        table1.putInGroup(savedTableGroup.getId());
        table2.putInGroup(savedTableGroup.getId());
        table3.putInGroup(savedTableGroup.getId());
        tableDao.save(table1);
        tableDao.save(table2);
        tableDao.save(table3);

        String url = String.format("/api/table-groups/%d", savedTableGroup.getId());

        mockMvc.perform(delete(url)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();

        List<Table> tables = tableDao
            .findAllByIdIn(Arrays.asList(table1.getId(), table2.getId(), table3.getId()));

        assertThat(tables).allSatisfy(orderTable -> assertThat(orderTable.getTableGroupId()).isNull());
    }

    @Test
    void ungroup_emptyParam_exception() throws Exception {
        String url = String.format("/api/table-groups/%d", null);

        mockMvc.perform(delete(url)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();
    }
}