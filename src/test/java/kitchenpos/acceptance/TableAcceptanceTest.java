package kitchenpos.acceptance;

import static org.junit.jupiter.api.DynamicTest.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class TableAcceptanceTest extends AcceptanceTest {
    /*
     * Feature: 주문 테이블 관리
     *
     * Scenario: 주문 테이블을 관리한다.
     *
     * When: 주문 테이블을 등록한다.
     * Then: 주문 테이블이 등록된다.
     *
     * Given: 주문 테이블이 등록되어 있다.
     * When: 주문 테이블의 목록을 조회한다.
     * Then: 저장되어 있는 주문 테이블의 목록이 반환된다.
     *
     * Given: 주문 테이블이 등록되어 있다.
     * When: 주문 테이블을 빈 테이블로 설정한다.
     * Then: 해당 주문 테이블이 빈 테이블로 설정된다.
     *
     * Given: 주문 테이블이 등록되어 있다.
     * When: 주문 테이블의 방문한 손님 수를 설정한다.
     * Then: 해당 주문 테이블의 방문한 손님 수가 설정된다.
     */
    @DisplayName("테이블을 관리한다")
    @TestFactory
    Stream<DynamicTest> manageTable() {
        return Stream.of(
                dynamicTest(
                        "",
                        () -> {

                        }
                )
        );
    }
}
