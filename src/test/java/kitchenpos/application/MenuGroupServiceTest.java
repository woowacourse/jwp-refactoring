package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
	private MenuGroupService menuGroupService;

	@Mock
	private JdbcTemplateMenuGroupDao menuGroupDao;

	private MenuGroup menuGroup1;

	private MenuGroup menuGroup2;

	@BeforeEach()
	void setUp() {
		menuGroupService = new MenuGroupService(menuGroupDao);

		menuGroup1 = new MenuGroup();
		menuGroup1.setId(1L);
		menuGroup1.setName("Fried Set");

		menuGroup2 = new MenuGroup();
		menuGroup2.setId(2L);
		menuGroup2.setName("New Set");
	}

	@DisplayName("MenuGroup을 생성한다.")
	@Test
	void creatTest() {
		when(menuGroupDao.save(any())).thenReturn(menuGroup1);

		MenuGroup created = menuGroupService.create(menuGroup1);

		assertThat(created.getId()).isEqualTo(menuGroup1.getId());
		assertThat(created.getName()).isEqualTo(menuGroup1.getName());
	}

	@DisplayName("등록된 모든 MenuGroup을 조회한다.")
	@Test
	void listTest() {
		List<MenuGroup> menuGroups = Arrays.asList(menuGroup1, menuGroup2);
		when(menuGroupDao.findAll()).thenReturn(menuGroups);

		List<MenuGroup> menuGroupList = menuGroupService.list();

		assertThat(menuGroupList.get(0).getId()).isEqualTo(menuGroup1.getId());
		assertThat(menuGroupList.get(0).getName()).isEqualTo(menuGroup1.getName());
		assertThat(menuGroupList.get(1).getId()).isEqualTo(menuGroup2.getId());
		assertThat(menuGroupList.get(1).getName()).isEqualTo(menuGroup2.getName());
	}
}
