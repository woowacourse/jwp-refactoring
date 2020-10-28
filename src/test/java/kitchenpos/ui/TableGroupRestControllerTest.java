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
import kitchenpos.domain.TableGroup;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@MockBean
	private TableGroupService tableGroupService;

	private TableGroup tableGroup;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());

		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.build();

		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(true);
		orderTable.setTableGroupId(1L);
		orderTable.setNumberOfGuests(2);

		tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setCreatedDate(LocalDateTime.of(2020, 10, 28, 16, 40));
		tableGroup.setOrderTables(Collections.singletonList(orderTable));
	}

	@Test
	void create() throws Exception {
		given(tableGroupService.create(any(TableGroup.class))).willReturn(tableGroup);

		mockMvc.perform(post("/api/table-groups")
			.content(objectMapper.writeValueAsString(tableGroup))
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