package kitchenpos.acceptance;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class TableGroupAcceptanceTest extends AcceptanceTest {
    /*
     * Feature: 단체 지정 관리
     *
     * Scenario: 단체 지정을 관리한다.
     *
     * Given: 주문 테이블이 등록되어 있다.
     * When: 주문 테이블을 단체 지정한다.
     * Then: 주문 테이블이 단체 지정된다.
     *
     * Given: 주문 테이블이 등록되어 있다.
     *        주문 테이블이 단체 지정되어 있다.
     * When: 주문 테이블의 단체 지정을 해지한다.
     * Then: 주문 테이블의 단체 지정이 해지된다.
     */
    @DisplayName("단체 지정을 관리한다")
    @TestFactory
    Stream<DynamicTest> manageTableGroup() {
        return Stream.of(
    
        );
    }
}
