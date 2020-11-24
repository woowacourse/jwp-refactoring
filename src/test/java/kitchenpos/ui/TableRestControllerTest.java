package kitchenpos.ui;

import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class TableRestControllerTest extends AbstractControllerTest {
    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        OrderTableCreateRequest orderTableCreateRequest = createOrderTableRequest(true, 0);

        mockMvc.perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(orderTableCreateRequest))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath(("$.tableGroupId")).doesNotExist())
            .andExpect(
                jsonPath("$.numberOfGuests").value(orderTableCreateRequest.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(orderTableCreateRequest.isEmpty()));
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        List<OrderTableResponse> orderTables = OrderTableResponse.listOf(orderTableRepository.findAll());

        String json = mockMvc.perform(get("/api/tables"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        List<OrderTableResponse> response = objectMapper.readValue(
            json,
            objectMapper.getTypeFactory()
                .constructCollectionType(List.class, OrderTableResponse.class)
        );

        assertThat(response).usingFieldByFieldElementComparator().containsAll(orderTables);
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 수 있다.")
    @Test
    void changeEmpty() throws Exception {
        OrderTable orderTable = orderTableRepository.save(createOrderTable(null, true, 0, null));
        OrderTableCreateRequest orderTableCreateRequest = createOrderTableRequest(false, 1);

        mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTable.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(orderTableCreateRequest))
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(orderTable.getId()))
            .andExpect(jsonPath(("$.tableGroupId")).value(orderTable.getTableGroupId()))
            .andExpect(jsonPath("$.numberOfGuests").value(orderTable.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(orderTableCreateRequest.isEmpty()));
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeOrderStatus() throws Exception {
        OrderTable orderTable = orderTableRepository.save(createOrderTable(null, false, 2, null));
        OrderTableCreateRequest orderTableCreateRequest = createOrderTableRequest(true, 1);

        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(orderTableCreateRequest))
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(orderTable.getId()))
            .andExpect(jsonPath(("$.tableGroupId")).value(orderTable.getTableGroupId()))
            .andExpect(jsonPath("$.numberOfGuests")
                .value(orderTableCreateRequest.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(orderTable.isEmpty()));
    }
}
