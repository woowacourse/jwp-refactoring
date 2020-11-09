package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableGroupServiceTest implements ServiceTest {

	@Autowired
	private TableGroupService tableGroupService;

	@Autowired
	private TableService tableService;

	@Autowired
	private OrderService orderService;

	@DisplayName("빈 테이블들을 단체로 지정한다")
	@Test
	void create() {
		OrderTable emptyTable1 = tableService.create(new OrderTable());
		OrderTable emptyTable2 = tableService.create(new OrderTable());
		emptyTable1.setEmpty(true);
		emptyTable2.setEmpty(true);
		tableService.changeEmpty(emptyTable1.getId(), emptyTable1);
		tableService.changeEmpty(emptyTable2.getId(), emptyTable2);

		TableGroup input = new TableGroup();

		input.setOrderTables(Arrays.asList(emptyTable1, emptyTable2));
		TableGroup output = tableGroupService.create(input);

		assertAll(
			() -> assertThat(output.getId()).isNotNull(),
			() -> assertThat(output.getCreatedDate()).isNotNull(),
			() -> assertThat(output.getOrderTables().get(0).isEmpty()).isFalse(),
			() -> assertThat(output.getOrderTables().get(1).isEmpty()).isFalse()
		);
	}

	@DisplayName("단체 지정을 중복할 경우 예외가 발생한다")
	@Test
	void create_WhenDuplicatedTableGroup_ThrowException() {
		OrderTable emptyTable1 = tableService.create(new OrderTable());
		OrderTable emptyTable2 = tableService.create(new OrderTable());
		emptyTable1.setEmpty(true);
		emptyTable2.setEmpty(true);
		tableService.changeEmpty(emptyTable1.getId(), emptyTable1);
		tableService.changeEmpty(emptyTable2.getId(), emptyTable2);

		TableGroup input1 = new TableGroup();

		input1.setOrderTables(Arrays.asList(emptyTable1, emptyTable2));
		tableGroupService.create(input1);

		assertThatThrownBy(() -> tableGroupService.create(input1))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정을 해제한다")
	@Test
	void ungroup() {
		OrderTable emptyTable1 = tableService.create(new OrderTable());
		OrderTable emptyTable2 = tableService.create(new OrderTable());
		emptyTable1.setEmpty(true);
		emptyTable2.setEmpty(true);
		tableService.changeEmpty(emptyTable1.getId(), emptyTable1);
		tableService.changeEmpty(emptyTable2.getId(), emptyTable2);

		TableGroup input = new TableGroup();

		input.setOrderTables(Arrays.asList(emptyTable1, emptyTable2));
		TableGroup output = tableGroupService.create(input);

		tableGroupService.ungroup(output.getId());

		assertThat(tableGroupService.findById(output.getId()).get().getOrderTables()).isNull();
	}

	@DisplayName("단체 지정을 해제시 주문 상태가 조리 혹은 식사인 경우 예외가 발생한다")
	@ValueSource(strings = {"COOKING", "MEAL"})
	@ParameterizedTest
	void ungroup_WhenOrderStatusIsCookingOrMeal_ThrowException(String invalidOderStatus) {
		OrderTable emptyTable1 = tableService.create(new OrderTable());
		OrderTable emptyTable2 = tableService.create(new OrderTable());
		emptyTable1.setEmpty(true);
		emptyTable2.setEmpty(true);
		tableService.changeEmpty(emptyTable1.getId(), emptyTable1);
		tableService.changeEmpty(emptyTable2.getId(), emptyTable2);

		TableGroup input = new TableGroup();

		input.setOrderTables(Arrays.asList(emptyTable1, emptyTable2));
		TableGroup output = tableGroupService.create(input);

		// 조리 혹은 식사인 주문과 테이블을 연결한다.
		Order order = new Order();
		order.setOrderTableId(emptyTable1.getId());
		order.setOrderStatus(invalidOderStatus);

		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);

		order.setOrderLineItems(Arrays.asList(orderLineItem));
		orderService.create(order);

		assertThatThrownBy(() -> tableGroupService.ungroup(output.getId()))
			.isInstanceOf(IllegalArgumentException.class);
	}
}