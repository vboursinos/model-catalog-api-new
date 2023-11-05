package ai.turintech.catalog.callable;

import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.mapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.Callable;

@Transactional
@Component
@Scope("prototype")
public class DeleteModelCallable implements Callable<String> {

    private UUID id;

    public DeleteModelCallable(UUID id) {
        this.id = id;
    }

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public String call() throws Exception {
        if (modelRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Model with id " + id + " does not exist");
        }
        modelRepository.deleteById(id);
        return "Model with id " + id + " deleted";
    }
}