package kitchenpos.ordertable.domain;

public interface TableDomainService {

    OrderTable changeEmpty(final Long orderTableId, final boolean empty);
}
