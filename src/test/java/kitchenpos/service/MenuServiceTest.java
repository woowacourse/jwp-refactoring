package kitchenpos.service;

import static kitchenpos.service.step.MenuServiceTestStep.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Menu;

@DisplayName("MenuService 단위 테스트")
@SpringBootTest
@Sql({"/truncate.sql", "/test_data.sql"})
class MenuServiceTest {

	@Autowired
	MenuService menuService;

	@DisplayName("1 개 이상의 등록된 상품으로 메뉴를 등록할 수 있다.")
	@Test
	void create() {
		Menu menu = createValidMenu();

		Menu created = menuService.create(menu);

		assertAll(
			() -> assertThat(created.getId()).isEqualTo(7L),
			() -> assertThat(created.getName()).isEqualTo("후라이드+후라이드"),
			() -> assertThat(created.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(19000)),
			() -> assertThat(created.getMenuGroupId()).isEqualTo(1L),
			() -> assertThat(created.getMenuProducts()).hasSize(1)
		);
	}

	@DisplayName("메뉴 등록 - 메뉴의 가격은 0 원 이상이어야 한다.")
	@Test
	void create_WhenPriceLessThanZero() {
		Menu menu = createMenuThatPriceLessThanZero();

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 등록 - 메뉴의 가격은 메뉴에 속한 상품 금액의 합보다 같거나 작아야 한다.")
	@Test
	void create_WhenPriceBiggerThanSum() {
		Menu menu = createMenuThatPriceBiggerThanSum();

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 등록 - 메뉴는 특정 메뉴 그룹에 속해야 한다.")
	@Test
	void create_WhenNotBelongToMenuGroup() {
		Menu menu = createMenuThatNotBelongToMenuGroup();

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 목록을 조회할 수 있다.")
	@Test
	void list() {
		List<Menu> list = menuService.list();

		assertThat(list).hasSize(6);
	}
}