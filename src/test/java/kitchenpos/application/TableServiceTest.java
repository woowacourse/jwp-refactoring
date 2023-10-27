package kitchenpos.application;

import kitchenpos.application.fixture.TableServiceFixture;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.CreateOrderTableRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
class TableServiceTest extends TableServiceFixture {

    @Autowired
    private TableService tableService;

    @Nested
    class 테이블_등록 {

        @Test
        void 테이블을_등록할_수_있다() {
            final CreateOrderTableRequest 주문_테이블_생성_요청_dto = new CreateOrderTableRequest(3, false);

            final OrderTable actual = tableService.create(주문_테이블_생성_요청_dto);

            assertThat(actual.getId()).isPositive();
        }
    }

    @Nested
    class 테이블_조회 {

        @Test
        void 모든_테이블을_조회한다() {
            모든_테이블을_조회한다_픽스처_생성();

            final List<OrderTable> actual = tableService.list();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(2);
                softAssertions.assertThat(actual.get(0)).isEqualTo(조회용_주문_테이블_1);
                softAssertions.assertThat(actual.get(1)).isEqualTo(조회용_주문_테이블_2);
            });
        }
    }

    @Nested
    class 테이블_상태_변경 {

        @Test
        void 사용_불가능한_테이블_상태를_사용_가능한_상태로_바꾼다() {
            사용_불가능한_테이블_상태를_사용_가능한_상태로_바꾼다_픽스처_생성();

            final OrderTable actual = tableService.changeEmpty(사용_불가능한_상태의_테이블.getId(), 상태_변경_요청_dto);

            assertThat(actual.isEmpty()).isFalse();
        }

        @Test
        void 유효하지_않은_테이블_아이디를_전달_받은_경우_예외가_발생한다() {
            유효하지_않은_테이블_아이디를_전달_받은_경우_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> tableService.changeEmpty(유효하지_않은_주문_테이블의_테이블아이디, 유효하지_않은_테이블아이디의_주문_테이블_상태_변경_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_아이디가_그룹_테이블에_포함되어_있다면_예외가_발생한다() {
            주문_테이블_아이디가_그룹_테이블에_포함되어_있다면_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> tableService.changeEmpty(그룹테이블에_포함된_주문_테이블_1.getId(), 상태_변경_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_아이디에_해당하는_테이블의_주문_상태가_COMPLETION이_아닌_경우_예외가_발생한다() {
            주문_테이블_아이디에_해당하는_테이블의_주문_상태가_COMPLETION이_아닌_경우_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> tableService.changeEmpty(주문_상태가_COMPLETION인_주문_테이블.getId(), 상태_변경_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 손님_수_변경 {

        @Test
        void 손님_수를_변경한다() {
            손님_수를_변경한다_픽스처_생성();

            final OrderTable actual = tableService.changeNumberOfGuests(손님이_한_명인_테이블.getId(), 손님수_변경_요청_dto);

            assertThat(actual.getNumberOfGuests()).isEqualTo(10);
        }

        @Test
        void 입력_받은_손님_수가_0보다_작으면_예외가_발생한다() {
            입력_받은_손님_수가_0보다_작으면_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(손님이_한_명인_테이블.getId(), 손님수가_음수인_손님수_변경_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 유효하지_않은_주문_테이블_아이디를_전달_받은_경우_예외가_발생한다() {
            유효하지_않은_주문_테이블_아이디를_전달_받은_경우_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(유효하지_않은_주문_테이블_아이디, 손님수_변경_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
