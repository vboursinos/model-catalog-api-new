package ai.turintech.catalog.callable.model;

import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.mapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.Callable;

@Transactional
@Component
@Scope("prototype")
public class PartialUpdateModelCallable implements Callable<ModelDTO> {
    private ModelDTO modelDTO;

    public PartialUpdateModelCallable(ModelDTO modelDTO) {
        this.modelDTO = modelDTO;
    }

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ModelDTO call() throws Exception {
        Optional<Model> result = modelRepository.findById(modelDTO.getId())
                .map(existingModel -> {
                    modelMapper.partialUpdate(existingModel, modelDTO);
                    return existingModel;
                })
                .map(modelRepository::save);

        if (result.isPresent()) {
            return result.map(modelMapper::toDto).get();
        } else {
            return null;
        }
    }
}