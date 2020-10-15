package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.MenuGroup;
import kitchenpos.service.common.ServiceTest;

@DisplayName("MenuGroupService 단위 테스트")
class MenuGroupServiceTest extends ServiceTest {

	@Autowired
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void create() {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setName("추천메뉴");

		MenuGroup created = menuGroupService.create(menuGroup);

		assertAll(
			() -> assertThat(created.getId()).isEqualTo(5),
			() -> assertThat(created.getName()).isEqualTo("추천메뉴")
		);
	}

	@DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
	@Test
	void list() {
		assertThat(menuGroupService.list()).hasSize(4);
	}
}