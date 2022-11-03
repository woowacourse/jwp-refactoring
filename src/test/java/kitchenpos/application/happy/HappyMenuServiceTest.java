package kitchenpos.application.happy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.request.MenuGroupRequest;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.request.MenuRequest;
import kitchenpos.product.ui.request.ProductRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HappyMenuServiceTest extends HappyServiceTest {

    @Autowired
    private MenuService menuService;


    @Test
    void 메뉴_생성() {
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

        Assertions.assertThat(menu.getId()).isNotNull();
    }

    @Test
    void 메뉴_목록_조회() {
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

        final MenuRequest 더즈_떡볶이 = new MenuRequest("더즈_떡볶이", BigDecimal.TEN, 코틀린하는_사람들이_먹는_떡볶이_ID,
                Arrays.asList(간장_떡볶이, 쿨피스));

        final MenuRequest 스모디_떡볶이 = new MenuRequest("스모디_떡볶이", BigDecimal.TEN, 코틀린하는_사람들이_먹는_떡볶이_ID,
                Arrays.asList(매운_떡볶이, 주먹밥));

        menuService.create(더즈_떡볶이);
        menuService.create(스모디_떡볶이);

        Assertions.assertThat(menuService.list().size()).isEqualTo(2);
    }


}
