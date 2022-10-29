package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductDto;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderLineItemDto;
import kitchenpos.ui.request.TableChangeEmptyRequest;
import kitchenpos.ui.request.TableCreateRequest;

public class DtoFixture {

    public static MenuCreateRequest getMenuCreateRequest(final Long menuGroupId,
                                                         final List<MenuProductDto> menuProducts) {
        return new MenuCreateRequest("마이쮸 포도맛", BigDecimal.valueOf(800), menuGroupId, menuProducts);
    }

    public static OrderCreateRequest getOrderCreateRequest(final Long orderTableId, final Long menuId) {
        return new OrderCreateRequest(
                orderTableId,
                getOrderLineItems(menuId, 1));
    }

    private static List<OrderLineItemDto> getOrderLineItems(final Long menuId, final long quantity) {
        return List.of(new OrderLineItemDto(menuId, quantity));
    }

    public static TableCreateRequest getEmptyTableCreateRequest() {
        return new TableCreateRequest(0, true);
    }

    public static TableCreateRequest getNotEmptyTableCreateRequest(final int numberOfGuests) {
        return new TableCreateRequest(numberOfGuests, false);
    }

    public static TableChangeEmptyRequest getTableChangeEmptyRequest(final boolean empty) {
        return new TableChangeEmptyRequest(empty);
    }
}
