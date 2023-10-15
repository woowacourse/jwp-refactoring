package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
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

    @InjectMocks
    MenuGroupService menuGroupService;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Nested
    class create {

        @Test
        void 성공() {
            // given
            MenuGroup expect = new MenuGroup();
            expect.setId(1L);
            given(menuGroupRepository.save(any(MenuGroup.class)))
                .willReturn(expect);

            // when
            MenuGroup actual = menuGroupService.create(new MenuGroup());

            // then
            assertThat(actual.getId()).isEqualTo(1L);
        }
    }

    @Nested
    class findAll {

        @Test
        void 성공() {
            // given
            given(menuGroupRepository.findAll())
                .willReturn(List.of(new MenuGroup(), new MenuGroup()));

            // when
            List<MenuGroup> actual = menuGroupService.findAll();

            // then
            assertThat(actual).hasSize(2);
        }
    }
}
