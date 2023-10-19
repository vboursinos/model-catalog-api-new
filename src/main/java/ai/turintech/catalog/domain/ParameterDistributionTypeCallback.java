package ai.turintech.catalog.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ParameterDistributionTypeCallback
    implements AfterSaveCallback<ParameterDistributionType>, AfterConvertCallback<ParameterDistributionType> {

    @Override
    public Publisher<ParameterDistributionType> onAfterConvert(ParameterDistributionType entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<ParameterDistributionType> onAfterSave(
        ParameterDistributionType entity,
        OutboundRow outboundRow,
        SqlIdentifier table
    ) {
        return Mono.just(entity.setIsPersisted());
    }
}
