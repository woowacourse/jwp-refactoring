package com.kitchenpos.exception;

public class TableGroupAlreadyRegisteredInGroup extends RuntimeException {

    public TableGroupAlreadyRegisteredInGroup() {
        super("테이블 그룹에 속해있어서 상태를 변경할 수 없습니다.");
    }
}
