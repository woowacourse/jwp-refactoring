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

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@MockBean
	private TableService tableService;

	private OrderTable orderTable;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		objectMapper = new ObjectMapper();

		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.build();

		orderTable = new OrderTable();
		orderTable.setId(1L);
	}

	@Test
	void create() throws Exception {
		given(tableService.create(any(OrderTable.class))).willReturn(orderTable);

		mockMvc.perform(post("/api/tables")
			.content(objectMapper.writeValueAsString(orderTable))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/tables/1"));
	}

	@Test
	void list() throws Exception {
		given(tableService.list()).willReturn(Collections.singletonList(orderTable));

		mockMvc.perform(get("/api/tables"))
			.andExpect(status().isOk());
	}

	@Test
	void changeEmpty() throws Exception {
		given(tableService.changeEmpty(anyLong(), any(OrderTable.class))).willReturn(orderTable);

		mockMvc.perform(put("/api/tables/1/empty")
			.content(objectMapper.writeValueAsString(orderTable))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	void changeNumberOfGuests() throws Exception {
		given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).willReturn(orderTable);

		mockMvc.perform(put("/api/tables/1/number-of-guests")
			.content(objectMapper.writeValueAsString(orderTable))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}