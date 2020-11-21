package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.tableGroup.OrderTableCreateRequest;
import kitchenpos.dto.tableGroup.OrderTableCreateRequests;
import kitchenpos.dto.tableGroup.TableGroupCreateRequest;
import kitchenpos.dto.tableGroup.TableGroupCreateResponse;

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
		TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(null,
			LocalDateTime.of(2020, 10, 28, 17, 1), new OrderTableCreateRequests(Collections.emptyList()));

		assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("orderTable이 2개 미만일 경우 IllegalArgumentException 발생")
	@Test
	void create_whenOrderTableCountIsLowerThenTwo_thenThrowIllegalArgumentException() {
		OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(null, null, 0, true);

		TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(null,
			LocalDateTime.of(2020, 10, 28, 17, 1),
			new OrderTableCreateRequests(Collections.singletonList(orderTableCreateRequest)));

		assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("존재하지 않는 orderTable을 가질 경우 IllegalArgumentException 발생")
	@Test
	void create_whenOrderTableIsNotExist_thenThrowIllegalArgumentException() {
		OrderTableCreateRequest orderTableCreateRequest1 = new OrderTableCreateRequest(null, null, 0, true);
		OrderTableCreateRequest orderTableCreateRequest2 = new OrderTableCreateRequest(null, null, 0, true);

		OrderTableCreateRequests orderTableCreateRequests = new OrderTableCreateRequests(new ArrayList<>(
			Arrays.asList(orderTableCreateRequest1, orderTableCreateRequest2)));

		TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(null,
			LocalDateTime.of(2020, 10, 28, 17, 1), orderTableCreateRequests);

		assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("비어있지 않은 orderTable을 가질 경우 IllegalArgumentException 발생")
	@Test
	void create_whenOrderTableIsNotEmpty_thenThrowIllegalArgumentException() {
		OrderTable orderTable1 = createOrderTable(null, true, null, 0);
		OrderTable orderTable2 = createOrderTable(null, false, null, 3);

		OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
		OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

		OrderTableCreateRequest orderTableCreateRequest1 = new OrderTableCreateRequest(savedOrderTable1);
		OrderTableCreateRequest orderTableCreateRequest2 = new OrderTableCreateRequest(savedOrderTable2);

		OrderTableCreateRequests orderTableCreateRequests = new OrderTableCreateRequests(new ArrayList<>(
			Arrays.asList(orderTableCreateRequest1, orderTableCreateRequest2)));

		TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(null,
			LocalDateTime.of(2020, 10, 28, 17, 1), orderTableCreateRequests);

		assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Table Group 저장 성공")
	@Test
	void create() {
		OrderTable orderTable1 = createOrderTable(null, true, null, 0);
		OrderTable orderTable2 = createOrderTable(null, true, null, 0);

		OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
		OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

		OrderTableCreateRequest orderTableCreateRequest1 = new OrderTableCreateRequest(savedOrderTable1);
		OrderTableCreateRequest orderTableCreateRequest2 = new OrderTableCreateRequest(savedOrderTable2);

		OrderTableCreateRequests orderTableCreateRequests = new OrderTableCreateRequests(new ArrayList<>(
			Arrays.asList(orderTableCreateRequest1, orderTableCreateRequest2)));

		TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(null,
			LocalDateTime.of(2020, 10, 28, 17, 1), orderTableCreateRequests);

		TableGroupCreateResponse actual = tableGroupService.create(tableGroupCreateRequest);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getCreatedDate()).isNotNull(),
			() -> assertThat(actual.getOrderTables().getOrderTableCreateResponses()).hasSize(2)
		);
	}

	@DisplayName("단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 IllegalArgumentException 발생")
	@Test
	void ungroup_whenTableStatusIsCookingOrMeal_thenThrowIllegalArgumentException() {
		OrderTable orderTable1 = createOrderTable(null, true, null, 0);
		OrderTable orderTable2 = createOrderTable(null, true, null, 0);

		OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
		OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

		orderRepository.save(
			createOrder(null, OrderStatus.COOKING, savedOrderTable1.getId(), LocalDateTime.now(),
				Collections.singletonList(null)));

		orderRepository.save(createOrder(null, OrderStatus.MEAL, savedOrderTable2.getId(), LocalDateTime.now(),
			Collections.singletonList(null)));

		OrderTableCreateRequest orderTableCreateRequest1 = new OrderTableCreateRequest(savedOrderTable1.getId(), null,
			0, true);
		OrderTableCreateRequest orderTableCreateRequest2 = new OrderTableCreateRequest(savedOrderTable2.getId(), null,
			0, true);

		OrderTableCreateRequests orderTableCreateRequests = new OrderTableCreateRequests(new ArrayList<>(
			Arrays.asList(orderTableCreateRequest1, orderTableCreateRequest2)));

		TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(null,
			LocalDateTime.of(2020, 10, 28, 17, 1), orderTableCreateRequests);

		TableGroupCreateResponse savedTableGroup = tableGroupService.create(tableGroupCreateRequest);

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
			createOrder(null, OrderStatus.COMPLETION, savedOrderTable1.getId(), LocalDateTime.now(),
				Collections.singletonList(null)));

		orderRepository.save(
			createOrder(null, OrderStatus.COMPLETION, savedOrderTable2.getId(), LocalDateTime.now(),
				Collections.singletonList(null)));

		OrderTableCreateRequest orderTableCreateRequest1 = new OrderTableCreateRequest(savedOrderTable1.getId(), null,
			0, true);
		OrderTableCreateRequest orderTableCreateRequest2 = new OrderTableCreateRequest(savedOrderTable2.getId(), null,
			0, true);

		OrderTableCreateRequests orderTableCreateRequests = new OrderTableCreateRequests(new ArrayList<>(
			Arrays.asList(orderTableCreateRequest1, orderTableCreateRequest2)));

		TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(null,
			LocalDateTime.of(2020, 10, 28, 17, 1), orderTableCreateRequests);

		TableGroupCreateResponse savedTableGroup = tableGroupService.create(tableGroupCreateRequest);

		tableGroupService.ungroup(savedTableGroup.getId());
	}
}