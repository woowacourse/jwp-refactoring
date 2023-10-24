package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.ui.dto.MenuGroupCreateRequest;
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
            var request = new MenuGroupCreateRequest("주류");

            // when
            var response = menuGroupService.create(request);

            // then
            assertThat(response.getName()).isEqualTo("주류");
        }
    }

    @Nested
    class findAll {

        @Test
        void 성공() {
            // given
            List<MenuGroup> expect = List.of(
                new MenuGroup(1L, "주류"),
                new MenuGroup(1L, "튀김류")
            );
            given(menuGroupRepository.findAll())
                .willReturn(expect);

            // when
            var response = menuGroupService.findAll();

            // then
            assertThat(response).hasSize(2);
        }
    }
}
