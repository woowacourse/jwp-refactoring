package kitchenpos.testutils;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TestDomainBuilder {

    public static ProductBuilder productBuilder() {
        return new ProductBuilder();
    }

    public static MenuGroupBuilder menuGroupBuilder() {
        return new MenuGroupBuilder();
    }

    public static MenuBuilder menuBuilder() {
        return new MenuBuilder();
    }

    public static MenuProductBuilder menuProductBuilder() {
        return new MenuProductBuilder();
    }

    public static OrderBuilder orderBuilder() {
        return new OrderBuilder();
    }

    public static OrderLineItemBuilder orderLineItemBuilder() {
        return new OrderLineItemBuilder();
    }

    public static OrderTableBuilder orderTableBuilder() {
        return new OrderTableBuilder();
    }

    public static TableGroupBuilder tableGroupBuilder() {
        return new TableGroupBuilder();
    }

    public static class ProductBuilder {
        private Long id;
        private String name;
        private BigDecimal price;

        public ProductBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            return product;
        }
    }

    public static class MenuGroupBuilder {
        private Long id;
        private String name;

        public MenuGroupBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MenuGroupBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuGroup build() {
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.setId(id);
            menuGroup.setName(name);
            return menuGroup;
        }
    }

    public static class MenuBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        public MenuBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MenuBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public MenuBuilder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public MenuBuilder menuProducts(List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            Menu menu = new Menu();
            menu.setId(id);
            menu.setName(name);
            menu.setPrice(price);
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(menuProducts);
            return menu;
        }
    }

    public static class MenuProductBuilder {
        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        public MenuProductBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public MenuProductBuilder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public MenuProductBuilder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public MenuProductBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setSeq(seq);
            menuProduct.setMenuId(menuId);
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(quantity);
            return menuProduct;
        }
    }

    public static class OrderBuilder {
        private Long id;
        private Long orderTableId;
        private String orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems;

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public OrderBuilder orderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderBuilder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public OrderBuilder orderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.setId(id);
            order.setOrderTableId(orderTableId);
            order.setOrderStatus(orderStatus);
            order.setOrderedTime(orderedTime);
            order.setOrderLineItems(orderLineItems);
            return order;
        }
    }

    public static class OrderLineItemBuilder {
        private Long seq;
        private Long orderId;
        private Long menuId;
        private long quantity;

        public OrderLineItemBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public OrderLineItemBuilder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderLineItemBuilder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public OrderLineItemBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setSeq(seq);
            orderLineItem.setOrderId(orderId);
            orderLineItem.setMenuId(menuId);
            orderLineItem.setQuantity(quantity);
            return orderLineItem;
        }
    }

    public static class OrderTableBuilder {
        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        public OrderTableBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderTableBuilder tableGroupId(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public OrderTableBuilder numberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public OrderTableBuilder empty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            OrderTable orderTable = new OrderTable();
            orderTable.setId(id);
            orderTable.setTableGroupId(tableGroupId);
            orderTable.setNumberOfGuests(numberOfGuests);
            orderTable.setEmpty(empty);
            return orderTable;
        }
    }

    public static class TableGroupBuilder {
        private Long id;
        private LocalDateTime createdDate;
        private List<OrderTable> orderTables;

        public TableGroupBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TableGroupBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public TableGroupBuilder orderTables(List<OrderTable> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            TableGroup tableGroup = new TableGroup();
            tableGroup.setId(id);
            tableGroup.setCreatedDate(createdDate);
            tableGroup.setOrderTables(orderTables);
            return tableGroup;
        }
    }
}
