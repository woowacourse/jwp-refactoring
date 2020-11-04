package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.MenuGroup;

class MenuGroupServiceTest implements ServiceTest {

	@Autowired
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹을 생성한다")
	@Test
	void create() {
		MenuGroup input = new MenuGroup();
		input.setName("회");
		MenuGroup output = menuGroupService.create(input);

		assertAll(
			() -> assertThat(output.getId()).isNotNull(),
			() -> assertThat(output.getName()).isEqualTo("회")
		);
	}

	@DisplayName("메뉴 그룹을 전체 조회한다")
	@Test
	void list() {
		List<MenuGroup> outputs = menuGroupService.list();
		assertThat(outputs.size()).isNotZero();
	}
}