package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.MenuGroup;

class MenuGroupServiceTest extends ServiceTest {
	@Autowired
	private MenuGroupService menuGroupService;

	@Test
	void create() {
		MenuGroup menuGroup = createMenuGroup(1L, "메뉴그룹");

		MenuGroup actual = menuGroupService.create(menuGroup);

		assertThat(actual.getId()).isNotNull();
		assertThat(actual.getName()).isEqualTo(menuGroup.getName());
	}

	@Test
	void list() {
		MenuGroup menuGroup = createMenuGroup(1L, "메뉴그룹");

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