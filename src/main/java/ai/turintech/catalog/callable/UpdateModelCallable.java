package ai.turintech.catalog.callable;

import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.mapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Callable;

@Transactional
@Component
@Scope("prototype")
public class UpdateModelCallable implements Callable<ModelDTO> {
    private ModelDTO modelDTO;

    public UpdateModelCallable(ModelDTO modelDTO) {
        this.modelDTO = modelDTO;
    }

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ModelDTO call() throws Exception {
        Model model = modelMapper.toEntity(modelDTO);
        model = modelRepository.save(model);
        return modelMapper.toDto(model);
    }
}