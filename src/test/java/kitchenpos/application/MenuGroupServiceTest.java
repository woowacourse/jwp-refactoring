package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupCreateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        // given
        final MenuGroupCreateRequest menuGroupCreateRequest = 메뉴_그룹_추천상품_요청();

        // when
        MenuGroupCreateResponse actual = menuGroupService.create(menuGroupCreateRequest);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(menuGroupCreateRequest.getName())
        );
    }

    @Test
    void 메뉴_그룹_목록을_조회한다() {
        // given
        final MenuGroupCreateRequest menuGroupCreateRequest = 메뉴_그룹_추천상품_요청();

        menuGroupService.create(menuGroupCreateRequest);

        // when
        List<MenuGroupCreateResponse> actual = menuGroupService.list();

        // TODO: 2022/10/27 내부 데이터까지 확인
        // then
        assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
    }

    public MenuGroupCreateRequest 메뉴_그룹_추천상품_요청() {
        return new MenuGroupCreateRequest("추천상품");
    }
}
