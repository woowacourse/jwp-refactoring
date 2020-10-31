package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
	@Mock
	private MenuDao menuDao;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderLineItemDao orderLineItemDao;

	@Mock
	private OrderTableDao orderTableDao;

	private OrderService orderService;

	@BeforeEach
	void setUp() {
		orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
	}

	@DisplayName("주문의 orderLineItems가 빈 배열일 경우 IllegalArgumentException 발생")
	@Test
	void create1() {
		Order order = new Order();
		order.setId(1L);
		order.setOrderTableId(1L);
		order.setOrderStatus(OrderStatus.COOKING.name());
		order.setOrderedTime(LocalDateTime.now());

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("존재하지 않는 menu를 가지고 있을 경우 IllegalArgumentException 발생")
	@Test
	void create2() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setOrderId(1L);
		orderLineItem.setQuantity(1L);
		orderLineItem.setSeq(1L);

		Order order = new Order();
		order.setId(1L);
		order.setOrderTableId(1L);
		order.setOrderStatus(OrderStatus.COOKING.name());
		order.setOrderedTime(LocalDateTime.now());
		order.setOrderLineItems(Collections.singletonList(orderLineItem));

		when(menuDao.countByIdIn(anyList())).thenReturn(0L);

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("존재하지 않는 테이블을 orderTable로 갖고 있을 경우 IllegalArgumentException 발생")
	@Test
	void create3() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setOrderId(1L);
		orderLineItem.setQuantity(1L);
		orderLineItem.setSeq(1L);

		Order order = new Order();
		order.setId(1L);
		order.setOrderTableId(1L);
		order.setOrderStatus(OrderStatus.COOKING.name());
		order.setOrderedTime(LocalDateTime.now());
		order.setOrderLineItems(Collections.singletonList(orderLineItem));

		when(menuDao.countByIdIn(anyList())).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenThrow(IllegalArgumentException.class);

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("orderTable이 비어있을 경우 IllegalArgumentException 발생")
	@Test
	void create4() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setOrderId(1L);
		orderLineItem.setQuantity(1L);
		orderLineItem.setSeq(1L);

		Order order = new Order();
		order.setId(1L);
		order.setOrderTableId(1L);
		order.setOrderStatus(OrderStatus.COOKING.name());
		order.setOrderedTime(LocalDateTime.now());
		order.setOrderLineItems(Collections.singletonList(orderLineItem));

		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setNumberOfGuests(2);
		orderTable.setTableGroupId(1L);
		orderTable.setEmpty(true);

		when(menuDao.countByIdIn(anyList())).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Order 저장 성공")
	@Test
	void create5() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setOrderId(1L);
		orderLineItem.setQuantity(1L);
		orderLineItem.setSeq(1L);

		Order order = new Order();
		order.setId(1L);
		order.setOrderTableId(1L);
		order.setOrderStatus(OrderStatus.COOKING.name());
		order.setOrderedTime(LocalDateTime.now());
		order.setOrderLineItems(Collections.singletonList(orderLineItem));

		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setNumberOfGuests(2);
		orderTable.setTableGroupId(1L);
		orderTable.setEmpty(false);

		when(menuDao.countByIdIn(anyList())).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderDao.save(any())).thenReturn(order);
		when(orderLineItemDao.save(any())).thenReturn(orderLineItem);

		Order actual = orderService.create(order);

		assertThat(actual).usingRecursiveComparison()
			.isEqualTo(order);
	}

	@Test
	void list() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setOrderId(1L);
		orderLineItem.setQuantity(1L);
		orderLineItem.setSeq(1L);

		Order order = new Order();
		order.setId(1L);
		order.setOrderTableId(1L);
		order.setOrderStatus(OrderStatus.COOKING.name());
		order.setOrderedTime(LocalDateTime.of(2020, 10, 26, 21, 50));

		when(orderDao.findAll()).thenReturn(Collections.singletonList(order));
		when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(Collections.singletonList(orderLineItem));

		Order expected = new Order();
		expected.setId(1L);
		expected.setOrderTableId(1L);
		expected.setOrderStatus(OrderStatus.COOKING.name());
		expected.setOrderedTime(LocalDateTime.of(2020, 10, 26, 21, 50));
		expected.setOrderLineItems(Collections.singletonList(orderLineItem));

		List<Order> actual = orderService.list();
		assertThat(actual).hasSize(1);
		assertThat(actual.get(0)).usingRecursiveComparison()
			.isEqualTo(expected);
	}

	@DisplayName("존재하지 않는 order를 수정할 경우 IllegalArgumentException 발생")
	@Test
	void changeOrderStatus1() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setOrderId(1L);
		orderLineItem.setQuantity(1L);
		orderLineItem.setSeq(1L);

		Order order = new Order();
		order.setId(1L);
		order.setOrderTableId(1L);
		order.setOrderStatus(OrderStatus.COOKING.name());
		order.setOrderedTime(LocalDateTime.of(2020, 10, 26, 21, 50));
		order.setOrderLineItems(Collections.singletonList(orderLineItem));

		when(orderDao.findById(anyLong())).thenThrow(IllegalArgumentException.class);

		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("orderStatus가 계산 완료(OrderStatus.COMPLETION인 경우 IllegalArgumentException 발생")
	@Test
	void changeOrderStatus2() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setOrderId(1L);
		orderLineItem.setQuantity(1L);
		orderLineItem.setSeq(1L);

		Order order = new Order();
		order.setId(1L);
		order.setOrderTableId(1L);
		order.setOrderStatus(OrderStatus.COMPLETION.name());
		order.setOrderedTime(LocalDateTime.of(2020, 10, 26, 21, 50));
		order.setOrderLineItems(Collections.singletonList(orderLineItem));

		when(orderDao.findById(anyLong())).thenReturn(Optional.of(order));

		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("orderStatus 변경 성공")
	@Test
	void changeOrderStatus3() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setOrderId(1L);
		orderLineItem.setQuantity(1L);
		orderLineItem.setSeq(1L);

		Order order = new Order();
		order.setId(1L);
		order.setOrderTableId(1L);
		order.setOrderStatus(OrderStatus.COOKING.name());
		order.setOrderedTime(LocalDateTime.of(2020, 10, 26, 21, 50));

		when(orderDao.findById(anyLong())).thenReturn(Optional.of(order));
		when(orderDao.save(any())).thenReturn(order);
		when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(Collections.singletonList(orderLineItem));

		Order actual = orderService.changeOrderStatus(1L, order);

		assertThat(actual).usingRecursiveComparison()
			.isEqualTo(order);
	}
}