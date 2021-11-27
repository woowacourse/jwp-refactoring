package kitchenpos.application;

import org.springframework.stereotype.Component;

@Component
public interface TableUngroupDomainService {

    void ungroup(final Long tableGroupId);
}



