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

	@BeforeEach()
	void setUp() {
		menuGroupService = new MenuGroupService(menuGroupDao);
	}

	@DisplayName("MenuGroup을 생성한다.")
	@Test
	void creatTest() {
		MenuGroup friedSet = new MenuGroup();
		friedSet.setId(1L);
		friedSet.setName("Fried Set");

		when(menuGroupDao.save(any())).thenReturn(friedSet);

		MenuGroup created = menuGroupService.create(friedSet);

		assertThat(created.getId()).isEqualTo(friedSet.getId());
		assertThat(created.getName()).isEqualTo(friedSet.getName());
	}

	@DisplayName("등록된 모든 MenuGroup을 조회한다.")
	@Test
	void listTest() {
		MenuGroup friedSet = new MenuGroup();
		friedSet.setId(1L);
		friedSet.setName("Fried Set");

		MenuGroup garlicSet = new MenuGroup();
		garlicSet.setId(2L);
		garlicSet.setName("Garlic Set");

		List<MenuGroup> menuGroups = Arrays.asList(friedSet, garlicSet);
		when(menuGroupDao.findAll()).thenReturn(menuGroups);

		List<MenuGroup> menuGroupList = menuGroupService.list();

		assertThat(menuGroupList.get(0).getId()).isEqualTo(friedSet.getId());
		assertThat(menuGroupList.get(0).getName()).isEqualTo(friedSet.getName());
		assertThat(menuGroupList.get(1).getId()).isEqualTo(garlicSet.getId());
		assertThat(menuGroupList.get(1).getName()).isEqualTo(garlicSet.getName());
	}
}
