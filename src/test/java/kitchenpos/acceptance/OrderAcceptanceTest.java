package kitchenpos.acceptance;

import static org.junit.jupiter.api.DynamicTest.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

@DisplayName("주문 인수 테스트")
class OrderAcceptanceTest extends AcceptanceTest {
    /*
     * Feature: 주문 관리
     *
     * Scenario: 주문을 관리한다.
     *
     * Given: 메뉴 그룹이 등록되어 있다.
     *        상품이 등록되어 있다.
     *        메뉴가 등록되어 있다.
     *        주문 테이블이 등록되어 있다.
     * When: 주문을 등록한다.
     * Then: 주문이 등록된다.
     *
     * When: 주문의 목록을 조회한다.
     * Then: 주문의 목록이 조회된다.
     *
     * Given: 주문이 등록되어 있다.
     * When: 주문의 상태를 변경한다.
     * Then: 주문의 상태가 변경된다.
     */
    @DisplayName("주문 관리")
    @TestFactory
    Stream<DynamicTest> manageOrder() {
        return Stream.of(
                dynamicTest(
                        "주문을 등록한다",
                        () -> {
                            // Given

                        }
                )
        );
    }
}
