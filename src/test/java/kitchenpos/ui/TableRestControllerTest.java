package kitchenpos.ui;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_1;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_2;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_7;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void create() throws Exception {
        OrderTable request = new OrderTable();
        request.setNumberOfGuests(10);
        request.setEmpty(true);

        String response = mockMvc.perform(post("/api/tables")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        OrderTable responseTable = mapper.readValue(response, OrderTable.class);

        assertAll(
            () -> assertThat(responseTable.getId()).isNotNull(),
            () -> assertThat(responseTable).isEqualToComparingOnlyGivenFields(request, "numberOfGuests", "empty")
        );
    }

    @Test
    void list() throws Exception {
        orderTableDao.save(ORDER_TABLE_FIXTURE_1);
        orderTableDao.save(ORDER_TABLE_FIXTURE_2);

        String response = mockMvc.perform(get("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<OrderTable> responseTables = mapper.readValue(response, mapper.getTypeFactory()
            .constructCollectionType(List.class, OrderTable.class));

        assertThat(responseTables).usingElementComparatorIgnoringFields("id", "tableGroupId")
            .containsAll(Arrays.asList(ORDER_TABLE_FIXTURE_1, ORDER_TABLE_FIXTURE_2));
    }

    @Test
    void changeEmpty() throws Exception {
        OrderTable savedTable = orderTableDao.save(ORDER_TABLE_FIXTURE_1);
        String url = String.format("/api/tables/%d/empty", savedTable.getId());

        OrderTable request = new OrderTable();
        request.setNumberOfGuests(1);
        request.setEmpty(false);

        String response = mockMvc.perform(put(url)
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        OrderTable responseTable = mapper.readValue(response, OrderTable.class);

        assertThat(responseTable).isEqualToComparingOnlyGivenFields(request, "empty");
    }

    @Test
    void changeNumberOfGuests() throws Exception {
        OrderTable savedTable = orderTableDao.save(ORDER_TABLE_FIXTURE_7);
        String url = String.format("/api/tables/%d/number-of-guests", savedTable.getId());

        OrderTable request = new OrderTable();
        request.setNumberOfGuests(20);
        request.setEmpty(true);

        String response = mockMvc.perform(put(url)
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        OrderTable responseTable = mapper.readValue(response, OrderTable.class);

        assertThat(responseTable).isEqualToComparingOnlyGivenFields(request, "numberOfGuests");
    }
}