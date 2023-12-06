package ai.turintech.modelcatalog.service;

import ai.turintech.components.architecture.service.impl.ReactiveAbstractCrudServiceImpl;
import ai.turintech.modelcatalog.dto.ModelFamilyTypeDTO;
import ai.turintech.modelcatalog.entity.ModelFamilyType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service Implementation for managing {@link ModelFamilyType}. */
@Service
@Transactional
public class ModelFamilyTypeServiceImpl
    extends ReactiveAbstractCrudServiceImpl<ModelFamilyTypeDTO, ModelFamilyType>
    implements ModelFamilyTypeService {}
