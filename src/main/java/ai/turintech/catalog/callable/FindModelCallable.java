package ai.turintech.catalog.callable;

import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.mapper.ModelMapper;
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
public class FindModelCallable implements Callable<ModelDTO> {

    private UUID id;

    // Constructor injection is the recommended way for mandatory dependencies
    public FindModelCallable(UUID id) {
        this.id = id;
    }

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ModelDTO call() throws Exception {
        Optional<Model> model = modelRepository.findOneWithEagerRelationships(id);
        return model.map(modelMapper::toDto).orElse(null);
    }
}