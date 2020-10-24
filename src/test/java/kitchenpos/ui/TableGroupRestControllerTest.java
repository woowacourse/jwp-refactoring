package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;

@WebMvcTest(controllers = TableGroupRestController.class)
public class TableGroupRestControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	@MockBean
	private TableGroupService tableGroupService;
	private MockMvc mockMvc;
	private TableGroup tableGroup;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();

		tableGroup = new TableGroup();
		tableGroup.setId(1L);
	}

	@DisplayName("TableGroup을 생성한다.")
	@Test
	void createTest() throws Exception {
		given(tableGroupService.create(any())).willReturn(tableGroup);

		mockMvc.perform(post("/api/table-groups")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(tableGroup))
		)
			.andExpect(status().isCreated());
	}

	@DisplayName("TableGroup을 삭제한다.")
	@Test
	void ungroupTest() throws Exception {
		doNothing().when(tableGroupService).ungroup(anyLong());

		mockMvc.perform(delete("/api/table-groups/{id}", 1L))
			.andExpect(status().isNoContent());
	}
}
