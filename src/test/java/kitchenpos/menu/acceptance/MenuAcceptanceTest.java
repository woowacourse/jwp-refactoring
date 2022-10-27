package kitchenpos.menu.acceptance;

import static kitchenpos.support.DomainFixture.뿌링클;
import static kitchenpos.support.DomainFixture.세트_메뉴;
import static kitchenpos.support.DomainFixture.치즈볼;
import static kitchenpos.support.MenuGroupRestAssuredFixture.메뉴_그룹_생성_요청;
import static kitchenpos.support.MenuRestAssuredFixture.메뉴_목록_조회_요청;
import static kitchenpos.support.MenuRestAssuredFixture.메뉴_생성_요청;
import static kitchenpos.support.ProductRestAssuredFixture.상품_생성_요청;
import static kitchenpos.support.SimpleRestAssured.toObjectList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.menu.ui.dto.MenuCreateRequest;
import kitchenpos.menu.ui.dto.MenuGroupResponse;
import kitchenpos.menu.ui.dto.MenuProductCreateRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
import kitchenpos.menu.ui.dto.ProductResponse;
import kitchenpos.support.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuAcceptanceTest extends AcceptanceTest {

    private Long productAId;
    private Long productBId;
    private Long menuGroupId;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        productAId = 상품_생성_요청(뿌링클).as(ProductResponse.class).getId();
        productBId = 상품_생성_요청(치즈볼).as(ProductResponse.class).getId();
        menuGroupId = 메뉴_그룹_생성_요청(세트_메뉴).as(MenuGroupResponse.class).getId();
    }

    @Test
    void 메뉴_생성을_요청한다() {
        // given
        final var request = 뿌링클_치즈볼_메뉴_요청_정보_생성(menuGroupId, productAId, productBId);

        // when
        final var response = 메뉴_생성_요청(request);

        // then
        final var created = response.as(MenuResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location").split("/menus/")[1]).isNotNull(),
                () -> assertThat(created.getId()).isNotNull(),
                () -> assertThat(created.getName()).isEqualTo(request.getName()),
                () -> assertThat(created.getPrice().intValue()).isEqualTo(request.getPrice()),
                () -> assertThat(created.getMenuGroupId()).isEqualTo(request.getMenuGroupId()),
                () -> assertThat(created.getMenuProducts()).hasSize(2)
        );
    }

    @Test
    void 메뉴_목록_조회를_요청한다() {
        // given
        final var request = 뿌링클_치즈볼_메뉴_요청_정보_생성(menuGroupId, productAId, productBId);
        메뉴_생성_요청(request);
        메뉴_생성_요청(request);

        // when
        final var response = 메뉴_목록_조회_요청();

        // then
        final var found = toObjectList(response, MenuResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(found).hasSize(2)
        );
    }

    private MenuCreateRequest 뿌링클_치즈볼_메뉴_요청_정보_생성(final long menuGroupId, final Long... productIds) {
        return new MenuCreateRequest("뿌링클+치즈볼", 23_000, menuGroupId,
                Arrays.stream(productIds)
                        .map(product -> new MenuProductCreateRequest(product, 1))
                        .collect(Collectors.toList()));
    }
}
