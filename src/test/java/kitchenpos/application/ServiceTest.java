package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Money;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class ServiceTest {
	public Menu createMenu(Long id, String name, Money price, Long menuGroupId, MenuProducts menuProducts) {
		return new Menu(id, name, price, menuGroupId, menuProducts);
	}

	public MenuGroup createMenuGroup(Long id, String name) {
		return new MenuGroup(id, name);
	}

	public MenuProduct createMenuProduct(Long menuId, Long productId, Long quantity, Long seq) {
		return new MenuProduct(seq, menuId, productId, quantity);
	}

	public Order createOrder(Long id, String status, Long orderTableId, LocalDateTime orderedTime,
		List<OrderLineItem> orderLineItems) {
		Order order = new Order();
		order.setId(id);
		order.setOrderStatus(status);
		order.setOrderTableId(orderTableId);
		order.setOrderedTime(orderedTime);
		order.setOrderLineItems(orderLineItems);

		return order;
	}

	public OrderLineItem createOrderLineItem(Long orderId, Long menuId, Long seq, Long quantity) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setOrderId(orderId);
		orderLineItem.setMenuId(menuId);
		orderLineItem.setSeq(seq);
		orderLineItem.setQuantity(quantity);

		return orderLineItem;
	}

	public OrderTable createOrderTable(Long id, boolean empty, Long tableGroupId, int numberOfGuests) {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(id);
		orderTable.setEmpty(empty);
		orderTable.setTableGroupId(tableGroupId);
		orderTable.setNumberOfGuests(numberOfGuests);

		return orderTable;
	}

	public Product createProduct(Long id, String name, Money price) {
		return new Product(id, name, price);
	}

	public TableGroup createTableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(id);
		tableGroup.setCreatedDate(createdDate);
		tableGroup.setOrderTables(orderTables);

		return tableGroup;
	}
}
