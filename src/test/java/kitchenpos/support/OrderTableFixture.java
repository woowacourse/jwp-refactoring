package kitchenpos.support;

import kitchenpos.table.application.dto.OrderTableRequestDto;

public class OrderTableFixture {

    public static OrderTableRequestDto 비어있는_주문_테이블 = 주문_테이블_생성(null, 2, true);
    public static OrderTableRequestDto 비어있지_않은_주문_테이블 = 주문_테이블_생성(null, 2, false);

    public static OrderTableRequestDto 주문_테이블_생성(final Long tableGroupId, final Integer numberOfGuests,
                                                 final Boolean isEmpty) {
        return new OrderTableRequestDto(tableGroupId, numberOfGuests, isEmpty);
    }

}
