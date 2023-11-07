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

import java.util.Optional;
import java.util.concurrent.Callable;

@Transactional
@Component
@Scope("prototype")
public class PartialUpdateParameterCallable implements Callable<Optional<ParameterDTO>> {
    private ParameterDTO parameterDTO;

    public PartialUpdateParameterCallable(ParameterDTO parameterDTO) {
        this.parameterDTO = parameterDTO;
    }

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterMapper parameterMapper;

    @Override
    public Optional<ParameterDTO> call() throws Exception {
        Optional<Parameter> result = parameterRepository.findById(parameterDTO.getId())
                .map(existingModel -> {
                    parameterMapper.partialUpdate(existingModel, parameterDTO);
                    return existingModel;
                })
                .map(parameterRepository::save);

        if (result.isPresent()) {
            return result.map(parameterMapper::toDto);
        } else {
            return null;
        }
    }
}