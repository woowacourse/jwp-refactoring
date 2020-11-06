package kitchenpos.acceptance;

import static java.lang.Long.*;
import static java.util.Arrays.*;
import static java.util.Objects.*;
import static kitchenpos.adapter.presentation.web.OrderTableRestController.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.factory.OrderFactory;
import kitchenpos.factory.OrderLineItemFactory;
import kitchenpos.factory.TableGroupFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AcceptanceTest {
    protected static final String LOCATION = "Location";
    private static final String DELIMITER = "/";

    @Autowired
    protected OrderFactory orderFactory;
    @Autowired
    protected OrderLineItemFactory orderLineItemFactory;
    @Autowired
    protected TableGroupFactory tableGroupFactory;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    protected MockMvc mockMvc;

    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    protected Long extractId(String location) {
        List<String> tokens = asList(location.split(DELIMITER));
        int indexOfId = tokens.size() - 1;
        String id = tokens.get(indexOfId);
        return parseLong(id);
    }

    protected <T> T getLastItem(List<T> items) {
        return items.get(items.size() - 1);
    }

    protected Long post(String request, String uri) throws Exception {
        return extractId(requireNonNull(
                mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getHeader(LOCATION)));
    }

    protected <T> List<T> getAll(Class<T> clazz, String uri) throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, clazz));
    }

    protected <T> T put(Class<T> clazz, String request, String uri) throws Exception {
        try {
            String response = mockMvc.perform(MockMvcRequestBuilders.put(uri)
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            return objectMapper.readValue(response, clazz);
        } catch (MismatchedInputException e) {
            // TODO: 2020/11/05 put 제거
            return null;
        }
    }

    protected void delete(String uri, Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(uri + "/" + id))
                .andExpect(status().isNoContent());
    }

    protected OrderTableResponse changeOrderTableEmpty(boolean empty, Long orderTableId) throws Exception {
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(empty);

        return put(OrderTableResponse.class, objectMapper.writeValueAsString(request), API_TABLES + "/" + orderTableId + "/empty");
    }
}
