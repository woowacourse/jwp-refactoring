package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	private TableService tableService;

	@BeforeEach
	void setUp() {
		tableService = new TableService(orderDao, orderTableDao);
	}

	@Test
	void create() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(1);
		orderTable.setTableGroupId(1L);

		when(orderTableDao.save(any(OrderTable.class))).thenReturn(orderTable);

		OrderTable actual = tableService.create(orderTable);

		OrderTable expect = new OrderTable();
		expect.setId(null);
		expect.setTableGroupId(null);
		expect.setEmpty(false);
		expect.setNumberOfGuests(1);

		assertThat(actual).usingRecursiveComparison()
			.isEqualTo(expect);
	}

	@Test
	void list() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(1);
		orderTable.setTableGroupId(1L);

		when(orderTableDao.findAll()).thenReturn(Collections.singletonList(orderTable));

		List<OrderTable> actual = tableService.list();

		assertThat(actual).hasSize(1);
		assertThat(actual.get(0)).usingRecursiveComparison()
			.isEqualTo(orderTable);
	}

	@DisplayName("존재하지 않는 OrderTable의 empty 상태를 수정할 때 IllegalArgumentException이 발생한다.")
	@Test
	void changeEmpty1() {
		when(orderTableDao.findById(anyLong())).thenThrow(IllegalArgumentException.class);

		assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정된 주문 테이블 수정할 때 IllegalArgumentException이 발생한다.")
	@Test
	void changeEmpty2() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(1);
		orderTable.setTableGroupId(1L);

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 때 IllegalArgumentException이 발생")
	@Test
	void changeEmpty3() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(1);
		orderTable.setTableGroupId(null);

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
			.thenReturn(true);

		assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("empty 상태 변경 성공")
	@Test
	void changeEmpty4() {
		OrderTable savedOrderTable = new OrderTable();
		savedOrderTable.setId(1L);
		savedOrderTable.setEmpty(true);
		savedOrderTable.setNumberOfGuests(1);
		savedOrderTable.setTableGroupId(null);

		OrderTable changedEmpty = new OrderTable();
		changedEmpty.setId(1L);
		changedEmpty.setEmpty(false);
		changedEmpty.setNumberOfGuests(1);
		changedEmpty.setTableGroupId(null);

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
			.thenReturn(false);
		when(orderTableDao.save(any(OrderTable.class))).thenReturn(changedEmpty);

		OrderTable actual = tableService.changeEmpty(1L, changedEmpty);

		assertThat(actual.isEmpty()).isEqualTo(changedEmpty.isEmpty());
	}

	@DisplayName("손님의 수가 음수일 때 IllegalArgumentException 발생")
	@Test
	void changeNumberOfGuests1() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(-1);
		orderTable.setTableGroupId(null);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("존재하지 않는 OrderTable의 numberOfGusets를 수정할 때 IllegalArgumentException 발생")
	@Test
	void changeNumberOfGuests2() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(1);
		orderTable.setTableGroupId(null);

		when(orderTableDao.findById(anyLong())).thenThrow(IllegalArgumentException.class);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("비어있는 OrderTable을 수정할 때 IllegalArgumentException 발생")
	@Test
	void changeNumberOfGuests3() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(1);
		orderTable.setTableGroupId(null);

		OrderTable savedOrderTable = new OrderTable();
		savedOrderTable.setId(1L);
		savedOrderTable.setEmpty(true);
		savedOrderTable.setNumberOfGuests(1);
		savedOrderTable.setTableGroupId(null);

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("OrderTable numberOfGuest 상태 변경 성공")
	@Test
	void changeNumberOfGuests4() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(10);
		orderTable.setTableGroupId(null);

		OrderTable savedOrderTable = new OrderTable();
		savedOrderTable.setId(1L);
		savedOrderTable.setEmpty(false);
		savedOrderTable.setNumberOfGuests(1);
		savedOrderTable.setTableGroupId(null);

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));
		when(orderTableDao.save(savedOrderTable)).thenReturn(orderTable);

		OrderTable actual = tableService.changeNumberOfGuests(1L, orderTable);

		assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
	}
}