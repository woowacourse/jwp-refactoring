package kitchenpos.ui;

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
import kitchenpos.application.TableService;
import kitchenpos.dto.TableChangeRequest;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableResponse;
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
    private TableService tableService;

    @Test
    void create() throws Exception {
        TableCreateRequest request = new TableCreateRequest(false, 10);

        String response = mockMvc.perform(post("/api/tables")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        TableResponse responseTable = mapper.readValue(response, TableResponse.class);

        assertAll(
            () -> assertThat(responseTable.getId()).isNotNull(),
            () -> assertThat(responseTable).isEqualToComparingOnlyGivenFields(request, "numberOfGuests", "empty")
        );
    }

    @Test
    void list() throws Exception {
        TableResponse savedTable = tableService.create(new TableCreateRequest(false, 4));
        TableResponse savedTable2 = tableService.create(new TableCreateRequest(false, 5));

        String response = mockMvc.perform(get("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<TableResponse> responseTables = mapper.readValue(response, mapper.getTypeFactory()
            .constructCollectionType(List.class, TableResponse.class));

        assertThat(responseTables).usingElementComparatorIgnoringFields("id", "tableGroupId")
            .containsAll(Arrays.asList(savedTable, savedTable2));
    }

    @Test
    void changeEmpty() throws Exception {
        TableResponse savedTable = tableService.create(new TableCreateRequest(false, 4));
        String url = String.format("/api/tables/%d/empty", savedTable.getId());

        TableChangeRequest request = new TableChangeRequest(true);

        String response = mockMvc.perform(put(url)
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        TableResponse responseTable = mapper.readValue(response, TableResponse.class);

        assertThat(responseTable).isEqualToComparingOnlyGivenFields(request, "empty");
    }

    @Test
    void changeNumberOfGuests() throws Exception {
        TableResponse savedTable = tableService.create(new TableCreateRequest(false, 10));
        String url = String.format("/api/tables/%d/number-of-guests", savedTable.getId());

        TableChangeRequest request = new TableChangeRequest(20);

        String response = mockMvc.perform(put(url)
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        TableResponse responseTable = mapper.readValue(response, TableResponse.class);

        assertThat(responseTable).isEqualToComparingOnlyGivenFields(request, "numberOfGuests");
    }
}