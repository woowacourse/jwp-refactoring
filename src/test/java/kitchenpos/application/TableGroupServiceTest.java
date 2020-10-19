package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

	private TableGroup tableGroup;

	private OrderTable orderTable1;

	private OrderTable orderTable2;

	private OrderTable orderTable3;

	@BeforeEach
	void setUp() {
		this.tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

		orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);

		orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);

		orderTable3 = new OrderTable();
		orderTable3.setId(3L);
		orderTable3.setEmpty(true);

		tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2, orderTable3));
	}

	@DisplayName("TableGroup을 생성한다.")
	@Test
	void createTest() {
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

	@DisplayName("TableGroup을 삭제한다.")
	@Test
	void ungroupTest() {
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
}
