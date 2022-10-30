package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ui.request.menu.MenuCreateRequest;
import kitchenpos.ui.request.menu.MenuProductDto;
import kitchenpos.ui.request.menugroup.MenuGroupCreateRequest;
import kitchenpos.ui.request.order.OrderCreateRequest;
import kitchenpos.ui.request.order.OrderLineItemDto;
import kitchenpos.ui.request.prodcut.ProductCreateRequest;
import kitchenpos.ui.request.table.TableChangeEmptyRequest;
import kitchenpos.ui.request.table.TableChangeNumberOfGuestsRequest;
import kitchenpos.ui.request.table.TableCreateRequest;
import kitchenpos.ui.request.tablegroup.OrderTableDto;
import kitchenpos.ui.request.tablegroup.TableGroupCreatRequest;

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

    public static TableChangeNumberOfGuestsRequest getTableChangeNumberOfGuestsRequest(final int number) {
        return new TableChangeNumberOfGuestsRequest(number);
    }

    public static TableGroupCreatRequest getTableCreateRequest(final List<Long> tableIds) {
        List<OrderTableDto> orderTableDtos = tableIds.stream()
                .map(OrderTableDto::new)
                .collect(Collectors.toList());
        return new TableGroupCreatRequest(orderTableDtos);
    }

    public static ProductCreateRequest getProductCreateRequest(final String name, final BigDecimal price) {
        return new ProductCreateRequest(name, price);
    }

    public static MenuGroupCreateRequest getMenuGroupCreateRequest() {
        return new MenuGroupCreateRequest("마이쮸 1종 세트");
    }
}
