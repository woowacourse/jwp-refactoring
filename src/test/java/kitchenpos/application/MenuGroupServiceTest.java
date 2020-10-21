package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
	private MenuGroupService menuGroupService;

	@Mock
	private MenuGroupDao menuGroupDao;

	private MenuGroup menuGroup;

	private List<MenuGroup> menuGroups;

	@BeforeEach
	void setUp() {
		menuGroupService = new MenuGroupService(menuGroupDao);

		menuGroup = new MenuGroup();
		menuGroup.setId(1L);
		menuGroup.setName("메뉴그룹");
	}

	@Test
	void create() {
		when(menuGroupDao.save(any())).thenReturn(menuGroup);

		assertThat(menuGroupService.create(menuGroup)).isEqualTo(menuGroup);
	}

	@Test
	void list() {
		when(menuGroupDao.findAll()).thenReturn(menuGroups);

		assertThat(menuGroupService.list()).isEqualTo(menuGroups);
	}
}