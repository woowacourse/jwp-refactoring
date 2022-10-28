package kitchenpos.acceptance;

import static kitchenpos.acceptance.fixture.MenuGroupStepDefinition.메뉴_그룹을_생성한다;
import static kitchenpos.acceptance.fixture.MenuStepDefinition.메뉴를_생성한다;
import static kitchenpos.acceptance.fixture.MenuStepDefinition.메뉴를_조회한다;
import static kitchenpos.acceptance.fixture.ProductStepDefinition.상품을_생성한다;
import static kitchenpos.support.fixture.MenuGroupFixture.두마리메뉴;
import static kitchenpos.support.fixture.MenuGroupFixture.한마리메뉴;
import static kitchenpos.support.fixture.ProductFixtures.반반치킨상품;
import static kitchenpos.support.fixture.ProductFixtures.양념치킨상품;
import static kitchenpos.support.fixture.ProductFixtures.후라이드상품;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.MenuResponse;
import org.junit.jupiter.api.Test;

public class MenuAcceptanceTest extends AcceptanceTest {

    @Test
    void 메뉴_목록을_조회할_수_있다() {
        // given
        long 두마리_메뉴_그룹 = 메뉴_그룹을_생성한다(두마리메뉴.getName());
        long 한마리_메뉴_그룹 = 메뉴_그룹을_생성한다(한마리메뉴.getName());
        long 후라이드_상품 = 상품을_생성한다(후라이드상품.getName(), 후라이드상품.getPrice());
        long 양념치킨_상품 = 상품을_생성한다(양념치킨상품.getName(), 양념치킨상품.getPrice());
        long 반반치킨_상품 = 상품을_생성한다(반반치킨상품.getName(), 반반치킨상품.getPrice());

        // and
        메뉴를_생성한다("후라이드", 32000, 두마리_메뉴_그룹, List.of(후라이드_상품), 2);
        메뉴를_생성한다("양념치킨", 32000, 두마리_메뉴_그룹, List.of(양념치킨_상품), 2);
        메뉴를_생성한다("반반치킨", 15000, 한마리_메뉴_그룹, List.of(반반치킨_상품), 1);

        // when
        List<MenuResponse> extract = 메뉴를_조회한다();

        // then
        assertThat(extract).hasSize(3);
    }
}
