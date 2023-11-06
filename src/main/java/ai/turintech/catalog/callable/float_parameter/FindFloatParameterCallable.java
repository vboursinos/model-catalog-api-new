package ai.turintech.catalog.callable.float_parameter;

import ai.turintech.catalog.domain.FloatParameter;
import ai.turintech.catalog.repository.FloatParameterRepository;
import ai.turintech.catalog.service.dto.FloatParameterDTO;
import ai.turintech.catalog.service.mapper.FloatParameterMapper;
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
public class FindFloatParameterCallable implements Callable<FloatParameterDTO> {

    private UUID id;

    public FindFloatParameterCallable(UUID id) {
        this.id = id;
    }

    @Autowired
    private FloatParameterRepository floatParameterRepository;

    @Autowired
    private FloatParameterMapper floatParameterMapper;

    @Override
    public FloatParameterDTO call() throws Exception {
        Optional<FloatParameter> model = floatParameterRepository.findById(id);
        return model.map(floatParameterMapper::toDto).orElse(null);
    }
}