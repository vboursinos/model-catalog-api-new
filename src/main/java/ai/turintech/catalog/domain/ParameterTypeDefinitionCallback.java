package ai.turintech.catalog.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ParameterTypeDefinitionCallback
    implements AfterSaveCallback<ParameterTypeDefinition>, AfterConvertCallback<ParameterTypeDefinition> {

    @Override
    public Publisher<ParameterTypeDefinition> onAfterConvert(ParameterTypeDefinition entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<ParameterTypeDefinition> onAfterSave(ParameterTypeDefinition entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
