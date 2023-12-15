package ai.turintech.modelcatalog.facade;

import ai.turintech.components.architecture.facade.impl.reactive.ReactiveAbstractUUIDIdentityCrudFacadeImpl;
import ai.turintech.modelcatalog.dto.ParameterTypeDefinitionDTO;
import ai.turintech.modelcatalog.entity.ParameterTypeDefinition;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Service Implementation for managing {@link ParameterTypeDefinition}. */
@Component
@Transactional
public class ParameterTypeDefinitionFacadeImpl
    extends ReactiveAbstractUUIDIdentityCrudFacadeImpl<
        ParameterTypeDefinitionDTO, ParameterTypeDefinition, UUID>
    implements ParameterTypeDefinitionFacade {}