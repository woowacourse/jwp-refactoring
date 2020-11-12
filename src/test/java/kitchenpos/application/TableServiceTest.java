package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableServiceTest extends ServiceTest {

	@Autowired
	private TableService tableService;

	@Autowired
	private OrderTableDao orderTableDao;

	@Autowired
	private TableGroupService tableGroupService;

	@Test
	void create() {
		OrderTable orderTable = createOrderTable(null, false, null, 1);

		OrderTable actual = tableService.create(orderTable);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.isEmpty()).isEqualTo(false),
			() -> assertThat(actual.getTableGroupId()).isNull(),
			() -> assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests())
		);
	}

	@Test
	void list() {
		OrderTable orderTable = createOrderTable(null, false, null, 1);

		OrderTable expect = tableService.create(orderTable);

		List<OrderTable> actual = tableService.list();

		assertThat(actual).hasSize(1);
		assertThat(actual.get(0)).usingRecursiveComparison()
			.isEqualTo(expect);
	}

	@DisplayName("존재하지 않는 OrderTable의 empty 상태를 수정할 때 IllegalArgumentException이 발생한다.")
	@Test
	void changeEmpty_whenOrderTableIsNotExist_thenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정된 주문 테이블 수정할 때 IllegalArgumentException이 발생한다.")
	@Test
	void changeEmpty_whenOrderTableIsSetTableGroup_thenThrowIllegalArgumentException() {
		OrderTable orderTable1 = createOrderTable(null, true, null, 2);
		OrderTable orderTable2 = createOrderTable(null, true, null, 3);

		OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
		OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(savedOrderTable1, savedOrderTable2));
		TableGroup tableGroup = createTableGroup(null, LocalDateTime.of(2020, 10, 28, 17, 1), orderTables);

		tableGroupService.create(tableGroup);

		assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), new OrderTable()))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("empty 상태 변경 성공")
	@Test
	void changeEmpty() {
		OrderTable orderTable = createOrderTable(null, false, null, 1);

		OrderTable savedOrderTable = tableService.create(orderTable);
		OrderTable changingOrderTable = createOrderTable(savedOrderTable.getId(), true, null, 1);

		OrderTable actual = tableService.changeEmpty(savedOrderTable.getId(), changingOrderTable);

		assertThat(actual.isEmpty()).isEqualTo(changingOrderTable.isEmpty());
	}

	@DisplayName("손님의 수가 음수일 때 IllegalArgumentException 발생")
	@Test
	void changeNumberOfGuests_whenNumberOfGuestIsMinus_thenThrowIllegalArgumentException() {
		OrderTable orderTable = createOrderTable(null, false, null, 1);

		OrderTable savedOrderTable = tableService.create(orderTable);
		OrderTable changingOrderTable = createOrderTable(savedOrderTable.getId(), false, null, -1);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), changingOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("존재하지 않는 OrderTable의 numberOfGusets를 수정할 때 IllegalArgumentException 발생")
	@Test
	void changeNumberOfGuests_whenOrderTableIsNotExist_thenThrowIllegalArgumentException() {
		OrderTable orderTable = createOrderTable(null, false, null, 1);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("비어있는 OrderTable을 수정할 때 IllegalArgumentException 발생")
	@Test
	void changeNumberOfGuests_whenOrderTableIsEmpty_thenThrowIllegalArgumentException() {
		OrderTable orderTable = createOrderTable(null, true, null, 0);

		OrderTable savedOrderTable = tableService.create(orderTable);
		OrderTable changingOrderTable = createOrderTable(savedOrderTable.getId(), true, null, 2);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), changingOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("OrderTable numberOfGuest 상태 변경 성공")
	@Test
	void changeNumberOfGuests() {
		OrderTable orderTable = createOrderTable(null, false, null, 2);

		OrderTable savedOrderTable = tableService.create(orderTable);
		OrderTable changingOrderTable = createOrderTable(savedOrderTable.getId(), false, null, 4);

		OrderTable actual = tableService.changeNumberOfGuests(savedOrderTable.getId(), changingOrderTable);

		assertThat(actual.getNumberOfGuests()).isEqualTo(changingOrderTable.getNumberOfGuests());
	}
}