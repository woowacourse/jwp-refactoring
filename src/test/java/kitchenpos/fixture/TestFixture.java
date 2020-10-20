package kitchenpos.fixture;

import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public abstract class TestFixture {

    public static final long MENU_GROUP_ID_1 = 1L;
    public static final long MENU_GROUP_ID_2 = 2L;
    public static final String MENU_GROUP_NAME_1 = "메뉴그룹이름1";
    public static final String MENU_GROUP_NAME_2 = "메뉴그룹이름2";
    public static final MenuGroup MENU_GROUP_1 = new MenuGroup();
    public static final MenuGroup MENU_GROUP_2 = new MenuGroup();

    public static final long MENU_PRODUCT_SEQ_1 = 1L;
    public static final long MENU_PRODUCT_SEQ_2 = 2L;
    public static final long MENU_PRODUCT_PRODUCT_ID_1 = 1L;
    public static final long MENU_PRODUCT_PRODUCT_ID_2 = 2L;
    public static final long MENU_PRODUCT_QUANTITY_1 = 1L;
    public static final long MENU_PRODUCT_QUANTITY_2 = 2L;
    public static final MenuProduct MENU_PRODUCT_1 = new MenuProduct();
    public static final MenuProduct MENU_PRODUCT_2 = new MenuProduct();

    public static final long MENU_ID_1 = 1L;
    public static final long MENU_ID_2 = 2L;
    public static final String MENU_NAME_1 = "메뉴이름1";
    public static final String MENU_NAME_2 = "메뉴이름2";
    public static final BigDecimal MENU_PRICE_1 = BigDecimal.valueOf(1L, 2);
    public static final BigDecimal MENU_PRICE_2 = BigDecimal.valueOf(2L, 2);
    public static final List<MenuProduct> MENU_PRODUCTS_1 = Arrays.asList(MENU_PRODUCT_1);
    public static final List<MenuProduct> MENU_PRODUCTS_2 = Arrays.asList(MENU_PRODUCT_2);
    public static final Menu MENU_1 = new Menu();
    public static final Menu MENU_2 = new Menu();

    public static final long PRODUCT_ID_1 = 1L;
    public static final long PRODUCT_ID_2 = 2L;
    public static final String PRODUCT_NAME_1 = "제품이름1";
    public static final String PRODUCT_NAME_2 = "제품이름2";
    public static final BigDecimal PRODUCT_PRICE_1 = BigDecimal.valueOf(1L, 2);
    public static final BigDecimal PRODUCT_PRICE_2 = BigDecimal.valueOf(2L, 2);
    public static final Product PRODUCT_1 = new Product();
    public static final Product PRODUCT_2 = new Product();

    public static final long ORDER_LINE_ITEM_SEQ_1 = 1L;
    public static final long ORDER_LINE_ITEM_SEQ_2 = 2L;
    public static final long ORDER_LINE_ITEM_QUANTITY_1 = 1L;
    public static final long ORDER_LINE_ITEM_QUANTITY_2 = 2L;
    public static final OrderLineItem ORDER_LINE_ITEM_1 = new OrderLineItem();
    public static final OrderLineItem ORDER_LINE_ITEM_2 = new OrderLineItem();

    public static final long ORDER_ID_1 = 1L;
    public static final long ORDER_ID_2 = 2L;
    public static final String ORDER_STATUS_1 = "COOKING";
    public static final String ORDER_STATUS_2 = "COOKING";
    public static final LocalDateTime ORDERED_TIME_1 = LocalDateTime.parse("2018-12-15T10:00:00");
    public static final LocalDateTime ORDERED_TIME_2 = LocalDateTime.parse("2018-12-16T10:00:00");
    public static final List<OrderLineItem> ORDER_LINE_ITEMS_1 = Arrays.asList(ORDER_LINE_ITEM_1);
    public static final List<OrderLineItem> ORDER_LINE_ITEMS_2 = Arrays.asList(ORDER_LINE_ITEM_2);
    public static final Order ORDER_1 = new Order();
    public static final Order ORDER_2 = new Order();

    public static final long ORDER_TABLE_ID_1 = 1L;
    public static final long ORDER_TABLE_ID_2 = 2L;
    public static final int ORDER_TABLE_NUMBER_OF_GUESTS_1 = 1;
    public static final int ORDER_TABLE_NUMBER_OF_GUESTS_2 = 2;
    public static final boolean ORDER_TABLE_EMPTY_1 = false;
    public static final boolean ORDER_TABLE_EMPTY_2 = false;
    public static final OrderTable ORDER_TABLE_1 = new OrderTable();
    public static final OrderTable ORDER_TABLE_2 = new OrderTable();

    public static final long TABLE_GROUP_ID = 1L;
    public static final LocalDateTime TABLE_GROUP_CREATED_DATE = LocalDateTime.parse("2018-11-15T10:00:00");
    public static final List<OrderTable> ORDER_TABLES = Arrays.asList(ORDER_TABLE_1, ORDER_TABLE_2);
    public static final TableGroup TABLE_GROUP = new TableGroup();

    @BeforeEach
    void setUpFixture() {
        MENU_GROUP_1.setId(MENU_GROUP_ID_1);
        MENU_GROUP_1.setName(MENU_GROUP_NAME_1);
        MENU_GROUP_2.setId(MENU_GROUP_ID_2);
        MENU_GROUP_2.setName(MENU_GROUP_NAME_2);

        MENU_PRODUCT_1.setSeq(MENU_PRODUCT_SEQ_1);
        MENU_PRODUCT_1.setMenuId(MENU_ID_1);
        MENU_PRODUCT_1.setProductId(MENU_PRODUCT_PRODUCT_ID_1);
        MENU_PRODUCT_1.setQuantity(MENU_PRODUCT_QUANTITY_1);
        MENU_PRODUCT_2.setSeq(MENU_PRODUCT_SEQ_2);
        MENU_PRODUCT_2.setMenuId(MENU_ID_2);
        MENU_PRODUCT_2.setProductId(MENU_PRODUCT_PRODUCT_ID_2);
        MENU_PRODUCT_2.setQuantity(MENU_PRODUCT_QUANTITY_2);

        MENU_1.setId(MENU_ID_1);
        MENU_1.setName(MENU_NAME_1);
        MENU_1.setPrice(MENU_PRICE_1);
        MENU_1.setMenuGroupId(MENU_GROUP_ID_1);
        MENU_1.setMenuProducts(MENU_PRODUCTS_1);
        MENU_2.setId(MENU_ID_2);
        MENU_2.setName(MENU_NAME_2);
        MENU_2.setPrice(MENU_PRICE_2);
        MENU_2.setMenuGroupId(MENU_GROUP_ID_2);
        MENU_2.setMenuProducts(MENU_PRODUCTS_2);

        PRODUCT_1.setId(PRODUCT_ID_1);
        PRODUCT_1.setName(PRODUCT_NAME_1);
        PRODUCT_1.setPrice(PRODUCT_PRICE_1);
        PRODUCT_2.setId(PRODUCT_ID_2);
        PRODUCT_2.setName(PRODUCT_NAME_2);
        PRODUCT_2.setPrice(PRODUCT_PRICE_2);

        ORDER_LINE_ITEM_1.setSeq(ORDER_LINE_ITEM_SEQ_1);
        ORDER_LINE_ITEM_1.setOrderId(ORDER_ID_1);
        ORDER_LINE_ITEM_1.setMenuId(MENU_ID_1);
        ORDER_LINE_ITEM_1.setQuantity(ORDER_LINE_ITEM_QUANTITY_1);
        ORDER_LINE_ITEM_2.setSeq(ORDER_LINE_ITEM_SEQ_2);
        ORDER_LINE_ITEM_2.setOrderId(ORDER_ID_2);
        ORDER_LINE_ITEM_2.setMenuId(MENU_ID_2);
        ORDER_LINE_ITEM_2.setQuantity(ORDER_LINE_ITEM_QUANTITY_2);

        ORDER_1.setId(ORDER_ID_1);
        ORDER_1.setOrderTableId(ORDER_TABLE_ID_1);
        ORDER_1.setOrderStatus(ORDER_STATUS_1);
        ORDER_1.setOrderedTime(ORDERED_TIME_1);
        ORDER_1.setOrderLineItems(ORDER_LINE_ITEMS_1);
        ORDER_2.setId(ORDER_ID_2);
        ORDER_2.setOrderTableId(ORDER_TABLE_ID_2);
        ORDER_2.setOrderStatus(ORDER_STATUS_2);
        ORDER_2.setOrderedTime(ORDERED_TIME_2);
        ORDER_2.setOrderLineItems(ORDER_LINE_ITEMS_2);

        ORDER_LINE_ITEM_1.setSeq(ORDER_LINE_ITEM_SEQ_1);
        ORDER_LINE_ITEM_1.setOrderId(ORDER_ID_1);
        ORDER_LINE_ITEM_1.setMenuId(MENU_ID_1);
        ORDER_LINE_ITEM_1.setQuantity(ORDER_LINE_ITEM_QUANTITY_1);
        ORDER_LINE_ITEM_2.setSeq(ORDER_LINE_ITEM_SEQ_2);
        ORDER_LINE_ITEM_2.setOrderId(ORDER_ID_2);
        ORDER_LINE_ITEM_2.setMenuId(MENU_ID_2);
        ORDER_LINE_ITEM_2.setQuantity(ORDER_LINE_ITEM_QUANTITY_2);

        ORDER_1.setId(ORDER_ID_1);
        ORDER_1.setOrderTableId(ORDER_TABLE_ID_1);
        ORDER_1.setOrderStatus(ORDER_STATUS_1);
        ORDER_1.setOrderedTime(ORDERED_TIME_1);
        ORDER_1.setOrderLineItems(ORDER_LINE_ITEMS_1);
        ORDER_2.setId(ORDER_ID_2);
        ORDER_2.setOrderTableId(ORDER_TABLE_ID_2);
        ORDER_2.setOrderStatus(ORDER_STATUS_2);
        ORDER_2.setOrderedTime(ORDERED_TIME_2);
        ORDER_2.setOrderLineItems(ORDER_LINE_ITEMS_2);

        ORDER_TABLE_1.setId(ORDER_TABLE_ID_1);
        ORDER_TABLE_1.setTableGroupId(TABLE_GROUP_ID);
        ORDER_TABLE_1.setNumberOfGuests(ORDER_TABLE_NUMBER_OF_GUESTS_1);
        ORDER_TABLE_1.setEmpty(ORDER_TABLE_EMPTY_1);
        ORDER_TABLE_2.setId(ORDER_TABLE_ID_2);
        ORDER_TABLE_2.setTableGroupId(TABLE_GROUP_ID);
        ORDER_TABLE_2.setNumberOfGuests(ORDER_TABLE_NUMBER_OF_GUESTS_2);
        ORDER_TABLE_2.setEmpty(ORDER_TABLE_EMPTY_2);

        TABLE_GROUP.setId(TABLE_GROUP_ID);
        TABLE_GROUP.setCreatedDate(TABLE_GROUP_CREATED_DATE);
        TABLE_GROUP.setOrderTables(ORDER_TABLES);
    }
}
