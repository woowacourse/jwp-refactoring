package kitchenpos.support.fixture;

import java.util.List;
import kitchenpos.table.application.dto.OrderTableDto;
import kitchenpos.table.domain.OrderTable;

public class TableFixture {

    public static OrderTable 비어있는_주문_테이블() {
        return new OrderTable(0, true);
    }

    public static OrderTable 비어있지_않는_주문_테이블() {
        return new OrderTable(0, false);
    }

    public static OrderTableDto 비어있는_주문_테이블_DTO() {
        return new OrderTableDto(null, 0L, 0, true);
    }

    public static OrderTableDto 비어있지_않는_주문_테이블_DTO() {
        return new OrderTableDto(null, 0L, 3, false);
    }

    public static List<OrderTable> 비어있지_않은_전체_주문_테이블() {
        return List.of(
            비어있지_않는_주문_테이블(),
            비어있지_않는_주문_테이블(),
            비어있지_않는_주문_테이블(),
            비어있지_않는_주문_테이블(),
            비어있지_않는_주문_테이블(),
            비어있지_않는_주문_테이블(),
            비어있지_않는_주문_테이블(),
            비어있지_않는_주문_테이블()
        );
    }

    public static List<OrderTable> 비어있는_전체_주문_테이블() {
        return List.of(
            비어있는_주문_테이블(),
            비어있는_주문_테이블(),
            비어있는_주문_테이블(),
            비어있는_주문_테이블(),
            비어있는_주문_테이블(),
            비어있는_주문_테이블(),
            비어있는_주문_테이블(),
            비어있는_주문_테이블()
        );
    }

    public static List<OrderTableDto> 비어있는_전쳬_주문_테이블_DTO() {
        return List.of(
            비어있는_주문_테이블_DTO(),
            비어있는_주문_테이블_DTO(),
            비어있는_주문_테이블_DTO(),
            비어있는_주문_테이블_DTO(),
            비어있는_주문_테이블_DTO(),
            비어있는_주문_테이블_DTO(),
            비어있는_주문_테이블_DTO(),
            비어있는_주문_테이블_DTO()
        );
    }

    public static List<OrderTableDto> 비어있지_않는_전쳬_주문_테이블_DTO() {
        return List.of(
            비어있지_않는_주문_테이블_DTO(),
            비어있지_않는_주문_테이블_DTO(),
            비어있지_않는_주문_테이블_DTO(),
            비어있지_않는_주문_테이블_DTO(),
            비어있지_않는_주문_테이블_DTO(),
            비어있지_않는_주문_테이블_DTO(),
            비어있지_않는_주문_테이블_DTO(),
            비어있지_않는_주문_테이블_DTO()
        );
    }
}
