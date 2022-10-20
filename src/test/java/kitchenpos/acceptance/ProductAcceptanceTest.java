package kitchenpos.acceptance;

import static kitchenpos.KitchenPosFixtures.까르보치킨_생성요청;
import static kitchenpos.KitchenPosFixtures.짜장치킨_생성요청;
import static kitchenpos.KitchenPosFixtures.프로덕트_URL;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ProductAcceptanceTest extends AcceptanceTest {
    @Test
    void 신규_프로덕트를_생성할_수_있다() {
        // given & when
        final var 프로덕트_생성응답 = 생성요청(프로덕트_URL, 까르보치킨_생성요청);
        final var 생성된_프로덕트 = 프로덕트_생성응답.body().as(Product.class);

        // then
        assertAll(
                응답일치(프로덕트_생성응답, HttpStatus.CREATED),
                단일_데이터_검증(생성된_프로덕트.getId(), 1L),
                단일_데이터_검증(생성된_프로덕트.getName(), 까르보치킨_생성요청.getName()),
                단일_데이터_검증(생성된_프로덕트.getPrice().doubleValue(), 까르보치킨_생성요청.getPrice().doubleValue())
        );
    }

    @Test
    void 전체_프로덕트를_조회할_수_있다() {
        // given
        생성요청(프로덕트_URL, 까르보치킨_생성요청);
        생성요청(프로덕트_URL, 짜장치킨_생성요청);

        // when
        final var 프로덕트_조회응답 = 조회요청(프로덕트_URL);
        final var 프로덕트_응답_데이터 = 프로덕트_조회응답.body().as(List.class);

        // then
        assertAll(
                응답일치(프로덕트_조회응답, HttpStatus.OK),
                리스트_데이터_검증(프로덕트_응답_데이터, "id", 1, 2),
                리스트_데이터_검증(프로덕트_응답_데이터, "name", 까르보치킨_생성요청.getName(), 짜장치킨_생성요청.getName()),
                리스트_데이터_검증(프로덕트_응답_데이터, "price", 까르보치킨_생성요청.getPrice().doubleValue(),
                        짜장치킨_생성요청.getPrice().doubleValue())
        );
    }
}
