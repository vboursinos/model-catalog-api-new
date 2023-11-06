package ai.turintech.catalog.callable.parameter;

import ai.turintech.catalog.domain.Parameter;
import ai.turintech.catalog.repository.ParameterRepository;
import ai.turintech.catalog.service.dto.ParameterDTO;
import ai.turintech.catalog.service.mapper.ParameterMapper;
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
public class FindParameterCallable implements Callable<ParameterDTO> {

    private UUID id;

    public FindParameterCallable(UUID id) {
        this.id = id;
    }

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterMapper parameterMapper;

    @Override
    public ParameterDTO call() throws Exception {
        Optional<Parameter> model = parameterRepository.findById(id);
        return model.map(parameterMapper::toDto).orElse(null);
    }
}