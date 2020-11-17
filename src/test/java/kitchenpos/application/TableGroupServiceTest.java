package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableGroupServiceTest extends ServiceTest {
	@Autowired
	private TableGroupService tableGroupService;

	@Autowired
	private OrderTableRepository orderTableRepository;

	@Autowired
	private OrderRepository orderRepository;

	@DisplayName("orderTable이 비어있을 경우 IllegalArgumentException 발생")
	@Test
	void create_whenOrderTableIsEmpty_thenThrowIllegalArgumentException() {
		TableGroup tableGroup = createTableGroup(1L, LocalDateTime.of(2020, 10, 28, 17, 1), Collections.emptyList());

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("orderTable이 2개 미만일 경우 IllegalArgumentException 발생")
	@Test
	void create_whenOrderTableCountIsLowerThenTwo_thenThrowIllegalArgumentException() {
		OrderTable orderTable = createOrderTable(null, true, null, 2);

		TableGroup tableGroup = createTableGroup(null, LocalDateTime.of(2020, 10, 28, 17, 1),
			Collections.singletonList(orderTable));

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("존재하지 않는 orderTable을 가질 경우 IllegalArgumentException 발생")
	@Test
	void create_whenOrderTableIsNotExist_thenThrowIllegalArgumentException() {
		OrderTable orderTable1 = createOrderTable(null, true, null, 2);
		OrderTable orderTable2 = createOrderTable(null, true, null, 3);

		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable1, orderTable2));
		TableGroup tableGroup = createTableGroup(null, LocalDateTime.of(2020, 10, 28, 17, 1), orderTables);

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("비어있지 않은 orderTable을 가질 경우 IllegalArgumentException 발생")
	@Test
	void create_whenOrderTableIsNotEmpty_thenThrowIllegalArgumentException() {
		OrderTable orderTable1 = createOrderTable(null, true, null, 2);
		OrderTable orderTable2 = createOrderTable(null, false, null, 3);

		OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
		OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(savedOrderTable1, savedOrderTable2));
		TableGroup tableGroup = createTableGroup(null, LocalDateTime.of(2020, 10, 28, 17, 1), orderTables);

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Table Group 저장 성공")
	@Test
	void create() {
		OrderTable orderTable1 = createOrderTable(null, true, null, 2);
		OrderTable orderTable2 = createOrderTable(null, true, null, 3);

		OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
		OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(savedOrderTable1, savedOrderTable2));
		TableGroup tableGroup = createTableGroup(null, LocalDateTime.of(2020, 10, 28, 17, 1), orderTables);

		TableGroup actual = tableGroupService.create(tableGroup);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getCreatedDate()).isNotNull(),
			() -> assertThat(actual.getOrderTables()).hasSize(2)
		);
	}

	@DisplayName("단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 IllegalArgumentException 발생")
	@Test
	void ungroup_whenTableStatusIsCookingOrMeal_thenThrowIllegalArgumentException() {
		OrderTable orderTable1 = createOrderTable(null, true, null, 2);
		OrderTable orderTable2 = createOrderTable(null, true, null, 3);

		OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
		OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

		orderRepository.save(
			createOrder(null, OrderStatus.COOKING.name(), savedOrderTable1.getId(), LocalDateTime.now(),
				Collections.singletonList(null)));

		orderRepository.save(createOrder(null, OrderStatus.MEAL.name(), savedOrderTable2.getId(), LocalDateTime.now(),
			Collections.singletonList(null)));

		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(savedOrderTable1, savedOrderTable2));
		TableGroup tableGroup = createTableGroup(null, LocalDateTime.of(2020, 10, 28, 17, 1), orderTables);

		TableGroup savedTableGroup = tableGroupService.create(tableGroup);

		assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Table Group 지정 해제 성공")
	@Test
	void ungroup() {
		OrderTable orderTable1 = createOrderTable(null, true, null, 2);
		OrderTable orderTable2 = createOrderTable(null, true, null, 3);

		OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
		OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

		orderRepository.save(
			createOrder(null, OrderStatus.COMPLETION.name(), savedOrderTable1.getId(), LocalDateTime.now(),
				Collections.singletonList(null)));

		orderRepository.save(
			createOrder(null, OrderStatus.COMPLETION.name(), savedOrderTable2.getId(), LocalDateTime.now(),
				Collections.singletonList(null)));

		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(savedOrderTable1, savedOrderTable2));
		TableGroup tableGroup = createTableGroup(null, LocalDateTime.of(2020, 10, 28, 17, 1), orderTables);

		TableGroup savedTableGroup = tableGroupService.create(tableGroup);

		tableGroupService.ungroup(savedTableGroup.getId());
	}
}