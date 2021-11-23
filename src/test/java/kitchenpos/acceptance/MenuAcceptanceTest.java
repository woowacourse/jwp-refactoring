package kitchenpos.acceptance;

import kitchenpos.AcceptanceTest;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.request.MenuRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import kitchenpos.ui.dto.response.MenuResponse;
import kitchenpos.ui.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceTest extends AcceptanceTest {

    private List<MenuProductRequest> 메뉴_상품_요청_리스트;
    private MenuGroupResponse 메뉴그룹1_응답;

    @BeforeEach
    void setup() {
        메뉴그룹1_응답 = 메뉴그룹_등록(menuGroupFixture.메뉴그룹_생성_요청("메뉴그룹1"));
        ProductResponse 상품1_응답 = 상품_등록(productFixture.상품_생성_요청("상품1", BigDecimal.valueOf(1000)));
        ProductResponse 상품2_응답 = 상품_등록(productFixture.상품_생성_요청("상품2", BigDecimal.valueOf(2000)));
        MenuProductRequest 메뉴_상품1_요청 = menuProductFixture.메뉴_상품_생성_요청(상품1_응답.getId(), 1L);
        MenuProductRequest 메뉴_상품2_요청 = menuProductFixture.메뉴_상품_생성_요청(상품2_응답.getId(), 1L);
        메뉴_상품_요청_리스트 = menuProductFixture.메뉴_상품_요청_리스트_생성(메뉴_상품1_요청, 메뉴_상품2_요청);
    }

    @Test
    @DisplayName("메뉴 생성 테스트 - 성공")
    public void createTest() throws Exception {
        // given
        MenuRequest 메뉴1_요청 = menuFixture.메뉴_생성_요청("메뉴1", BigDecimal.valueOf(1000), 메뉴그룹1_응답.getId(), 메뉴_상품_요청_리스트);

        // when
        MenuResponse 메뉴1_응답 = 메뉴_등록(메뉴1_요청);

        // then
        assertThat(메뉴1_응답).usingRecursiveComparison()
                .isEqualTo(MenuResponse.create(menuFixture.메뉴_조회(메뉴1_응답.getId())));
    }

    @Test
    @DisplayName("모든 메뉴 조회 테스트 - 성공")
    public void listTest() throws Exception {
        // given
        MenuRequest 메뉴1_생성_요청 = menuFixture.메뉴_생성_요청("메뉴1", BigDecimal.valueOf(1000), 메뉴그룹1_응답.getId(), 메뉴_상품_요청_리스트);
        MenuRequest 메뉴2_생성_요청 = menuFixture.메뉴_생성_요청("메뉴2", BigDecimal.valueOf(2000), 메뉴그룹1_응답.getId(), 메뉴_상품_요청_리스트);

        List<MenuResponse> expected = menuFixture.메뉴_응답_리스트_생성(메뉴_등록(메뉴1_생성_요청), 메뉴_등록(메뉴2_생성_요청));

        // when
        List<MenuResponse> actual = 메뉴_리스트_조회();

        // then
        assertThat(actual).hasSize(expected.size());
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
