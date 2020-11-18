package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.ProductRepository;

class OrderServiceTest extends ServiceTest {
	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MenuGroupRepository menuGroupRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private OrderTableRepository orderTableRepository;

	@DisplayName("주문의 orderLineItems가 빈 배열일 경우 IllegalArgumentException 발생")
	@Test
	void create_whenOrderLineItemsIsEmpty_thenThrowIllegalArgumentException() {
		Order order = createOrder(null, OrderStatus.COOKING.name(), 1L, LocalDateTime.now(), Collections.emptyList());

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("존재하지 않는 menu를 가지고 있을 경우 IllegalArgumentException 발생")
	@Test
	void create_whenMenuIsNotExist_thenThrowIllegalArgumentException() {
		OrderLineItem orderLineItem = createOrderLineItem(null, 1L, 1L, 1L);

		Order order = createOrder(null, OrderStatus.COOKING.name(), 1L, LocalDateTime.now(),
			Collections.singletonList(orderLineItem));

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("존재하지 않는 테이블을 orderTable로 갖고 있을 경우 IllegalArgumentException 발생")
	@Test
	void create_whenOrderTableIsNotExist_thenThrowIllegalArgumentException() {
		Product product = productRepository.save(createProduct(null, "제품", BigDecimal.valueOf(500L)));
		MenuProduct menuProduct = createMenuProduct(null, product.getId(), 2L, 7L);
		MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(null, "메뉴", new Money(1000L), menuGroup.getId(),
			Collections.singletonList(menuProduct));

		Menu savedMenu = menuRepository.save(menu);

		OrderLineItem orderLineItem = createOrderLineItem(null, savedMenu.getId(), 1L, 1L);

		Order order = createOrder(null, OrderStatus.COOKING.name(), 1L, LocalDateTime.now(),
			Collections.singletonList(orderLineItem));

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("orderTable이 비어있을 경우 IllegalArgumentException 발생")
	@Test
	void create_whenOrderTableIsEmpty_thenThrowIllegalArgumentException() {
		Product product = productRepository.save(createProduct(null, "제품", BigDecimal.valueOf(500L)));
		MenuProduct menuProduct = createMenuProduct(null, product.getId(), 2L, 7L);
		MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(null, "메뉴", new Money(1000L), menuGroup.getId(),
			Collections.singletonList(menuProduct));

		Menu savedMenu = menuRepository.save(menu);

		OrderLineItem orderLineItem = createOrderLineItem(null, savedMenu.getId(), 1L, 1L);

		OrderTable savedOrderTable = orderTableRepository.save(createOrderTable(null, true, null, 2));

		Order order = createOrder(1L, OrderStatus.COOKING.name(), savedOrderTable.getId(), LocalDateTime.now(),
			Collections.singletonList(orderLineItem));

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Order 저장 성공")
	@Test
	void create() {
		Product product = productRepository.save(createProduct(null, "제품", BigDecimal.valueOf(500L)));
		MenuProduct menuProduct = createMenuProduct(null, product.getId(), 2L, 7L);
		MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(null, "메뉴", new Money(1000L), menuGroup.getId(),
			Collections.singletonList(menuProduct));

		Menu savedMenu = menuRepository.save(menu);

		OrderLineItem orderLineItem = createOrderLineItem(null, savedMenu.getId(), 1L, 1L);

		OrderTable savedOrderTable = orderTableRepository.save(createOrderTable(null, false, null, 2));

		Order order = createOrder(null, OrderStatus.COOKING.name(), savedOrderTable.getId(), LocalDateTime.now(),
			Collections.singletonList(orderLineItem));

		Order actual = orderService.create(order);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getOrderedTime()).isNotNull(),
			() -> assertThat(actual.getOrderTableId()).isNotNull(),
			() -> assertThat(actual.getOrderLineItems()).hasSize(1),
			() -> assertThat(actual.getOrderStatus()).isEqualTo(order.getOrderStatus())
		);
	}

	@Test
	void list() {
		Product product = productRepository.save(createProduct(null, "제품", BigDecimal.valueOf(500L)));
		MenuProduct menuProduct = createMenuProduct(null, product.getId(), 2L, null);
		MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(1L, "메뉴", new Money(1000L), menuGroup.getId(),
			Collections.singletonList(menuProduct));

		Menu savedMenu = menuRepository.save(menu);

		OrderLineItem orderLineItem = createOrderLineItem(null, savedMenu.getId(), 1L, 1L);

		OrderTable savedOrderTable = orderTableRepository.save(createOrderTable(null, false, null, 2));

		Order savedOrder = orderService.create(
			createOrder(null, OrderStatus.COOKING.name(), savedOrderTable.getId(), LocalDateTime.now(),
				Collections.singletonList(orderLineItem)));

		List<Order> actual = orderService.list();
		Order actualItem = actual.get(0);

		assertThat(actual).hasSize(1);
		assertAll(
			() -> assertThat(actualItem.getOrderStatus()).isEqualTo(savedOrder.getOrderStatus()),
			() -> assertThat(actualItem.getOrderLineItems().get(0).getQuantity()).isEqualTo(
				savedOrder.getOrderLineItems().get(0).getQuantity())
		);
	}

	@DisplayName("존재하지 않는 order를 수정할 경우 IllegalArgumentException 발생")
	@Test
	void changeOrderStatus_whenOrderIsNotExist_thenThrowIllegalArgumentException() {
		Product product = productRepository.save(createProduct(1L, "제품", BigDecimal.valueOf(500L)));
		MenuProduct menuProduct = createMenuProduct(1L, product.getId(), 2L, null);
		MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(1L, "메뉴", new Money(1000L), menuGroup.getId(),
			Collections.singletonList(menuProduct));

		Menu savedMenu = menuRepository.save(menu);

		OrderLineItem orderLineItem = createOrderLineItem(null, savedMenu.getId(), 1L, 1L);

		OrderTable savedOrderTable = orderTableRepository.save(createOrderTable(null, false, null, 2));

		Order order = createOrder(1L, OrderStatus.COOKING.name(), savedOrderTable.getId(), LocalDateTime.now(),
			Collections.singletonList(orderLineItem));

		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("orderStatus 변경 성공")
	@Test
	void changeOrderStatus() {
		Product product = productRepository.save(createProduct(1L, "제품", BigDecimal.valueOf(500L)));
		MenuProduct menuProduct = createMenuProduct(1L, product.getId(), 2L, null);
		MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(1L, "메뉴", new Money(1000L), menuGroup.getId(),
			Collections.singletonList(menuProduct));

		Menu savedMenu = menuRepository.save(menu);

		OrderLineItem orderLineItem = createOrderLineItem(null, savedMenu.getId(), 1L, 1L);

		OrderTable savedOrderTable = orderTableRepository.save(createOrderTable(null, false, null, 2));

		Order order = createOrder(null, OrderStatus.COOKING.name(), savedOrderTable.getId(), LocalDateTime.now(),
			Collections.singletonList(orderLineItem));

		Order savedOrder = orderService.create(order);

		Order changingOrder = createOrder(savedOrder.getId(), OrderStatus.COMPLETION.name(), savedOrderTable.getId(),
			LocalDateTime.now(),
			Collections.singletonList(orderLineItem));

		Order actual = orderService.changeOrderStatus(savedOrder.getId(), changingOrder);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getOrderedTime()).isNotNull(),
			() -> assertThat(actual.getOrderTableId()).isNotNull(),
			() -> assertThat(actual.getOrderLineItems()).hasSize(1),
			() -> assertThat(actual.getOrderStatus()).isEqualTo(changingOrder.getOrderStatus())
		);
	}
}