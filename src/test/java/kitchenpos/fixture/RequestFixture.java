package kitchenpos.fixture;

import static java.util.Arrays.*;
import static java.util.Collections.*;

import java.math.BigDecimal;

import kitchenpos.application.command.ChangeNumberOfOrderTableGuestsCommand;
import kitchenpos.application.command.ChangeOrderStatusCommand;
import kitchenpos.application.command.ChangeOrderTableEmptyCommand;
import kitchenpos.application.command.CreateMenuCommand;
import kitchenpos.application.command.CreateMenuGroupCommand;
import kitchenpos.application.command.CreateOrderCommand;
import kitchenpos.application.command.CreateOrderTableCommand;
import kitchenpos.application.command.CreateProductCommand;
import kitchenpos.application.command.CreateTableGroupCommand;
import kitchenpos.domain.model.AggregateReference;
import kitchenpos.domain.model.menu.MenuProduct;
import kitchenpos.domain.model.order.OrderLineItem;

public class RequestFixture {
    public static final CreateMenuGroupCommand MENU_GROUP_REQUEST = new CreateMenuGroupCommand(
            "추천메뉴");
    public static final CreateMenuCommand MENU_REQUEST = new CreateMenuCommand("후라이드+후라이드",
            BigDecimal.valueOf(19_000L), 1L, singletonList(new MenuProduct(null, null, 1L, 2L)));
    public static final CreateProductCommand PRODUCT_REQUEST = new CreateProductCommand("강정치킨",
            BigDecimal.valueOf(17_000L));
    public static final CreateOrderTableCommand ORDER_TABLE_REQUEST = new CreateOrderTableCommand(0,
            true);
    public static final ChangeOrderTableEmptyCommand ORDER_TABLE_CHANGE_EMPTY_REQUEST = new ChangeOrderTableEmptyCommand(
            true);
    public static final ChangeNumberOfOrderTableGuestsCommand ORDER_TABLE_CHANGE_NUMBER_OF_GUESTS = new ChangeNumberOfOrderTableGuestsCommand(
            1);
    public static final CreateTableGroupCommand TABLE_GROUP_CREATE_REQUEST = new CreateTableGroupCommand(
            asList(new AggregateReference<>(1L), new AggregateReference<>(2L)));
    public static final CreateOrderCommand ORDER_CREATE_REQUEST = new CreateOrderCommand(1L,
            singletonList(new OrderLineItem(null, null, 1L, 1L)));

    public static final ChangeOrderStatusCommand ORDER_STATUS_CHANGE_REQUEST1 = new ChangeOrderStatusCommand(
            "MEAL");
    public static final ChangeOrderStatusCommand ORDER_STATUS_CHANGE_REQUEST2 = new ChangeOrderStatusCommand(
            "COMPLETION");
}
