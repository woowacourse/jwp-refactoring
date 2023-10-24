package kitchenpos.domain.mapper;

import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static kitchenpos.fixture.MenuFixture.REQUEST;
import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
public class MenuMapperTest {

    @MockBean
    private MenuProductMapper menuProductMapper;

    @MockBean
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuMapper menuMapper;

    @Nested
    class toMenu_메소드는_ {
        @Test
        void 메뉴_생성_요청을_메뉴로_변환한다() {
            // given
            CreateMenuRequest request = REQUEST.후라이드_치킨_16000원_1마리_등록_요청();
            given(menuGroupRepository.findById(anyLong()))
                    .willReturn(Optional.of(MENU_GROUP.메뉴_그룹_치킨()));

            // when
            Menu menu = menuMapper.toMenu(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(menu.getId()).isNull();
                softly.assertThat(menu.getName()).isEqualTo(request.getName());
                softly.assertThat(menu.getPrice()).isEqualTo(request.getPrice());
                softly.assertThat(menu.getMenuGroup()).isNotNull();
            });
        }


        @Test
        void 메뉴_그룹이_존재하지_않으면_예외() {
            // given
            CreateMenuRequest request = REQUEST.후라이드_치킨_16000원_1마리_등록_요청();
            given(menuGroupRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> menuMapper.toMenu(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

}
