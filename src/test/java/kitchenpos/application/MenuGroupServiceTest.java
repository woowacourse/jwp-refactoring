package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
class MenuGroupServiceTest {
	@Autowired
	private MenuGroupService menuGroupService;

	@Test
	void create() {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setId(1L);
		menuGroup.setName("메뉴그룹");

		MenuGroup actual = menuGroupService.create(menuGroup);

		assertThat(actual.getId()).isNotNull();
		assertThat(actual.getName()).isEqualTo(menuGroup.getName());
	}

	@Test
	void list() {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setId(1L);
		menuGroup.setName("메뉴그룹");

		menuGroupService.create(menuGroup);

		List<MenuGroup> actual = menuGroupService.list();
		MenuGroup actualItem = actual.get(0);

		assertAll(
			() -> assertThat(actual).hasSize(1),
			() -> assertThat(actualItem.getId()).isNotNull(),
			() -> assertThat(actualItem.getName()).isEqualTo(menuGroup.getName())
		);
	}
}