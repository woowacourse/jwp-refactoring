package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class ServiceTest {
	public Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
		Menu menu = new Menu();
		menu.setId(id);
		menu.setName(name);
		menu.setPrice(price);
		menu.setMenuGroupId(menuGroupId);
		menu.setMenuProducts(menuProducts);

		return menu;
	}

	public MenuGroup createMenuGroup(Long id, String name) {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setId(id);
		menuGroup.setName(name);

		return menuGroup;
	}

	public MenuProduct createMenuProduct(Long menuId, Long productId, Long quantity, Long seq) {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setMenuId(menuId);
		menuProduct.setProductId(productId);
		menuProduct.setQuantity(quantity);
		menuProduct.setSeq(seq);

		return menuProduct;
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

	public Product createProduct(Long id, String name, BigDecimal price) {
		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setPrice(price);

		return product;
	}

	public TableGroup createTableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(id);
		tableGroup.setCreatedDate(createdDate);
		tableGroup.setOrderTables(orderTables);

		return tableGroup;
	}
}
