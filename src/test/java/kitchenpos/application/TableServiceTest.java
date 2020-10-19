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

	private OrderTable orderTable;

	@BeforeEach
	void setUp() {
		this.tableService = new TableService(orderDao, orderTableDao);
		orderTable = new OrderTable();
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(2);
	}

	@DisplayName("Table을 생성한다.")
	@Test
	void createTest() {
		when(orderTableDao.save(any())).thenReturn(orderTable);

		OrderTable saved = tableService.create(orderTable);

		assertThat(saved.getId()).isNull();
		assertThat(saved.getTableGroupId()).isNull();
	}

	@DisplayName("등록된 모든 Table을 조회한다.")
	@Test
	void listTest() {
		when(orderTableDao.findAll()).thenReturn(Collections.singletonList(orderTable));

		List<OrderTable> tables = tableService.list();

		assertThat(tables.size()).isEqualTo(1);
	}

	@DisplayName("Table의 empty 여부를 변경한다.")
	@Test
	void changeEmptyTest() {
		OrderTable newOrderTable = new OrderTable();
		newOrderTable.setEmpty(true);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);
		when(orderTableDao.save(any())).thenReturn(orderTable);

		OrderTable changed = tableService.changeEmpty(1L, newOrderTable);

		assertThat(changed.isEmpty()).isEqualTo(newOrderTable.isEmpty());
	}

	@DisplayName("Table의 손님 수를 변경한다.")
	@Test
	void changeNumberOfGuestsTest() {
		OrderTable newOrderTable = new OrderTable();
		newOrderTable.setNumberOfGuests(4);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderTableDao.save(any())).thenReturn(orderTable);

		OrderTable changed = tableService.changeNumberOfGuests(1L, newOrderTable);

		assertThat(changed.getNumberOfGuests()).isEqualTo(newOrderTable.getNumberOfGuests());
	}
}
