package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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

import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
	private OrderService orderService;

	@Mock
	private JdbcTemplateMenuDao menuDao;

	@Mock
	private JdbcTemplateOrderDao orderDao;

	@Mock
	private JdbcTemplateOrderLineItemDao orderLineItemDao;

	@Mock
	private JdbcTemplateOrderTableDao orderTableDao;

	private Order order;

	private Order newOrder;

	private OrderLineItem orderLineItem;

	private OrderTable orderTable;

	@BeforeEach
	void setUp() {
		this.orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		orderTable = new OrderTable();
		orderTable.setId(1L);
		orderLineItem = new OrderLineItem();
		orderLineItem.setOrderId(1L);
		orderLineItem.setMenuId(1L);
		order = new Order();
		order.setId(1L);
		order.setOrderTableId(1L);
		order.setOrderStatus(OrderStatus.COOKING.name());
		order.setOrderLineItems(Collections.singletonList(orderLineItem));
		newOrder = new Order();
		newOrder.setOrderStatus(OrderStatus.MEAL.name());
	}

	@DisplayName("Order를 생성한다.")
	@Test
	void createTest() {
		when(menuDao.countByIdIn(anyList())).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderDao.save(any())).thenReturn(order);
		when(orderLineItemDao.save(any())).thenReturn(orderLineItem);

		Order saved = orderService.create(this.order);
		assertAll(
			() -> assertThat(saved.getId()).isEqualTo(order.getId()),
			() -> assertThat(saved.getOrderTableId()).isEqualTo(order.getOrderTableId()),
			() -> assertThat(saved.getOrderLineItems().get(0).getOrderId()).isEqualTo(
				order.getOrderLineItems().get(0).getOrderId())
		);
	}

	@DisplayName("주문된 상품이 없으면 예외 발생한다.")
	@Test
	void emptyOrderLineItemsException() {
		order.setOrderLineItems(null);

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문된 상품 개수와 해당하는 메뉴 개수가 다르면 예외 발생한다.")
	@Test
	void differentOrderLineItemsException() {
		when(menuDao.countByIdIn(anyList())).thenReturn(0L);

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블이 빈 테이블이면 예외 발생한다.")
	@Test
	void emptyOrderTableException() {
		orderTable.setEmpty(true);
		when(menuDao.countByIdIn(anyList())).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("등록된 모든 Order를 조회한다.")
	@Test
	void listTest() {
		when(orderDao.findAll()).thenReturn(Collections.singletonList(order));
		when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(Collections.singletonList(orderLineItem));
		List<Order> orders = orderService.list();
		Order find = orders.get(0);

		assertAll(
			() -> assertThat(find.getId()).isEqualTo(order.getId()),
			() -> assertThat(find.getOrderTableId()).isEqualTo(order.getOrderTableId()),
			() -> assertThat(find.getOrderStatus()).isEqualTo(order.getOrderStatus()),
			() -> assertThat(find.getOrderLineItems().get(0).getOrderId()).isEqualTo(
				order.getOrderLineItems().get(0).getOrderId())
		);
	}

	@DisplayName("Order의 상태를 변경한다.")
	@Test
	void changeOrderStatusTest() {
		when(orderDao.findById(anyLong())).thenReturn(Optional.of(order));
		when(orderDao.save(any())).thenReturn(order);
		when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(Collections.singletonList(orderLineItem));
		Order changed = orderService.changeOrderStatus(1L, newOrder);

		assertAll(
			() -> assertThat(changed.getId()).isEqualTo(order.getId()),
			() -> assertThat(changed.getOrderStatus()).isEqualTo(newOrder.getOrderStatus()),
			() -> assertThat(changed.getOrderLineItems().get(0).getOrderId()).isEqualTo(
				order.getOrderLineItems().get(0).getOrderId())
		);
	}

	@DisplayName("주문 상태가 COMPLETION이면 예외 발생한다.")
	@Test
	void completionOrderStatusException() {
		order.setOrderStatus(OrderStatus.COMPLETION.name());
		when(orderDao.findById(anyLong())).thenReturn(Optional.of(order));

		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
