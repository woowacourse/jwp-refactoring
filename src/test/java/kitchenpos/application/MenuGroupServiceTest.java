package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.supports.MenuGroupFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuGroupService menuGroupService;

    @Nested
    class 메뉴_그룹_생성 {

        @Test
        void 이름을_받아_메뉴_그룹을_생성한다() {
            // given
            String name = "안주류";

            MenuGroupCreateRequest request = new MenuGroupCreateRequest(name);
            given(menuGroupRepository.save(any(MenuGroup.class)))
                .willReturn(MenuGroupFixture.fixture().id(1L).name(name).build());

            // when
            MenuGroupResponse actual = menuGroupService.create(request);

            // then
            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new MenuGroupResponse(1L, name));
        }
    }

    @Nested
    class 메뉴_그룹_전체_조회 {

        @Test
        void 전체_메뉴_그룹을_조회한다() {
            // given
            List<MenuGroup> expected = List.of(
                MenuGroupFixture.fixture().id(1L).name("치킨").build(),
                MenuGroupFixture.fixture().id(2L).name("피자").build()
            );

            given(menuGroupRepository.findAll())
                .willReturn(expected);

            // when
            List<MenuGroupResponse> actual = menuGroupService.list();

            // then
            assertThat(actual)
                .map(MenuGroupResponse::getId)
                .containsExactly(1L, 2L);
        }
    }
}
