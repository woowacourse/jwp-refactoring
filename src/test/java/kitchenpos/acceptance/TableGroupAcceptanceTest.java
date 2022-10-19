package kitchenpos.acceptance;

import static kitchenpos.KitchenPosFixtures.삼인용_테이블;
import static kitchenpos.KitchenPosFixtures.오인용_테이블;
import static kitchenpos.KitchenPosFixtures.테이블_URL;
import static kitchenpos.KitchenPosFixtures.테이블그룹_URL;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Map;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTable 테이블A;
    private OrderTable 테이블B;

    @BeforeEach
    void setupTables() {
        테이블A = 생성요청(테이블_URL, 삼인용_테이블.changeEmpty(true)).body().as(OrderTable.class);
        테이블B = 생성요청(테이블_URL, 오인용_테이블.changeEmpty(true)).body().as(OrderTable.class);
    }

    @Test
    void 둘_이상의_테이블을_단체로_지정할_수_있다() {
        // given
        final var 테이블그룹_생성요청 =
                Map.of("orderTables", List.of(
                        Map.of("id", 테이블A.getId()),
                        Map.of("id", 테이블B.getId()))
                );

        // when
        final var 테이블그룹_생성응답 = 생성요청(테이블그룹_URL, 테이블그룹_생성요청);
        final var 생성된_테이블그룹 = 테이블그룹_생성응답.body().as(TableGroup.class);
        final var 그룹소속_테이블들 = 생성된_테이블그룹.getOrderTables();

        // then
        assertAll(
                응답일치(테이블그룹_생성응답, HttpStatus.CREATED),
                단일_데이터_검증(생성된_테이블그룹.getId(), 1L),
                리스트_데이터_검증(그룹소속_테이블들, "id", 테이블A.getId(), 테이블B.getId()),
                리스트_데이터_검증(그룹소속_테이블들, "tableGroupId", 생성된_테이블그룹.getId(), 생성된_테이블그룹.getId()),
                리스트_데이터_검증(그룹소속_테이블들, "numberOfGuests", 테이블A.getNumberOfGuests(), 테이블B.getNumberOfGuests()),
                리스트_데이터_검증(그룹소속_테이블들, "empty", false, false)
        );
    }

    @Test
    void 단체로_지정된_테이블들을_그룹_해제할_수_있다() {
        // given
        final var 테이블그룹_생성요청 =
                Map.of("orderTables", List.of(
                        Map.of("id", 테이블A.getId()),
                        Map.of("id", 테이블B.getId()))
                );

        // when
        final var 테이블그룹_생성응답 = 생성요청(테이블그룹_URL, 테이블그룹_생성요청);
        final var 테이블그룹_생성_데이터 = 테이블그룹_생성응답.body().as(TableGroup.class);
        final var 그룹_내_테이블 = 테이블그룹_생성_데이터.getOrderTables();
        final var 그룹아이디 = 테이블그룹_생성_데이터.getId();

        final var 테이블그룹_삭제_URL = String.format(테이블그룹_URL + "/%d", 그룹아이디);
        final var 테이블그룹_삭제응답 = 삭제요청(테이블그룹_삭제_URL);

        final var 테이블_조회응답 = 조회요청(테이블_URL);
        final var 테이블_조회_데이터 = 테이블_조회응답.body().as(List.class);

        // then
        assertAll(
                응답일치(테이블그룹_생성응답, HttpStatus.CREATED),
                응답일치(테이블그룹_삭제응답, HttpStatus.NO_CONTENT),
                응답일치(테이블_조회응답, HttpStatus.OK),
                단일_데이터_검증(그룹아이디, 1L),
                리스트_데이터_검증(그룹_내_테이블, "tableGroupId", 그룹아이디, 그룹아이디),
                리스트_데이터_검증(테이블_조회_데이터, "tableGroupId", null, null)
        );
    }
}
