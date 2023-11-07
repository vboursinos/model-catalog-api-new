package ai.turintech.catalog.callable;

import ai.turintech.catalog.domain.ModelType;
import ai.turintech.catalog.repository.ModelTypeRepository;
import ai.turintech.catalog.service.dto.ModelTypeDTO;
import ai.turintech.catalog.service.mapper.ModelTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Transactional
@Component
@Scope("prototype")
public class ModelTypeCallable<T> implements Callable<T> {
    private String name;
    private UUID id;
    private ModelTypeDTO modelTypeDTO;

    public ModelTypeCallable(String name) {
        this.name = name;
    }

    public ModelTypeCallable(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public ModelTypeCallable(String name, ModelTypeDTO modelTypeDTO) {
        this.name = name;
        this.modelTypeDTO = modelTypeDTO;
    }

    @Autowired
    private ModelTypeRepository modelTypeRepository;

    @Autowired
    private ModelTypeMapper modelTypeMapper;

    private List<ModelTypeDTO> findAll() {
        return modelTypeRepository.findAll().stream().map(modelTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public ModelTypeDTO findById() throws Exception {
        Optional<ModelType> modelTypeOptional = modelTypeRepository.findById(id);
        if (!modelTypeOptional.isPresent()) {
            throw new Exception("ML Task Type with ID " + id + " not found.");
        }
        return modelTypeMapper.toDto(modelTypeOptional.get());
    }

    public ModelTypeDTO create() throws Exception {
        ModelType modelType = modelTypeMapper.toEntity(modelTypeDTO);
        modelType = modelTypeRepository.save(modelType);
        return modelTypeMapper.toDto(modelType);
    }

    public ModelTypeDTO update() throws Exception {
        ModelType modelType = modelTypeMapper.toEntity(modelTypeDTO);
        modelType = modelTypeRepository.save(modelType);
        return modelTypeMapper.toDto(modelType);
    }

    public ModelTypeDTO partialUpdate() throws Exception {
        return modelTypeRepository
                .findById(modelTypeDTO.getId())
                .map(existingMlTaskType -> {
                    modelTypeMapper.partialUpdate(existingMlTaskType, modelTypeDTO);

                    return existingMlTaskType;
                })
                .map(modelTypeRepository::save)
                .map(modelTypeMapper::toDto).get();
    }

    public void delete() throws Exception {
        modelTypeRepository.deleteById(id);
    }

    @Override
    public T call() throws Exception {
        if (name.equalsIgnoreCase("create")) {
            return (T) create();
        } else if (name.equalsIgnoreCase("findAll")) {
            return (T) findAll();
        } else if (name.equalsIgnoreCase("findById")) {
            return (T) findById();
        } else if (name.equalsIgnoreCase("update")) {
            return (T) update();
        } else if (name.equalsIgnoreCase("partialUpdate")) {
            return (T) partialUpdate();
        } else if (name.equalsIgnoreCase("delete")) {
            delete();
        }
        return null;
    }
}