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
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;

@WebMvcTest(controllers = MenuGroupRestController.class)
public class MenuGroupRestControllerTest {
	@MockBean
	private MenuGroupService menuGroupService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	private MockMvc mockMvc;

	private MenuGroup menuGroup1;

	private MenuGroup menuGroup2;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();

		menuGroup1 = new MenuGroup();
		menuGroup1.setId(1L);
		menuGroup1.setName("Fried Set");
		menuGroup2 = new MenuGroup();
		menuGroup2.setId(2L);
		menuGroup2.setName("New Set");
	}

	@DisplayName("MenuGroup을 생성한다.")
	@Test
	void createTest() throws Exception {
		given(menuGroupService.create(any())).willReturn(menuGroup1);

		mockMvc.perform(post("/api/menu-groups")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(menuGroup1))
		)
			.andExpect(status().isCreated());
	}

	@DisplayName("등록된 모든 MenuGroup을 조회한다.")
	@Test
	void listTest() throws Exception {
		List<MenuGroup> menuGroups = Arrays.asList(menuGroup1, menuGroup2);
		given(menuGroupService.list()).willReturn(menuGroups);

		mockMvc.perform(get("/api/menu-groups")
			.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk());
	}
}
