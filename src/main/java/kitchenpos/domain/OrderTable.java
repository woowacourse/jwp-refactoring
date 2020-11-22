package kitchenpos.domain;

import kitchenpos.domain.service.ValidateOrderStatusEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Objects;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable changeEmpty(boolean empty, ApplicationEventPublisher publisher) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
        publisher.publishEvent(new ValidateOrderStatusEvent(this));
        this.empty = empty;
        return this;
    }

    public OrderTable changeNumOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (empty) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    public OrderTable groupBy(Long tableGroupId) {
        if (!empty || Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException();
        }
        this.tableGroupId = tableGroupId;
        this.empty = false;
        return this;
    }

    public OrderTable ungroup(ApplicationEventPublisher publisher) {
        publisher.publishEvent(new ValidateOrderStatusEvent(this));
        tableGroupId = null;
        empty = false;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
