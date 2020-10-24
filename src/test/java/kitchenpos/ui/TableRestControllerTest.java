package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;

@WebMvcTest(controllers = TableRestController.class)
public class TableRestControllerTest {
	private static final String BASE_URL = "/api/tables";
	private final ObjectMapper objectMapper = new ObjectMapper();
	@MockBean
	private TableService tableService;
	private MockMvc mockMvc;
	private OrderTable orderTable;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();

		orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(true);
		orderTable.setNumberOfGuests(2);
	}

	@DisplayName("OrderTable을 생성한다.")
	@Test
	void createTest() throws Exception {
		given(tableService.create(any())).willReturn(orderTable);

		mockMvc.perform(post(BASE_URL)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderTable))
		)
			.andExpect(status().isCreated());
	}

	@DisplayName("등록된 모든 OrderTable을 조회한다.")
	@Test
	void listTest() throws Exception {
		given(tableService.list()).willReturn(Collections.singletonList(orderTable));

		mockMvc.perform(get(BASE_URL)
			.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk());
	}

	@DisplayName("OrderTable의 empty 여부를 변경한다.")
	@Test
	void changeEmptyTest() throws Exception {
		OrderTable changed = new OrderTable();
		changed.setEmpty(false);
		given(tableService.changeEmpty(anyLong(), any())).willReturn(changed);

		mockMvc.perform(put(BASE_URL + "/{id}/empty", 1L)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(changed))
		)
			.andExpect(status().isOk());
	}

	@DisplayName("OrderTable의 손님 수를 변경한다.")
	@Test
	void changeNumberOfGuestsTest() throws Exception {
		OrderTable changed = new OrderTable();
		changed.setNumberOfGuests(4);
		given(tableService.changeNumberOfGuests(anyLong(), any())).willReturn(changed);

		mockMvc.perform(put(BASE_URL + "/{id}/number-of-guests", 1L)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(changed))
		)
			.andExpect(status().isOk());
	}
}
