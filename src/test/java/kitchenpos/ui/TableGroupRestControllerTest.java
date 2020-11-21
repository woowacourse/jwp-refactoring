package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.tableGroup.OrderTableCreateRequests;
import kitchenpos.dto.tableGroup.TableGroupCreateRequest;
import kitchenpos.dto.tableGroup.TableGroupCreateResponse;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
	@MockBean
	private TableGroupService tableGroupService;

	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	private TableGroup tableGroup;

	private TableGroupCreateRequest tableGroupCreateRequest;

	private TableGroupCreateResponse tableGroupCreateResponse;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());

		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.build();

		OrderTable orderTable = new OrderTable(1L, 1L, 2, true);

		tableGroup = new TableGroup(1L, LocalDateTime.of(2020, 10, 28, 16, 40),
			new OrderTables(Collections.singletonList(orderTable)));

		tableGroupCreateRequest = new TableGroupCreateRequest(1L, LocalDateTime.of(2020, 10, 28, 16, 40),
			OrderTableCreateRequests.from(new OrderTables(Collections.singletonList(orderTable))));

		tableGroupCreateResponse = new TableGroupCreateResponse(tableGroup);
	}

	@Test
	void create() throws Exception {
		given(tableGroupService.create(any(TableGroupCreateRequest.class))).willReturn(tableGroupCreateResponse);

		mockMvc.perform(post("/api/table-groups")
			.content(objectMapper.writeValueAsString(tableGroupCreateRequest))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/table-groups/1"));
	}

	@Test
	void ungroup() throws Exception {
		doNothing().when(tableGroupService).ungroup(anyLong());

		mockMvc.perform(delete("/api/table-groups/1"))
			.andExpect(status().isNoContent());
	}
}