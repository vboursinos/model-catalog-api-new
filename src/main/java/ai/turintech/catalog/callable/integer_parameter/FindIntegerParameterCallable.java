package ai.turintech.catalog.callable.integer_parameter;

import ai.turintech.catalog.domain.IntegerParameter;
import ai.turintech.catalog.repository.IntegerParameterRepository;
import ai.turintech.catalog.service.dto.IntegerParameterDTO;
import ai.turintech.catalog.service.mapper.IntegerParameterMapper;
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
public class FindIntegerParameterCallable implements Callable<IntegerParameterDTO> {

    private UUID id;

    public FindIntegerParameterCallable(UUID id) {
        this.id = id;
    }

    @Autowired
    private IntegerParameterRepository integerParameterRepository;

    @Autowired
    private IntegerParameterMapper integerParameterMapper;

    @Override
    public IntegerParameterDTO call() throws Exception {
        Optional<IntegerParameter> model = integerParameterRepository.findById(id);
        return model.map(integerParameterMapper::toDto).orElse(null);
    }
}