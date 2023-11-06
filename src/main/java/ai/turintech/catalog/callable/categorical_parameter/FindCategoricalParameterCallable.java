package ai.turintech.catalog.callable.categorical_parameter;

import ai.turintech.catalog.domain.CategoricalParameter;
import ai.turintech.catalog.repository.CategoricalParameterRepository;
import ai.turintech.catalog.service.dto.CategoricalParameterDTO;
import ai.turintech.catalog.service.mapper.CategoricalParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

@Transactional
@Component
@Scope("prototype")
public class FindCategoricalParameterCallable implements Callable<CategoricalParameterDTO> {

    private UUID id;

    // Constructor injection is the recommended way for mandatory dependencies
    public FindCategoricalParameterCallable(UUID id) {
        this.id = id;
    }

    @Autowired
    private CategoricalParameterRepository categoricalParameterRepository;

    @Autowired
    private CategoricalParameterMapper categoricalParameterMapper;

    @Override
    public CategoricalParameterDTO call() throws Exception {
        Optional<CategoricalParameter> model = categoricalParameterRepository.findById(id);
        return model.map(categoricalParameterMapper::toDto).orElse(null);
    }
}