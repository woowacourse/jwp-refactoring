package kitchenpos.application;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.application.happy.HappyServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.request.MenuGroupRequest;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.ui.dto.request.OrderLineItemRequest;
import kitchenpos.ui.dto.request.OrderRequest;
import kitchenpos.ui.dto.request.OrderTableRequest;
import kitchenpos.product.ui.request.ProductRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class HappyOrderServiceTest extends HappyServiceTest {

    @Test
    void 주문_생성() {
        final Long 테이블_번호 = tableService.create(new OrderTableRequest(5, false)).getId();

        final MenuGroupRequest 코틀린하는_사람들이_먹는_떡볶이_그룹_요청 = new MenuGroupRequest("코틀린하는 사람들이 먹는 떡볶이");
        final Long 코틀린하는_사람들이_먹는_떡볶이_ID = menuGroupService.create(코틀린하는_사람들이_먹는_떡볶이_그룹_요청).getId();

        final ProductRequest 간장_떡볶이_요청 = new ProductRequest("간장 떡볶이", BigDecimal.TEN);
        final ProductRequest 주먹밥_요청 = new ProductRequest("주먹밥", BigDecimal.TEN);
        final ProductRequest 매운_떡볶이_요청 = new ProductRequest("매운 떡볶이", BigDecimal.TEN);
        final ProductRequest 쿨피스_요청 = new ProductRequest("쿨피스", BigDecimal.TEN);

        final Long 간장_떡볶이_ID = productService.create(간장_떡볶이_요청).getId();
        final Long 주먹밥_ID = productService.create(주먹밥_요청).getId();
        final Long 매운_떡볶이_ID = productService.create(매운_떡볶이_요청).getId();
        final Long 쿨피스_ID = productService.create(쿨피스_요청).getId();

        final MenuProductRequest 간장_떡볶이 = new MenuProductRequest(간장_떡볶이_ID, 5);
        final MenuProductRequest 주먹밥 = new MenuProductRequest(주먹밥_ID, 5);
        final MenuProductRequest 매운_떡볶이 = new MenuProductRequest(매운_떡볶이_ID, 5);
        final MenuProductRequest 쿨피스 = new MenuProductRequest(쿨피스_ID, 5);

        final MenuRequest 제이슨이_좋아하는_떡볶이 = new MenuRequest("제이슨이 좋아하는 떡볶이", BigDecimal.TEN, 코틀린하는_사람들이_먹는_떡볶이_ID,
                Arrays.asList(간장_떡볶이, 주먹밥, 매운_떡볶이, 쿨피스));
        final Menu menu = menuService.create(제이슨이_좋아하는_떡볶이);
        final OrderLineItemRequest 주문_상세_요청 = new OrderLineItemRequest(menu.getId(), 2);
        final OrderRequest 주문요청 = new OrderRequest(테이블_번호, Arrays.asList(주문_상세_요청));

        final Order 주문 = orderService.create(주문요청);

        Assertions.assertThat(주문).isNotNull();

    }
}
