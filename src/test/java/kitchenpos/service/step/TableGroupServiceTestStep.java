package kitchenpos.service.step;

import java.util.Arrays;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupServiceTestStep {
	public static TableGroup createValidTableGroup() {
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);

		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(3L);

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

		return tableGroup;
	}

	public static TableGroup createTableGroupThatHasDuplicatedTables() {
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);

		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(1L);

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

		return tableGroup;
	}
}
