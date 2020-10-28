package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private TableGroupDao tableGroupDao;

	private TableGroupService tableGroupService;

	@BeforeEach
	void setUp() {
		tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
	}

	@DisplayName("orderTable이 비어있을 경우 IllegalArgumentException 발생")
	@Test
	void create1() {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setOrderTables(Collections.emptyList());
		tableGroup.setCreatedDate(LocalDateTime.of(2020, 10, 28, 17, 1));

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("orderTable이 2개 미만일 경우 IllegalArgumentException 발생")
	@Test
	void create2() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(true);
		orderTable.setTableGroupId(1L);
		orderTable.setNumberOfGuests(2);

		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setOrderTables(Collections.singletonList(orderTable));
		tableGroup.setCreatedDate(LocalDateTime.of(2020, 10, 28, 17, 1));

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("존재하지 않는 orderTable을 가질 경우 IllegalArgumentException 발생")
	@Test
	void create3() {
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		orderTable1.setTableGroupId(null);
		orderTable1.setNumberOfGuests(2);

		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		orderTable2.setTableGroupId(null);
		orderTable2.setNumberOfGuests(2);

		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable1, orderTable2));

		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setOrderTables(orderTables);
		tableGroup.setCreatedDate(LocalDateTime.of(2020, 10, 28, 17, 1));

		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Collections.singletonList(orderTable1));

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("비어있지 않은 orderTable을 가질 경우 IllegalArgumentException 발생")
	@Test
	void create4() {
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		orderTable1.setTableGroupId(null);
		orderTable1.setNumberOfGuests(2);

		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(false);
		orderTable2.setTableGroupId(null);
		orderTable2.setNumberOfGuests(2);

		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable1, orderTable2));

		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setOrderTables(orderTables);
		tableGroup.setCreatedDate(LocalDateTime.of(2020, 10, 28, 17, 1));

		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(orderTables);

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정이 되어있는 orderTable을 가질 경우 IllegalArgumentException 발생")
	@Test
	void create5() {
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		orderTable1.setTableGroupId(null);
		orderTable1.setNumberOfGuests(2);

		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		orderTable2.setTableGroupId(1L);
		orderTable2.setNumberOfGuests(2);

		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable1, orderTable2));

		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setOrderTables(orderTables);
		tableGroup.setCreatedDate(LocalDateTime.of(2020, 10, 28, 17, 1));

		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(orderTables);

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Table Group 저장 성공")
	@Test
	void create6() {
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		orderTable1.setTableGroupId(null);
		orderTable1.setNumberOfGuests(2);

		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		orderTable2.setTableGroupId(null);
		orderTable2.setNumberOfGuests(2);

		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable1, orderTable2));

		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setOrderTables(orderTables);
		tableGroup.setCreatedDate(LocalDateTime.of(2020, 10, 28, 17, 1));

		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(orderTables);

		when(tableGroupDao.save(any())).thenReturn(tableGroup);

		TableGroup actual = tableGroupService.create(tableGroup);

		assertThat(tableGroup).usingRecursiveComparison().isEqualTo(actual);
	}

	@DisplayName("단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 IllegalArgumentException 발생")
	@Test
	void ungroup1() {
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		orderTable1.setTableGroupId(null);
		orderTable1.setNumberOfGuests(2);

		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		orderTable2.setTableGroupId(null);
		orderTable2.setNumberOfGuests(2);

		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable1, orderTable2));

		when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(orderTables);

		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), any())).thenReturn(true);

		assertThatThrownBy(() -> tableGroupService.ungroup(1L))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Table Group 지정 해제 성공")
	@Test
	void ungroup2() {
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		orderTable1.setTableGroupId(null);
		orderTable1.setNumberOfGuests(2);

		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		orderTable2.setTableGroupId(null);
		orderTable2.setNumberOfGuests(2);

		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable1, orderTable2));

		when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(orderTables);

		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), any())).thenReturn(false);

		tableGroupService.ungroup(1L);

		// TableGroupService.ungroup() 실행 시, orderTableDao.save()가 2회 발새아
		verify(orderTableDao, times(2)).save(any(OrderTable.class));
	}
}