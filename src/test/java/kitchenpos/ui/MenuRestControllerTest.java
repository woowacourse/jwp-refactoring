package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

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
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;

@WebMvcTest(controllers = MenuRestController.class)
public class MenuRestControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	@MockBean
	private MenuService menuService;
	private MockMvc mockMvc;
	private Menu menu1;

	private Menu menu2;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();
		menu1 = new Menu();
		menu1.setId(1L);
		menu1.setName("Fried");
		menu2 = new Menu();
		menu2.setId(2L);
		menu2.setName("Garlic");
	}

	@DisplayName("Menu를 생성한다.")
	@Test
	void createTest() throws Exception {
		given(menuService.create(any())).willReturn(menu1);

		mockMvc.perform(post("/api/menus")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(menu1))
			.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isCreated());
	}

	@DisplayName("등록된 모든 Menu를 보여준다.")
	@Test
	void listTest() throws Exception {
		List<Menu> menus = Arrays.asList(menu1, menu2);
		given(menuService.list()).willReturn(menus);

		mockMvc.perform(get("/api/menus")
			.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk());
	}
}
