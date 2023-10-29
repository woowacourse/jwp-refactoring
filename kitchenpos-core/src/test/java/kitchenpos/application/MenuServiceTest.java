package kitchenpos.application;

import kitchenpos.application.fixture.MenuServiceFixture;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.event.CreateMenuProductEvent;
import kitchenpos.menu.domain.Menu;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
@RecordApplicationEvents
class MenuServiceTest extends MenuServiceFixture {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ApplicationEvents events;

    @Nested
    class 메뉴_생성 {

        @Test
        void 메뉴를_생성한다() {
            메뉴를_생성한다_픽스처_생성();

            final Menu actual = menuService.create(메뉴_생성_요청_dto);

            assertThat(actual.getId()).isPositive();
        }

        @Test
        void 메뉴를_생성하면_메뉴_상품_생성_이벤트가_호출된다() {
            메뉴를_생성한다_픽스처_생성();

            menuService.create(메뉴_생성_요청_dto);
            final long actual = events.stream(CreateMenuProductEvent.class).count();

            assertThat(actual).isEqualTo(1);
        }

        @Test
        void 전달_받은_메뉴의_가격이_입력되지_않았다면_예외가_발생한다() {
            전달_받은_메뉴의_가격이_입력되지_않았다면_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> menuService.create(가격이_입력되지_않은_메뉴_생성_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 전달_받은_메뉴의_가격이_0보다_작은_경우_예외가_발생한다() {
            전달_받은_메뉴의_가격이_0보다_작은_경우_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> menuService.create(가격이_0보다_작은_메뉴_생성_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 유효하지_않은_메뉴_그룹_아이디를_전달_받으면_예외가_발생한다() {
            유효하지_않은_메뉴_그룹_아이디를_전달_받으면_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> menuService.create(유효하지_않은_메뉴_그룹_아이디를_갖는_메뉴_생성_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 유효하지_않은_메뉴_상품_아이디를_전달_받으면_예외가_발생한다() {
            유효하지_않은_메뉴_상품_아이디를_전달_받으면_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> menuService.create(유효하지_않은_메뉴_상품_아이디를_갖는_메뉴_생성_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_메뉴에_포함된_상품_가격을_합친_것보다_작은_경우_예외가_발생한다() {
            메뉴의_가격이_메뉴에_포함된_상품_가격을_합친_것보다_작은_경우_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> menuService.create(유효하지_않은_가격을_갖는_메뉴_생성_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_조회 {

        @Test
        void 모든_메뉴를_조회한다() {
            모든_메뉴를_조회한다_픽스처_생성();

            final List<Menu> actual = menuService.list();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(2);
                softAssertions.assertThat(actual.get(0).getName()).isEqualTo("저장된 메뉴 1");
                softAssertions.assertThat(actual.get(1).getName()).isEqualTo("저장된 메뉴 2");
            });
        }
    }
}
