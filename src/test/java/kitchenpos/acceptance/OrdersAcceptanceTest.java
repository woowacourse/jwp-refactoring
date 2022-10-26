package kitchenpos.acceptance;

import static kitchenpos.KitchenPosFixtures.objectMapper;
import static kitchenpos.KitchenPosFixtures.까르보치킨_생성요청;
import static kitchenpos.KitchenPosFixtures.메뉴_URL;
import static kitchenpos.KitchenPosFixtures.메뉴그룹_URL;
import static kitchenpos.KitchenPosFixtures.세_마리_메뉴_생성요청;
import static kitchenpos.KitchenPosFixtures.주문_URL;
import static kitchenpos.KitchenPosFixtures.짜장치킨_생성요청;
import static kitchenpos.KitchenPosFixtures.테이블_URL;
import static kitchenpos.KitchenPosFixtures.프로덕트_URL;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrdersAcceptanceTest extends AcceptanceTest {
    private OrderTable 생성된_테이블;
    private Product 생성된_까르보치킨;
    private Product 생성된_짜장치킨;
    private MenuGroupResponse 메뉴그룹_생성응답;
    private Menu 생성된_메뉴;

    @BeforeEach
    void setUpOrders() {
        생성된_테이블 = 생성요청(테이블_URL, new OrderTableCreateRequest(5, false)).body().as(OrderTable.class);
        생성된_까르보치킨 = 생성요청(프로덕트_URL, 까르보치킨_생성요청).body().as(Product.class);
        생성된_짜장치킨 = 생성요청(프로덕트_URL, 짜장치킨_생성요청).body().as(Product.class);
        메뉴그룹_생성응답 = 생성요청(메뉴그룹_URL, 세_마리_메뉴_생성요청).body().as(MenuGroupResponse.class);
        생성된_메뉴 = 생성요청(메뉴_URL, 메뉴생성요청_데이터()).body().as(Menu.class);
    }

    public Map<String, Object> 메뉴생성요청_데이터() {
        return Map.of(
                "name", "까르보 두 마리 + 짜장 한 마리",
                "price", new BigDecimal("40000.00"),
                "menuGroupId", 메뉴그룹_생성응답.getId(),
                "menuProducts", List.of(
                        Map.of("productId", 생성된_까르보치킨.getId(), "quantity", 2),
                        Map.of("productId", 생성된_짜장치킨.getId(), "quantity", 1)
                )
        );
    }

    @Test
    void 신규_주문을_생성할_수_있다() {
        // given
        final var 주문_데이터 = Map.of("orderTableId", 생성된_테이블.getId(),
                "orderLineItems", List.of(
                        Map.of("menuId", 생성된_메뉴.getId(), "quantity", 1)
                )
        );

        // when
        final var 주문_생성응답 = 생성요청(주문_URL, 주문_데이터);
        final var 생성된_주문 = 주문_생성응답.body().as(Order.class);
        final var 주문메뉴 = 생성된_주문.getOrderLineItems().iterator().next();

        // then
        assertAll(
                응답일치(주문_생성응답, HttpStatus.CREATED),
                단일_데이터_검증(생성된_주문.getId(), 1L),
                단일_데이터_검증(생성된_주문.getOrderTableId(), 생성된_테이블.getId()),
                단일_데이터_검증(생성된_주문.getOrderStatus(), OrderStatus.COOKING.name()),
                단일_데이터_검증(주문메뉴.getMenuId(), 생성된_메뉴.getId()),
                단일_데이터_검증(주문메뉴.getOrderId(), 1L),
                단일_데이터_검증(주문메뉴.getQuantity(), 1L)
        );
    }

    @Test
    void 전체_주문을_조회할_수_있다() throws JsonProcessingException {
        // given
        final var 주문_데이터 = Map.of("orderTableId", 생성된_테이블.getId(),
                "orderLineItems", List.of(
                        Map.of("menuId", 생성된_메뉴.getId(), "quantity", 1)
                )
        );
        생성요청(주문_URL, 주문_데이터);

        // when
        final var 주문_조회응답 = 조회요청(주문_URL);
        final var 첫주문내역 = 주문_조회응답.body().as(List.class).iterator().next();
        final var 주문정보 = objectMapper.readValue(objectMapper.writeValueAsString(첫주문내역), Order.class);

        // then
        assertAll(
                응답일치(주문_조회응답, HttpStatus.OK),
                단일_데이터_검증(주문정보.getId(), 1L),
                단일_데이터_검증(주문정보.getOrderStatus(), OrderStatus.COOKING.name()),
                단일_데이터_검증(주문정보.getOrderTableId(), 생성된_테이블.getId()),
                리스트_데이터_검증(주문정보.getOrderLineItems(), "menuId", 생성된_메뉴.getId()),
                리스트_데이터_검증(주문정보.getOrderLineItems(), "quantity", 1L)
        );
    }

    @Test
    void 주문의_상태를_변경할_수_있다() {
        // given
        final var 주문_데이터 = Map.of("orderTableId", 생성된_테이블.getId(),
                "orderLineItems", List.of(
                        Map.of("menuId", 생성된_메뉴.getId(), "quantity", 1)
                )
        );
        final var 주문_아이디 = 생성요청(주문_URL, 주문_데이터).body().as(Order.class).getId();

        // when
        final var 주문_수정_URL = String.format(주문_URL + "/%d/order-status", 주문_아이디);
        final var 계산완료_요청_데이터 = Map.of("orderStatus", "COMPLETION");
        final var 수정요청_응답 = 수정요청(주문_수정_URL, 계산완료_요청_데이터);
        final var 수정된_주문 = 수정요청_응답.body().as(Order.class);

        // then
        assertAll(
                응답일치(수정요청_응답, HttpStatus.OK),
                단일_데이터_검증(수정된_주문.getOrderStatus(), OrderStatus.COMPLETION.name())
        );
    }
}
