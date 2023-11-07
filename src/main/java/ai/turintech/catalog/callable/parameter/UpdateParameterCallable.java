package ai.turintech.catalog.callable.parameter;

import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.domain.Parameter;
import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.repository.ParameterRepository;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.dto.ParameterDTO;
import ai.turintech.catalog.service.mapper.ModelMapper;
import ai.turintech.catalog.service.mapper.ParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Callable;

@Transactional
@Component
@Scope("prototype")
public class UpdateParameterCallable implements Callable<ParameterDTO> {
    private ParameterDTO parameterDTO;

    public UpdateParameterCallable(ParameterDTO parameterDTO) {
        this.parameterDTO = parameterDTO;
    }

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterMapper parameterMapper;

    @Override
    public ParameterDTO call() throws Exception {
        Parameter parameter = parameterMapper.toEntity(parameterDTO);
        parameter = parameterRepository.save(parameter);
        return parameterMapper.toDto(parameter);
    }
}