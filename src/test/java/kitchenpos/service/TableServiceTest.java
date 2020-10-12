package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.OrderTable;
import kitchenpos.service.common.ServiceTest;

@DisplayName("TableService 단위 테스트")
class TableServiceTest extends ServiceTest {

	@Autowired
	TableService tableService;

	@DisplayName("주문 테이블을 등록할 수 있다.")
	@Test
	void create() {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(0);
		orderTable.setEmpty(true);

		OrderTable created = tableService.create(orderTable);

		assertAll(
			() -> assertThat(created.getId()).isEqualTo(9),
			() -> assertThat(created.getTableGroupId()).isNull(),
			() -> assertThat(created.getNumberOfGuests()).isEqualTo(0),
			() -> assertThat(created.isEmpty()).isTrue()
		);
	}

	@DisplayName("주문 테이블의 목록을 조회할 수 있다.")
	@Test
	void list() {
		List<OrderTable> list = tableService.list();

		assertThat(list).hasSize(8);
	}

	@Test
	void changeEmpty() {
	}

	@Test
	void changeNumberOfGuests() {
	}
}