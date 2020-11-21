package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.table.OrderTableFindAllResponse;
import kitchenpos.dto.table.OrderTableFindAllResponses;
import kitchenpos.dto.table.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.table.OrderTableUpdateEmptyResponse;
import kitchenpos.dto.table.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.dto.table.OrderTableUpdateNumberOfGuestsResponse;
import kitchenpos.dto.tableGroup.OrderTableCreateRequest;
import kitchenpos.dto.tableGroup.OrderTableCreateResponse;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@MockBean
	private TableService tableService;

	private OrderTable orderTable;

	private OrderTableCreateRequest orderTableCreateRequest;

	private OrderTableCreateResponse orderTableCreateResponse;

	private OrderTableFindAllResponse orderTableFindAllResponse;

	private OrderTableFindAllResponses orderTableFindAllResponses;

	private OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest;

	private OrderTableUpdateEmptyResponse orderTableUpdateEmptyResponse;

	private OrderTableUpdateNumberOfGuestsRequest orderTableUpdateNumberOfGuestsRequest;

	private OrderTableUpdateNumberOfGuestsResponse orderTableUpdateNumberOfGuestsResponse;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		objectMapper = new ObjectMapper();

		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.build();

		orderTable = new OrderTable(1L, 1L, 1, true);

		orderTableCreateRequest = new OrderTableCreateRequest(null, null, 1, true);

		orderTableCreateResponse = new OrderTableCreateResponse(1L, null, 1, true);

		orderTableFindAllResponse = new OrderTableFindAllResponse(1L, null, 1, true);

		orderTableFindAllResponses = new OrderTableFindAllResponses(
			Collections.singletonList(orderTableFindAllResponse));

		orderTableUpdateEmptyRequest = new OrderTableUpdateEmptyRequest(false);

		orderTableUpdateEmptyResponse = new OrderTableUpdateEmptyResponse(1L, 1L, 1, false);

		orderTableUpdateNumberOfGuestsRequest = new OrderTableUpdateNumberOfGuestsRequest(3);

		orderTableUpdateNumberOfGuestsResponse = new OrderTableUpdateNumberOfGuestsResponse(1L, 1L, 3, true);

	}

	@Test
	void create() throws Exception {
		given(tableService.create(any(OrderTableCreateRequest.class))).willReturn(orderTableCreateResponse);

		mockMvc.perform(post("/api/tables")
			.content(objectMapper.writeValueAsString(orderTableCreateRequest))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/tables/1"));
	}

	@Test
	void list() throws Exception {
		given(tableService.list()).willReturn(orderTableFindAllResponses);

		mockMvc.perform(get("/api/tables"))
			.andExpect(status().isOk());
	}

	@Test
	void changeEmpty() throws Exception {
		given(tableService.changeEmpty(anyLong(), any(OrderTableUpdateEmptyRequest.class))).willReturn(
			orderTableUpdateEmptyResponse);

		mockMvc.perform(put("/api/tables/1/empty")
			.content(objectMapper.writeValueAsString(orderTableUpdateEmptyRequest))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	void changeNumberOfGuests() throws Exception {
		given(
			tableService.changeNumberOfGuests(anyLong(), any(OrderTableUpdateNumberOfGuestsRequest.class))).willReturn(
			orderTableUpdateNumberOfGuestsResponse);

		mockMvc.perform(put("/api/tables/1/number-of-guests")
			.content(objectMapper.writeValueAsString(orderTableUpdateNumberOfGuestsRequest))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}