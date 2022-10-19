package kitchenpos.acceptance;

import static kitchenpos.KitchenPosFixtures.삼인용_테이블;
import static kitchenpos.KitchenPosFixtures.오인용_테이블;
import static kitchenpos.KitchenPosFixtures.테이블_URL;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderTableAcceptanceTest extends AcceptanceTest {
    @Test
    void 신규_테이블을_생성할_수_있다() {
        // given & when
        final var 테이블_생성응답 = 생성요청(테이블_URL, 삼인용_테이블);
        final var 생성된_테이블 = 테이블_생성응답.body().as(OrderTable.class);

        // then
        assertAll(
                응답일치(테이블_생성응답, HttpStatus.CREATED),
                단일_데이터_검증(생성된_테이블.getId(), 1L),
                단일_데이터_검증(생성된_테이블.getTableGroupId(), null),
                단일_데이터_검증(생성된_테이블.getNumberOfGuests(), 삼인용_테이블.getNumberOfGuests()),
                단일_데이터_검증(생성된_테이블.isEmpty(), 삼인용_테이블.isEmpty())
        );
    }

    @Test
    void 전체_테이블을_조회할_수_있다() {
        // given
        생성요청(테이블_URL, 삼인용_테이블);
        생성요청(테이블_URL, 오인용_테이블);

        // when
        final var 테이블_조회응답 = 조회요청(테이블_URL);
        final var 테이블_응답_데이터 = 테이블_조회응답.body().as(List.class);
        final var 주문_가능_여부 = 테이블_응답_데이터.stream().map(table -> ((Map) table).get("empty"))
                .collect(Collectors.toList());

        // then
        assertAll(
                응답일치(테이블_조회응답, HttpStatus.OK),
                리스트_데이터_검증(테이블_응답_데이터, "id", 1, 2),
                리스트_데이터_검증(테이블_응답_데이터, "groupTableId", null, null),
                리스트_데이터_검증(테이블_응답_데이터, "numberOfGuests", 삼인용_테이블.getNumberOfGuests(), 오인용_테이블.getNumberOfGuests()),
                단일_데이터_검증(주문_가능_여부, List.of(삼인용_테이블.isEmpty(), 오인용_테이블.isEmpty()))
        );
    }

    @Test
    void 테이블의_주문_가능_여부를_변경할_수_있다() {
        // given
        final var 테이블 = 생성요청(테이블_URL, 삼인용_테이블).body().as(OrderTable.class);
        final var 주문가능_여부 = 테이블.isEmpty();
        final var 주문가능_여부_수정_URL = String.format(테이블_URL + "/%d/empty", 테이블.getId());

        // when
        final var 수정_응답 = 수정요청(주문가능_여부_수정_URL, Map.of("empty", !주문가능_여부));
        final var 수정된_테이블 = 수정_응답.body().as(OrderTable.class);
        final var 수정된_주문가능_여부 = 수정된_테이블.isEmpty();

        // then
        assertAll(
                응답일치(수정_응답, HttpStatus.OK),
                단일_데이터_검증(수정된_테이블.getId(), 테이블.getId()),
                단일_데이터_검증(수정된_테이블.getTableGroupId(), 테이블.getTableGroupId()),
                단일_데이터_검증(수정된_테이블.getNumberOfGuests(), 테이블.getNumberOfGuests()),
                단일_데이터_검증(수정된_주문가능_여부, !주문가능_여부)
        );
    }

    @Test
    void 주문_가능한_테이블의_고객_인원_수를_변경할_수_있다() {
        // given
        final var 주문가능_테이블 = 삼인용_테이블.changeEmpty(false);
        final var 테이블 = 생성요청(테이블_URL, 주문가능_테이블).body().as(OrderTable.class);
        final var 고객_인원수 = 테이블.getNumberOfGuests();
        final var 고객_인원수_수정_URL = String.format(테이블_URL + "/%d/number-of-guests", 테이블.getId());
        final var 기대_인원수 = 고객_인원수 + 1;

        // when
        final var 수정_응답 = 수정요청(고객_인원수_수정_URL, Map.of("numberOfGuests", 기대_인원수));
        final var 수정된_테이블 = 수정_응답.body().as(OrderTable.class);
        final var 수정된_고객_인원수 = 수정된_테이블.getNumberOfGuests();

        // then
        assertAll(
                응답일치(수정_응답, HttpStatus.OK),
                단일_데이터_검증(수정된_테이블.getId(), 테이블.getId()),
                단일_데이터_검증(수정된_테이블.getTableGroupId(), 테이블.getTableGroupId()),
                단일_데이터_검증(수정된_고객_인원수, 기대_인원수),
                단일_데이터_검증(수정된_테이블.isEmpty(), 테이블.isEmpty())
        );
    }
}
