package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@MockBean
	private MenuService menuService;

	private Menu menu;

	private List<Menu> menus;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext) {
		objectMapper = new ObjectMapper();

		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.build();

		menu = new Menu(1L, "메뉴", BigDecimal.valueOf(3000), null, null);

		menus = Collections.singletonList(menu);
	}

	@Test
	void create() throws Exception {
		given(menuService.create(any())).willReturn(menu);

		mockMvc.perform(post("/api/menus")
			.content(objectMapper.writeValueAsString(menu))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/menus/1"));
	}

	@Test
	void list() throws Exception {
		given(menuService.list()).willReturn(menus);

		mockMvc.perform(get("/api/menus")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}