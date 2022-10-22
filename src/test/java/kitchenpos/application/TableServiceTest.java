package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/***
 * 매장에서 주문을 받는 내용에 대한 내용을 담은 도메인 입니다. `테이블 그룹`과, `손님 수`, `비어있는지` 확인합니다.
 * <br>
 * - `create`(오프라인 주문을 생성합니다.)
 *    <br>
 *      - 테이블 초기화 시도시 존재하지않는 테이블이면 예외를 반환합니다.
 *    <br>
 *   - 테이블 그룹이 *null*이 아니면 예외를 반환합니다.
 *    <br>
 * - `findAll`(전체 매장 주문에 대한 내용을 반환합니다.)
 *  <br>
 * - `ChangeEmpty`(식사 종료 후 테이블을 정리합니다.)
 *  <br>
 *   - 테이블에서 주문한 내역이 없으면 예외를 반환합니다.
 *    <br>
 *   - `테이블 그룹이 없으면` 예외를 반환합니다.
 *    <br>
 *   - 주문 상태가 `요리중이거나 식사중이 아니면` 예외를 반환합니다.
 *    <br>
 * - `changeNumberOfGuests`(오프라인에서 식사하는 손님의 인원 수를 변경합니다.)
 *  <br>
 *   - `인원 수가 0명 이상`이어야합니다.
 *    <br>
 *   - 주문 내역이 없으면 예외를 반환합니다.
 */
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    void 테이블_초기화_시도시_존재하지않는_테이블이면_예외를_반환한다() {
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, 테이블_생성(1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블을 찾을 수 없습니다.");
    }

    @Test
    void 테이블_초기화_시도시_테이블_그룹이_null_이_아니면_예외를_반환한다() {
        존재하는_테이블_세팅();
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, 테이블_생성(1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("진행중인 테이블 그룹이 존재합니다.");
    }

    @Test
    void 조리중일때_테이블을_초기화하면_예외를_반환한다() {
        테이블_그룹이_없는_테이블_세팅(1L);
        테이블_내_주문_상태를_진행중으로_설정();

        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, 테이블_생성(1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아직 그룹을 해지할 수 없습니다.");
    }

    @Test
    void 게스트_숫자가_음수일_경우_예외를_반환한다() {
        final OrderTable 음수_테이블 = 게스트_숫자_음수인_테이블_생성();

        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 음수_테이블))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게스트 숫자는 음수일 수 없습니다.");
    }

    @Test
    void 테이블_게스트_숫자_변경시_빈테이블이면_예외를_반환한다() {
        존재하지않는_테이블_세팅();

        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 테이블_생성(1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 존재하지 않습니다.");
    }
}
