package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    class 메뉴_그룹_생성 {

        @Test
        void 정상_요청() {
            // given
            final MenuGroupCreateRequest createRequest = new MenuGroupCreateRequest("한식");

            // when
            final MenuGroupResponse createResponse = menuGroupService.create(createRequest);

            // then
            assertThat(createResponse.getName()).isEqualTo(createRequest.getName());
        }
    }

    @Nested
    class 메뉴_그룹_전체_조회 {

        @Test
        void 정상_요청() {
            // given
            final MenuGroupCreateRequest createRequest = new MenuGroupCreateRequest("중식");
            final MenuGroupResponse createResponse = menuGroupService.create(createRequest);

            // when
            final List<MenuGroupResponse> findResponses = menuGroupService.readAll();

            // then
            assertThat(findResponses)
                    .extracting(MenuGroupResponse::getName)
                    .contains(createResponse.getName());
        }
    }
}
