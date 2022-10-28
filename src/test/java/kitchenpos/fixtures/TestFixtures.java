package kitchenpos.fixtures;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuGroupCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.TableCreateRequest;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import kitchenpos.ui.dto.TableUpdateEmptyRequest;
import kitchenpos.ui.dto.TableUpdateGuestNumberRequest;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixtures {

    private TestFixtures() {
    }

    public static Product 상품_생성(final String name, final BigDecimal price) {
        return new Product(name, price);
    }

    public static MenuGroup 메뉴_그룹_생성(final String name) {
        return new MenuGroup(name);
    }

    public static MenuGroupCreateRequest 메뉴_그룹_생성_요청(final String name) {
        return new MenuGroupCreateRequest(name);
    }

    public static Menu 메뉴_생성(final String name, final BigDecimal price,
                             final Long menuGroupId, final List<MenuProduct> menuProducts) {
        return new Menu(name, price, null, menuProducts);
    }

    public static MenuCreateRequest 메뉴_생성_요청(final String name,
                                             final BigDecimal price,
                                             final Long menuGroupId,
                                             final List<MenuProductCreateRequest> menuProductCreateRequests) {
        return new MenuCreateRequest(name, price, menuGroupId, menuProductCreateRequests);
    }

    public static MenuProduct 메뉴_상품_생성(final Long menuId, final Long productId, final long quantity) {
        return new MenuProduct(null, null, quantity);
    }

    public static MenuProductCreateRequest 메뉴_상품_생성_요청(final Long menuId,
                                                       final Long productId,
                                                       final long quantity) {
        return new MenuProductCreateRequest(1L, quantity);
    }

    public static Order 주문_생성(final OrderTable orderTable,
                              final OrderStatus orderStatus,
                              final LocalDateTime orderedTime,
                              final List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public static OrderCreateRequest 주문_생성_요청(final Long orderTableId,
                                              final List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderCreateRequest(orderTableId, orderLineItemRequests);
    }

    public static OrderLineItemRequest 주문_항목_요청(final Long menuId, final Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public static OrderLineItem 주문_항목_생성(final Long menuId, final long quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    public static OrderTable 주문_테이블_생성(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    public static TableUpdateEmptyRequest 주문_테이블_Empty_변경_요청(final boolean empty) {
        return new TableUpdateEmptyRequest(empty);
    }

    public static TableUpdateGuestNumberRequest 주문_테이블_손님_수_변경_요청(final int numberOfGuests) {
        return new TableUpdateGuestNumberRequest(numberOfGuests);
    }


    public static TableCreateRequest 주문_테이블_생성_요청(final int numberOfGuests, final boolean empty) {
        return new TableCreateRequest(numberOfGuests, empty);
    }

    public static TableGroup 단체_지정_생성(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public static TableGroupCreateRequest 단체_지정_생성_요청(final List<OrderTableRequest> orderTableRequests) {
        return new TableGroupCreateRequest(orderTableRequests);
    }

    public static OrderTableRequest 주문_테이블_요청(final Long id) {
        return new OrderTableRequest(id);
    }
}
