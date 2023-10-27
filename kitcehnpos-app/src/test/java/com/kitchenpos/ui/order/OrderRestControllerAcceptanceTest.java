package com.kitchenpos.ui.order;

import com.kitchenpos.application.dto.OrderUpdateRequest;
import com.kitchenpos.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.kitchenpos.domain.OrderStatus.MEAL;
import static com.kitchenpos.fixture.OrderFixture.주문_생성;
import static com.kitchenpos.fixture.OrderFixture.주문_생성_요청;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderRestControllerAcceptanceTest extends OrderRestControllerAcceptanceTestFixture {

    private Order 주문;

    @BeforeEach
    void setup() {
        주문 = 주문_생성(orderTable.getId(), MEAL.name(), List.of(orderLineItem));
    }

    @Test
    void 주문을_생성한다() {
        // when
        var 주문_생성_결과 = 주문_생성한다("/api/orders", 주문_생성_요청(orderTable, List.of(orderLineItem)));

        // then
        주문이_성공적으로_생성된다(주문_생성_결과, 주문);
    }

    @Test
    void 모든_주문을_조회한다() {
        // given
        var 생성된_주문 = 주문_데이터를_생성한다();

        // when
        var 조회_결과 = 주문을_전체_조회한다("/api/orders");

        // then
        주문들이_성공적으로_조회된다(조회_결과, 생성된_주문);
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        var 생성된_주문 = 주문_데이터를_생성한다();
        var 변경할_상태 = new OrderUpdateRequest(MEAL.name());

        // when
        var 변경된_주문_결과 = 주문_상태를_변경한다("/api/orders/" + 생성된_주문.getId() + "/order-status", 변경할_상태);

        // then
        주문이_성공적으로_변경된다(변경된_주문_결과, 생성된_주문);
    }
}
