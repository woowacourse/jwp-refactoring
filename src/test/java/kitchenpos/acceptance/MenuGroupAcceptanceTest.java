package kitchenpos.acceptance;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.ui.dto.request.MenuGroupRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    private final MenuGroupFixture menuGroupFixture = new MenuGroupFixture();

    @Test
    @DisplayName("메뉴그룹 생성 테스트 - 성공")
    public void createTest() throws Exception {
        // given
        MenuGroupRequest 메뉴그룹1_생성_요청 = menuGroupFixture.메뉴그룹_생성_요청("메뉴그룹1");
        MenuGroup expected = menuGroupFixture.메뉴그룹_생성("메뉴그룹1");

        // when
        MenuGroupResponse response = 메뉴그룹_등록(메뉴그룹1_생성_요청);

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("모든 메뉴그룹 조회 테스트 - 성공")
    public void listTest() throws Exception {
        // given
        MenuGroupRequest 메뉴그룹1_생성_요청 = menuGroupFixture.메뉴그룹_생성_요청("메뉴그룹1");
        MenuGroupRequest 메뉴그룹2_생성_요청 = menuGroupFixture.메뉴그룹_생성_요청("메뉴그룹2");


        List<MenuGroupResponse> expected = menuGroupFixture.메뉴그룹_응답_리스트_생성(메뉴그룹_등록(메뉴그룹1_생성_요청), 메뉴그룹_등록(메뉴그룹2_생성_요청));

        // when
        List<MenuGroupResponse> actual = 메뉴그룹_리스트_조회();

        // then
        assertThat(actual).hasSize(expected.size());
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    public List<MenuGroupResponse> 메뉴그룹_리스트_조회(){
        return request()
                .get("/api/menu-groups")
                .build()
                .convertBodyToList(MenuGroupResponse.class);
    }
}
