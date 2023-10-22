package kitchenpos.application;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.repository.MenuGroupRepository;
import kitchenpos.domain.menu.service.MenuGroupService;
import kitchenpos.domain.menu.service.dto.MenuGroupCreateRequest;
import kitchenpos.domain.menu.service.dto.MenuGroupResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Nested
    class Create {

        @Test
        void 메뉴_그룹을_생성할_수_있다() {
            // given
            final MenuGroup expected = spy(new MenuGroup("식사"));
            given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(expected);
            final long savedId = 1L;
            given(expected.getId()).willReturn(savedId);

            // when
            final MenuGroupResponse actual = menuGroupService.create(new MenuGroupCreateRequest(expected.getName()));

            // then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getName()).isEqualTo(expected.getName())
            );
        }
    }

    @Nested
    class FindAll {

        @Test
        void 메뉴_그룹을_전체_조회할_수_있다() {
            // when
            menuGroupRepository.findAll();

            // then
            verify(menuGroupRepository, only()).findAll();
        }
    }
}
