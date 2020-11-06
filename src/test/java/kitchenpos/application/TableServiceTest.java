package kitchenpos.application;

import static kitchenpos.application.ServiceIntegrationTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
	private TableService tableService;

	@Mock
	private JdbcTemplateOrderDao orderDao;

	@Mock
	private JdbcTemplateOrderTableDao orderTableDao;

	@BeforeEach
	void setUp() {
		this.tableService = new TableService(orderDao, orderTableDao);
	}

	@DisplayName("Table을 생성한다.")
	@Test
	void createTest() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(2);

		when(orderTableDao.save(any())).thenReturn(orderTable);

		OrderTable saved = tableService.create(orderTable);

		assertThat(saved.getId()).isNull();
		assertThat(saved.getTableGroupId()).isNull();
	}

	@DisplayName("등록된 모든 Table을 조회한다.")
	@Test
	void listTest() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(2);

		when(orderTableDao.findAll()).thenReturn(Collections.singletonList(orderTable));

		List<OrderTable> tables = tableService.list();

		assertThat(tables.size()).isEqualTo(1);
	}

	@DisplayName("Table의 empty 여부를 변경한다.")
	@Test
	void changeEmptyTest() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(2);

		OrderTable newOrderTable = new OrderTable();
		newOrderTable.setEmpty(true);
		newOrderTable.setNumberOfGuests(4);

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);
		when(orderTableDao.save(any())).thenReturn(orderTable);

		OrderTable changed = tableService.changeEmpty(1L, newOrderTable);

		assertThat(changed.isEmpty()).isEqualTo(newOrderTable.isEmpty());
	}

	@DisplayName("테이블이 등록되어 있지 않으면 빈 테이블 여부를 변경할 수 없다.")
	@Test
	void changeEmpty_willThrowException_whenTableDoesNotExist() {
		given(orderTableDao.findById(anyLong())).willThrow(IllegalArgumentException.class);

		assertThatThrownBy(() -> tableService.changeEmpty(9L, getOrderTableNotEmpty()))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹에 포함되는 테이블이면 예외 발생한다.")
	@Test
	void hasTableGroupException() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(2);
		orderTable.setTableGroupId(1L);

		OrderTable newOrderTable = new OrderTable();

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), newOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블의 주문 상태가 COOKING 혹은 MEAL이면 예외 발생한다.")
	@Test
	void notCompletionOrderStatusException() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(2);

		OrderTable newOrderTable = new OrderTable();

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

		assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), newOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Table의 손님 수를 변경한다.")
	@Test
	void changeNumberOfGuestsTest() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(2);

		OrderTable newOrderTable = new OrderTable();
		newOrderTable.setEmpty(true);
		newOrderTable.setNumberOfGuests(4);

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderTableDao.save(any())).thenReturn(orderTable);

		OrderTable changed = tableService.changeNumberOfGuests(1L, newOrderTable);

		assertThat(changed.getNumberOfGuests()).isEqualTo(newOrderTable.getNumberOfGuests());
	}

	@DisplayName("손님 수가 0보다 작으면 예외 발생한다.")
	@ParameterizedTest
	@ValueSource(ints = {-1, -2, -100})
	void negativeNumberOfGuestsException(int numberOfGuests) {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(2);

		OrderTable newOrderTable = new OrderTable();
		newOrderTable.setNumberOfGuests(numberOfGuests);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블이 비어있으면 예외 발생한다.")
	@Test
	void emptyTableException() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(true);

		OrderTable newOrderTable = new OrderTable();
		newOrderTable.setNumberOfGuests(4);

		orderTable.setEmpty(true);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}
	@DisplayName("주문 테이블이 등록되어 있지 않으면 방문한 손님 수를 변경할 수 없다.")
	@Test
	void changeNumberOfGuests_willThrowException_whenTableDoesNotExist() {
		given(orderTableDao.findById(anyLong())).willThrow(IllegalArgumentException.class);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(2L, getOrderTableWithGuests(3)))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
