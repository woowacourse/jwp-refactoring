package kitchenpos.acceptance;

import org.junit.jupiter.api.Test;

class TableGroupAcceptanceTest {

    /**
     * Feature: 테이블 그룹을 관리한다. Scenario: 테이블들을 그룹짓고, 그룹 헤제한다.
     *
     * Given empty=true 인 테이블들이 존재한다.
     *
     * When empty=true 인 테이블들을 그룹짓는다. Then 테이블들이 그룹지어진다.
     *
     * When 그룹지은 테이블을 그룹 헤제한다. Then 테이블 그룹이 해제된다.
     */
    @Test
    void manageTableGroup() {

    }

    /**
     * Feature: 테이블 하나짜리 그룹 생성을 시도한다.
     *
     * Given empty=true 인 테이블들이 존재한다.
     *
     * When 테이블 하나짜리 그룹 생성을 시도한다.
     * Then
     */
    @Test
    void createTableGroupWithOneTable() {

    }

    /**
     * Feature: 비어있지 않은 테이블들을 가지고 그룹 생성을 시도한다.
     *
     * Given 비어있는 테이블과 비어있지않은 테이블들이 존재한다.
     *
     * When 비어있지 않은 테이블이 포함된 테이블 그룹 생성을 시도한다.
     * Then
     */
    @Test
    void createTableGroupWithTablesContainingNotEmpty() {

    }

    /**
     * Feature: 이미 다른 그룹에 속해있는 테이블을 새로운 그룹에 포함시키려 한다.
     *
     * Given 이미 다른 그룹에 속한 테이블 A와 그렇지 않은 테이블들이 있다.
     *
     * When 테이블 A를 포함하여 그룹 생성하기를 시도한다.
     * Then
     */
    @Test
    void createTableGroupWithTableAlreadyInAnotherGroup() {

    }
}
