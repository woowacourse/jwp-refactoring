package kitchenpos.acceptance;

import static kitchenpos.acceptance.fixture.MenuGroupStepDefinition.메뉴_그룹을_생성한다;
import static kitchenpos.acceptance.fixture.MenuStepDefinition.메뉴를_생성한다;
import static kitchenpos.acceptance.fixture.OrderStepDefinition.주문을_생성한다;
import static kitchenpos.acceptance.fixture.OrderStepDefinition.주문을_조회한다;
import static kitchenpos.acceptance.fixture.OrderStepDefinition.주문의_상태를_변경한다;
import static kitchenpos.acceptance.fixture.ProductStepDefinition.상품을_생성한다;
import static kitchenpos.acceptance.fixture.TableStepDefinition.테이블을_생성한다;
import static kitchenpos.support.fixture.MenuFixtures.후라이드메뉴;
import static kitchenpos.support.fixture.MenuGroupFixture.한마리메뉴;
import static kitchenpos.support.fixture.ProductFixtures.후라이드상품;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.Test;

public class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    void 주문을_조회할_수_있다() {
        // given
        long 한마리_메뉴_그룹 = 메뉴_그룹을_생성한다(한마리메뉴.getName());
        long 후라이드_상품 = 상품을_생성한다(후라이드상품.getName(), 후라이드상품.getPrice());
        long 후라이드_메뉴 = 메뉴를_생성한다(후라이드메뉴.getName(), 후라이드메뉴.getPrice(), 한마리_메뉴_그룹, List.of(후라이드_상품), 1);
        long 테이블 = 테이블을_생성한다(1, false);

        // and
        주문을_생성한다(테이블, LocalDateTime.now(), List.of(후라이드_메뉴), 1);

        // when
        List<Order> extract = 주문을_조회한다();

        // then
        assertThat(extract).hasSize(1);
    }

    @Test
    void 주문의_상태를_변경할_수_있다() {
        // given
        long 한마리_메뉴_그룹 = 메뉴_그룹을_생성한다(한마리메뉴.getName());
        long 후라이드_상품 = 상품을_생성한다(후라이드상품.getName(), 후라이드상품.getPrice());
        long 후라이드_메뉴 = 메뉴를_생성한다(후라이드메뉴.getName(), 후라이드메뉴.getPrice(), 한마리_메뉴_그룹, List.of(후라이드_상품), 1);
        long 테이블 = 테이블을_생성한다(1, false);
        long 주문 = 주문을_생성한다(테이블, LocalDateTime.now(), List.of(후라이드_메뉴), 1);

        // when
        주문의_상태를_변경한다(주문, OrderStatus.MEAL.name());
    }
}
