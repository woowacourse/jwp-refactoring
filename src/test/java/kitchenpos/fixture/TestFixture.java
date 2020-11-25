package kitchenpos.fixture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderMenu;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;

public abstract class TestFixture {

    public static final long MENU_GROUP_ID_1 = 1L;
    public static final long MENU_GROUP_ID_2 = 2L;
    public static final String MENU_GROUP_NAME_1 = "메뉴그룹이름1";
    public static final String MENU_GROUP_NAME_2 = "메뉴그룹이름2";
    public static final MenuGroup MENU_GROUP_1 = new MenuGroup(MENU_GROUP_ID_1, MENU_GROUP_NAME_1);
    public static final MenuGroup MENU_GROUP_2 = new MenuGroup(MENU_GROUP_ID_2, MENU_GROUP_NAME_2);

    public static final long MENU_PRODUCT_SEQ_1 = 1L;
    public static final long MENU_PRODUCT_SEQ_2 = 2L;
    public static final long MENU_ID_1 = 1L;
    public static final long MENU_ID_2 = 2L;
    public static final long MENU_PRODUCT_PRODUCT_ID_1 = 1L;
    public static final long MENU_PRODUCT_PRODUCT_ID_2 = 2L;
    public static final long MENU_PRODUCT_QUANTITY_1 = 1L;
    public static final long MENU_PRODUCT_QUANTITY_2 = 2L;
    public static final MenuProduct MENU_PRODUCT_1 = new MenuProduct(MENU_PRODUCT_SEQ_1, MENU_ID_1, MENU_PRODUCT_PRODUCT_ID_1, MENU_PRODUCT_QUANTITY_1);
    public static final MenuProduct MENU_PRODUCT_2 = new MenuProduct(MENU_PRODUCT_SEQ_2, MENU_ID_2, MENU_PRODUCT_PRODUCT_ID_2, MENU_PRODUCT_QUANTITY_2);

    public static final String MENU_NAME_1 = "메뉴이름1";
    public static final String MENU_NAME_2 = "메뉴이름2";
    public static final Price MENU_PRICE_1 = Price.of(BigDecimal.valueOf(1L, 2));
    public static final Price MENU_PRICE_2 = Price.of(BigDecimal.valueOf(2L, 2));
    public static final Menu MENU_1 = new Menu(MENU_ID_1, MENU_NAME_1, MENU_PRICE_1, MENU_GROUP_ID_1);
    public static final Menu MENU_2 = new Menu(MENU_ID_2, MENU_NAME_2, MENU_PRICE_2, MENU_GROUP_ID_2);

    public static final long PRODUCT_ID_1 = 1L;
    public static final long PRODUCT_ID_2 = 2L;
    public static final String PRODUCT_NAME_1 = "제품이름1";
    public static final String PRODUCT_NAME_2 = "제품이름2";
    public static final Price PRODUCT_PRICE_1 = Price.of(BigDecimal.valueOf(1L, 2));
    public static final Price PRODUCT_PRICE_2 = Price.of(BigDecimal.valueOf(2L, 2));
    public static final Product PRODUCT_1 = new Product(PRODUCT_ID_1, PRODUCT_NAME_1, PRODUCT_PRICE_1);
    public static final Product PRODUCT_2 = new Product(PRODUCT_ID_2, PRODUCT_NAME_2, PRODUCT_PRICE_2);

    public static final long TABLE_GROUP_ID = 1L;
    public static final LocalDateTime TABLE_GROUP_CREATED_DATE = LocalDateTime.parse("2018-11-15T10:00:00");
    public static final TableGroup TABLE_GROUP = new TableGroup(TABLE_GROUP_ID, TABLE_GROUP_CREATED_DATE);

    public static final long TABLE_ID_1 = 1L;
    public static final long TABLE_ID_2 = 2L;
    public static final int TABLE_NUMBER_OF_GUESTS_1 = 1;
    public static final int TABLE_NUMBER_OF_GUESTS_2 = 2;
    public static final boolean TABLE_EMPTY_1 = false;
    public static final boolean TABLE_EMPTY_2 = false;
    public static final Table TABLE_1 = new Table(TABLE_ID_1, TABLE_GROUP_ID, TABLE_NUMBER_OF_GUESTS_1, TABLE_EMPTY_1);
    public static final Table TABLE_2 = new Table(TABLE_ID_2, TABLE_GROUP_ID, TABLE_NUMBER_OF_GUESTS_2, TABLE_EMPTY_2);
    public static final List<Table> TABLES = Arrays.asList(TABLE_1, TABLE_2);

    public static final long ORDER_ID_1 = 1L;
    public static final long ORDER_ID_2 = 2L;
    public static final OrderStatus ORDER_STATUS_1 = OrderStatus.COOKING;
    public static final OrderStatus ORDER_STATUS_2 = OrderStatus.COOKING;
    public static final LocalDateTime ORDERED_TIME_1 = LocalDateTime.parse("2018-12-15T10:00:00");
    public static final LocalDateTime ORDERED_TIME_2 = LocalDateTime.parse("2018-12-16T10:00:00");
    public static final Order ORDER_1 = new Order(ORDER_ID_1, TABLE_ID_1, ORDER_STATUS_1, ORDERED_TIME_1);
    public static final Order ORDER_2 = new Order(ORDER_ID_2, TABLE_ID_2, ORDER_STATUS_2, ORDERED_TIME_2);

    public static final long ORDER_MENU_SEQ_1 = 1L;
    public static final long ORDER_MENU_SEQ_2 = 2L;
    public static final long ORDER_MENU_QUANTITY_1 = 1L;
    public static final long ORDER_MENU_QUANTITY_2 = 2L;
    public static final OrderMenu ORDER_MENU_1 = new OrderMenu(ORDER_MENU_SEQ_1, ORDER_ID_1, MENU_ID_1, ORDER_MENU_QUANTITY_1);
    public static final OrderMenu ORDER_MENU_2 = new OrderMenu(ORDER_MENU_SEQ_2, ORDER_ID_2, MENU_ID_2, ORDER_MENU_QUANTITY_2);
}
