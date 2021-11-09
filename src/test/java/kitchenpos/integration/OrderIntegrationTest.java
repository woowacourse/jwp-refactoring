package kitchenpos.integration;

import static kitchenpos.integration.api.texture.ProductTexture.*;

import java.util.Collections;
import kitchenpos.application.response.OrderResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.integration.api.MenuApi;
import kitchenpos.integration.api.MenuGroupApi;
import kitchenpos.integration.api.OrderApi;
import kitchenpos.integration.api.ProductApi;
import kitchenpos.integration.api.TableApi;
import kitchenpos.integration.utils.MockMvcResponse;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderLineItemRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderApi orderApi;
    @Autowired
    private TableApi tableApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private MenuApi menuApi;
    @Autowired
    private MenuGroupApi menuGroupApi;

    private MenuResponse 기본_메뉴;

    @BeforeEach
    void setUp() {
        final Long menuGroupId = menuGroupApi.메뉴_그룹_등록("존맛탱").getContent().getId();
        final Long productId = productApi.상품_등록(민초치킨).getContent().getId();
        final MenuProductRequest menuProduct = MenuProductRequest.create(productId, 1);

        기본_메뉴 = menuApi
            .메뉴_등록(민초치킨, menuGroupId, Collections.singletonList(menuProduct))
            .getContent();
    }

    @Test
    public void 주문_성공() {
        //given
        final Long orderTableId = tableApi.테이블_등록(2, false).getContent().getId();

        //when
        final MockMvcResponse<OrderResponse> result = orderApi.주문(
            OrderCreateRequest.create(orderTableId, Collections.singletonList(OrderLineItemRequest.create(기본_메뉴.getId(), 3)))
        );

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(result.getContent().getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString());
    }

    @Test
    public void 주문_상태_변경() {
        //given
        final Long orderTableId = tableApi.테이블_등록(2, false).getContent().getId();

        final OrderResponse order = orderApi.주문(
            OrderCreateRequest.create(orderTableId, Collections.singletonList(OrderLineItemRequest.create(기본_메뉴.getId(), 3)))
        ).getContent();

        //when
        final MockMvcResponse<OrderResponse> result = orderApi
            .주문_상태_변경(order.getId(), OrderStatus.COMPLETION);

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(result.getContent().getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.toString());
    }
}
