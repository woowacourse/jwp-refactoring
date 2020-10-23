package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
	private TableGroupService tableGroupService;

	@Mock
	private JdbcTemplateOrderDao orderDao;

	@Mock
	private JdbcTemplateOrderTableDao orderTableDao;

	@Mock
	private JdbcTemplateTableGroupDao tableGroupDao;

	private static Stream<Arguments> provideSavedOrderTables() {
		OrderTable firstOrderTable = new OrderTable();
		OrderTable secondOrderTable = new OrderTable();

		return Stream.of(
			Arguments.of(Collections.singletonList(firstOrderTable)),
			Arguments.of(Arrays.asList(firstOrderTable, secondOrderTable))
		);
	}

	@BeforeEach
	void setUp() {
		this.tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
	}

	@DisplayName("TableGroup을 생성한다.")
	@Test
	void createTest() {
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		OrderTable orderTable3 = new OrderTable();
		orderTable3.setId(3L);
		orderTable3.setEmpty(true);
		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2, orderTable3));
		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(orderTable1, orderTable2, orderTable3));
		when(tableGroupDao.save(any())).thenReturn(tableGroup);
		when(orderTableDao.save(any())).thenReturn(any());

		TableGroup saved = tableGroupService.create(tableGroup);

		assertAll(
			() -> assertThat(saved.getId()).isEqualTo(tableGroup.getId()),
			() -> assertThat(saved.getOrderTables().size()).isEqualTo(tableGroup.getOrderTables().size()),
			() -> assertThat(saved.getOrderTables().get(0).isEmpty()).isFalse()
		);
	}

	@DisplayName("테이블 그룹의 테아블 리스트가 비어있으면 예외 발생한다.")
	@Test
	void nullOrderTablesException() {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(null);

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹의 테이블 개수가 2개 미만이면 예외 발생한다.")
	@Test
	void lessThanTwoTablesException() {
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setOrderTables(Collections.singletonList(orderTable1));

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹의 테이블 수가 DB에서 조회한 테이블의 수와 다르면 예외 발생한다.")
	@ParameterizedTest
	@MethodSource("provideSavedOrderTables")
	void differentTablesException(List<OrderTable> savedOrderTables) {
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		OrderTable orderTable3 = new OrderTable();
		orderTable3.setId(3L);
		orderTable3.setEmpty(true);
		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2, orderTable3));
		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(savedOrderTables);

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("TableGroup을 삭제한다.")
	@Test
	void ungroupTest() {
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		OrderTable orderTable3 = new OrderTable();
		orderTable3.setId(3L);
		orderTable3.setEmpty(true);
		when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(
			Arrays.asList(orderTable1, orderTable2, orderTable3));
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);
		when(orderTableDao.save(any())).thenReturn(any());

		tableGroupService.ungroup(1L);

		assertAll(
			() -> assertThat(orderTable1.getTableGroupId()).isNull(),
			() -> assertThat(orderTable1.isEmpty()).isFalse(),
			() -> assertThat(orderTable2.getTableGroupId()).isNull(),
			() -> assertThat(orderTable2.isEmpty()).isFalse(),
			() -> assertThat(orderTable3.getTableGroupId()).isNull(),
			() -> assertThat(orderTable3.isEmpty()).isFalse()
		);
	}

	@DisplayName("테이블들의 주문 상태가 COOKING 혹은 MEAL이면 예외 발생한다.")
	@Test
	void completionOrderStatusException() {
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

		assertThatThrownBy(() -> tableGroupService.ungroup(1L))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
