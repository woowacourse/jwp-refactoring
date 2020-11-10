package kitchenpos.ui;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_1;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_2;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void create() throws Exception {
        OrderTable table1 = orderTableDao.save(ORDER_TABLE_FIXTURE_1);
        OrderTable table2 = orderTableDao.save(ORDER_TABLE_FIXTURE_2);
        OrderTable table3 = orderTableDao.save(ORDER_TABLE_FIXTURE_3);

        TableGroup request = new TableGroup();
        request.setCreatedDate(LocalDateTime.of(2020, 1, 1, 1, 1));
        request.setOrderTables(Arrays.asList(table1, table2, table3));

        String response = mockMvc.perform(post("/api/table-groups")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        TableGroup responseTableGroup = mapper.readValue(response, TableGroup.class);

        assertAll(
            () -> assertThat(responseTableGroup.getOrderTables()).usingElementComparatorOnFields("numberOfGuests")
                .isEqualTo(request.getOrderTables()),
            () -> assertThat(responseTableGroup.getOrderTables())
                .allSatisfy(orderTable -> assertThat(orderTable.isEmpty()).isFalse())
        );
    }

    @Test
    void ungroup() throws Exception {
        OrderTable table1 = orderTableDao.save(ORDER_TABLE_FIXTURE_1);
        OrderTable table2 = orderTableDao.save(ORDER_TABLE_FIXTURE_2);
        OrderTable table3 = orderTableDao.save(ORDER_TABLE_FIXTURE_3);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.of(2020, 1, 1, 1, 1));
        tableGroup.setOrderTables(Arrays.asList(table1, table2, table3));

        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        String url = String.format("/api/table-groups/%d", savedTableGroup.getId());

        mockMvc.perform(delete(url)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andReturn();

        List<OrderTable> orderTables = orderTableDao
            .findAllByIdIn(Arrays.asList(table1.getId(), table2.getId(), table3.getId()));

        assertThat(orderTables).allSatisfy(orderTable -> assertThat(orderTable.getTableGroupId()).isNull());
    }
}