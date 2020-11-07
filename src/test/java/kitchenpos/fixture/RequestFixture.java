package kitchenpos.fixture;

import static java.util.Arrays.*;
import static java.util.Collections.*;

import java.math.BigDecimal;

import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuests;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.core.AggregateReference;
import kitchenpos.domain.MenuProduct;

public class RequestFixture {
    public static final MenuGroupRequest MENU_GROUP_REQUEST = new MenuGroupRequest("추천메뉴");
    public static final MenuRequest MENU_REQUEST = new MenuRequest("후라이드+후라이드",
            BigDecimal.valueOf(19_000L), 1L, singletonList(new MenuProduct(null, null, 1L, 2L)));
    public static final ProductRequest PRODUCT_REQUEST = new ProductRequest("강정치킨",
            BigDecimal.valueOf(17_000L));
    public static final OrderTableCreateRequest ORDER_TABLE_REQUEST = new OrderTableCreateRequest(0,
            true);
    public static final OrderTableChangeEmptyRequest ORDER_TABLE_CHANGE_EMPTY_REQUEST = new OrderTableChangeEmptyRequest(
            true);
    public static final OrderTableChangeNumberOfGuests ORDER_TABLE_CHANGE_NUMBER_OF_GUESTS = new OrderTableChangeNumberOfGuests(
            1);
    public static final TableGroupCreateRequest TABLE_GROUP_CREATE_REQUEST = new TableGroupCreateRequest(
            asList(new AggregateReference<>(1L), new AggregateReference<>(2L)));
}
