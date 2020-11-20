package kitchenpos.ui;

import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static kitchenpos.fixture.TableGroupFixture.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class TableGroupRestControllerTest extends AbstractControllerTest {
    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("단체 지정을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        List<OrderTable> orderTables = Arrays.asList(
            orderTableDao.save(createOrderTable(null, true, 0, null)),
            orderTableDao.save(createOrderTable(null, true, 0, null))
        );
        TableGroupCreateRequest tableGroupCreateRequest = createTableGroupRequest(orderTables);

        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(tableGroupCreateRequest))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.createdDate").exists())
            .andExpect(jsonPath("$.orderTables").isNotEmpty());
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroup() throws Exception {
        TableGroup tableGroup = tableGroupDao
            .save(createTableGroup(null, LocalDateTime.now()));

        orderTableDao.save(createOrderTable(null, false, 0, tableGroup.getId()));
        orderTableDao.save(createOrderTable(null, false, 0, tableGroup.getId()));

        mockMvc.perform(delete("/api/table-groups/{id}", tableGroup.getId()))
            .andDo(print())
            .andExpect(status().isNoContent());

        assertThat(orderTableDao.findAllByTableGroupId(tableGroup.getId())).isEmpty();
    }
}
