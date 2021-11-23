package kitchenpos.acceptance;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.fixture.*;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.request.OrderChangeStatusRequest;
import kitchenpos.ui.dto.request.OrderCreatedRequest;
import kitchenpos.ui.dto.request.OrderLineItemRequest;
import kitchenpos.ui.dto.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MenuFixture menuFixture;
    @Autowired
    private OrderFixture orderFixture;
    @Autowired
    private OrderLineItemFixture orderLineItemFixture;
    @Autowired
    private ProductFixture productFixture;
    @Autowired
    private OrderTableFixture orderTableFixture;

    private final MenuProductFixture menuProductFixture = new MenuProductFixture();
    private final MenuGroupFixture menuGroupFixture = new MenuGroupFixture();

    private List<MenuProductRequest> 메뉴_상품_요청_리스트;
    private List<OrderLineItemRequest> 주문_메뉴_요청_리스트;
    private OrderTableResponse 주문_테이블1_응답;
    private OrderCreatedRequest 주문1_생성_요청;

    @BeforeEach
    void setup() {
        ProductResponse 상품1_응답 = 상품_등록(productFixture.상품_생성_요청("상품1", BigDecimal.valueOf(1000)));
        ProductResponse 상품2_응답 = 상품_등록(productFixture.상품_생성_요청("상품2", BigDecimal.valueOf(2000)));
        MenuProductRequest 메뉴_상품1_요청 = menuProductFixture.메뉴_상품_생성_요청(상품1_응답.getId(), 1L);
        MenuProductRequest 메뉴_상품2_요청 = menuProductFixture.메뉴_상품_생성_요청(상품2_응답.getId(), 1L);
        메뉴_상품_요청_리스트 = menuProductFixture.메뉴_상품_요청_리스트_생성(메뉴_상품1_요청, 메뉴_상품2_요청);

        MenuGroupResponse 메뉴그룹1_응답 = 메뉴그룹_등록(menuGroupFixture.메뉴그룹_생성_요청("메뉴그룹1"));
        MenuResponse 메뉴1_응답 = 메뉴_등록(menuFixture.메뉴_생성_요청("메뉴1", BigDecimal.valueOf(1000), 메뉴그룹1_응답.getId(), 메뉴_상품_요청_리스트));
        MenuResponse 메뉴2_응답 = 메뉴_등록(menuFixture.메뉴_생성_요청("메뉴2", BigDecimal.valueOf(2000), 메뉴그룹1_응답.getId(), 메뉴_상품_요청_리스트));

        OrderLineItemRequest 주문_메뉴1_요청 = orderLineItemFixture.주문_메뉴_생성_요청(메뉴1_응답.getId(), 1L);
        OrderLineItemRequest 주문_메뉴2_요청 = orderLineItemFixture.주문_메뉴_생성_요청(메뉴2_응답.getId(), 1L);
        주문_메뉴_요청_리스트 = orderLineItemFixture.주문_메뉴_요청_리스트_생성(주문_메뉴1_요청, 주문_메뉴2_요청);
        주문_테이블1_응답 = 주문_테이블_등록(orderTableFixture.주문_테이블_생성_요청(2, false));
        주문1_생성_요청 = orderFixture.주문_생성_요청(주문_테이블1_응답.getId(), 주문_메뉴_요청_리스트);
    }

    @Test
    @DisplayName("주문 생성 테스트 - 성공")
    void createTest() {
        // when
        OrderResponse 주문1_응답 = 주문_등록(주문1_생성_요청);
        Order 주문1 = orderFixture.주문_조회(주문1_응답.getId());

        // then
        assertThat(주문1_응답).usingRecursiveComparison()
                .isEqualTo(OrderResponse.create(주문1, orderLineItemFixture.특정_주문에_따른_주문_메뉴들_조회(주문1)));
    }

    @Test
    @DisplayName("주문 리스트 조회 테스트 - 성공")
    void listTest() {
        // given
        OrderTableResponse 주문_테이블2_응답 = 주문_테이블_등록(orderTableFixture.주문_테이블_생성_요청(4, false));
        OrderCreatedRequest 주문2_생성_요청 = orderFixture.주문_생성_요청(주문_테이블2_응답.getId(), 주문_메뉴_요청_리스트);
        List<OrderResponse> expected = orderFixture.주문_응답_리스트_생성(주문_등록(주문1_생성_요청), 주문_등록(주문2_생성_요청));

        // when
        List<OrderResponse> actual = 주문_리스트_조회();

        // then
        assertThat(actual).hasSize(expected.size());
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("주문 상태 변경 테스트 - 성공")
    void changeOrderStatus() {
        // given
        OrderResponse 주문1_응답 = 주문_등록(주문1_생성_요청);
        Long 주문1_id = 주문1_응답.getId();
        OrderChangeStatusRequest 주문1_식사완료_요청 = new OrderChangeStatusRequest(OrderStatus.COMPLETION);

        // when
        OrderResponse 주문1_식사완료 = 주문_상태_변경(주문1_id, 주문1_식사완료_요청);


        // then
        assertThat(주문1_식사완료.getOrderStatus()).isEqualTo(주문1_식사완료_요청.getOrderStatus().name());
    }
}
