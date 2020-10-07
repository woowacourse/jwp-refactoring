package kitchenpos.acceptance;

import org.junit.jupiter.api.Test;

class TableAcceptanceTest {

    /**
     * Feature: 테이블을 관리한다.
     *
     * Scenario: 테이블들을 준비한다.
     *           손님을 받은 테이블을 주문 가능 상태로 바꾸고, 해당 테이블의 손님 수를 입력한다.
     *           손님이 떠난 테이블을 주문 불가능 상태(empty)로 바꾸고,
     *           해당 테이블의 손님 수를 0명으로 한다.
     *
     * When 테이블들을 생성한다. Then 테이블들이 생성된다.
     *
     * Given 테이블들이 생성되어있다.
     * When 어떤 테이블의 empty 여부를 true 에서 false 로 바꾼다.
     *      (주문 불가능한 테이블을 주문 가능한 상태로 바꾼다.)
     * Then 테이블의 empty 여부가 false 로 바뀐다.
     *
     * Given 테이블들이 생성되어있고, empty 여부가 false 인 테이블이 있다.
     * When empty=false 인 테이블 의 손님 수를 0 보다 큰 수로 바꾼다.
     *
     * When empty=true 인 테이블의 손님 수를 0 보다 큰 수로 바꿀 수 없다.
     *
     * When 어떤 테이블의 empty 여부를 true 에서 false 로 바꾼다.
     *      (주문 가능한 상태의 테이블을 주문 불가능한 상태로 바꾼다.)
     * Then 테이블의 empty 여부가 false 에서 true 로 바뀐다.
     */
    @Test
    void manageTable() {

    }

    /**
     * Feature: 테이블 생성에 있어서 사용자가 실수를 한다.
     *
     * When 테이블 생성 요청을 하는데, 테이블 id 와 테이블 그룹 id를 지정해서 요청한다.
     * Then 테이블 id는 사용자 요청을 무시하고 자동으로 정해지고,
     *      테이블 그룹 id 도 사용자 요청을 무시하는데 항상 null 로만 생성된다.
     */
    @Test
    void createTable_ExceptionalCase() {

    }

    /**
     * Feature: 그룹으로 묶여있는 테이블의 empty 상태 전환을 시도한다.
     *
     * Given 테이블 두 개가 empty=false 이고, 하나의 테이블 그룹으로 묶여있다.
     * When 둘 중 하나 이상의 테이블에 대하여 empty 상태 전환을 요청한다.
     * Then response 가 오지 않는다.    // Todo: 나중에 401같은거 오도록 바꿔야할듯!!
     */
    @Test
    void changeEmptyOfGroupedTable() {

    }

    /**
     * Feature: 주문이 들어가있는 테이블의 empty 상태 전환을 시도한다.
     *
     * Given 테이블 두 개가 empty=false 이고, 하나의 테이블 그룹으로 묶여있다.
     * When 둘 중 하나 이상의 테이블에 대하여 empty 상태 전환을 요청한다.
     * Then response 가 오지 않는다.    // Todo: 나중에 401같은거 오도록 바꿔야할듯!!
     */
    @Test
    void changeEmpty_ExceptionalCase() {

    }
}
