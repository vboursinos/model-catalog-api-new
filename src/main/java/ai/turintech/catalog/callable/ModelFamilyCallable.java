package ai.turintech.catalog.callable;

import ai.turintech.catalog.domain.ModelFamilyType;
import ai.turintech.catalog.repository.ModelFamilyTypeRepository;
import ai.turintech.catalog.service.dto.ModelFamilyTypeDTO;
import ai.turintech.catalog.service.mapper.ModelFamilyTypeMapper;
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
public class ModelFamilyCallable<T> implements Callable<T> {
    private String name;
    private UUID id;
    private ModelFamilyTypeDTO modelFamilyTypeDTO;

    public ModelFamilyCallable(String name) {
        this.name = name;
    }

    public ModelFamilyCallable(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public ModelFamilyCallable(String name, ModelFamilyTypeDTO modelFamilyTypeDTO) {
        this.name = name;
        this.modelFamilyTypeDTO = modelFamilyTypeDTO;
    }

    @Autowired
    private ModelFamilyTypeRepository modelFamilyTypeRepository;

    @Autowired
    private ModelFamilyTypeMapper modelFamilyTypeMapper;

    private List<ModelFamilyTypeDTO> findAll() {
        return modelFamilyTypeRepository.findAll().stream().map(modelFamilyTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public ModelFamilyTypeDTO findById() throws Exception {
        Optional<ModelFamilyType> modelFamilyType = modelFamilyTypeRepository.findById(id);
        if (!modelFamilyType.isPresent()) {
            throw new Exception("ML Task Type with ID " + id + " not found.");
        }
        return modelFamilyTypeMapper.toDto(modelFamilyType.get());
    }

    public ModelFamilyTypeDTO create() throws Exception {
        ModelFamilyType modelFamilyType = modelFamilyTypeMapper.toEntity(modelFamilyTypeDTO);
        modelFamilyType = modelFamilyTypeRepository.save(modelFamilyType);
        return modelFamilyTypeMapper.toDto(modelFamilyType);
    }

    public ModelFamilyTypeDTO update() throws Exception {
        ModelFamilyType modelFamilyType = modelFamilyTypeMapper.toEntity(modelFamilyTypeDTO);
        modelFamilyType = modelFamilyTypeRepository.save(modelFamilyType);
        return modelFamilyTypeMapper.toDto(modelFamilyType);
    }

    public ModelFamilyTypeDTO partialUpdate() throws Exception {
        return modelFamilyTypeRepository
                .findById(modelFamilyTypeDTO.getId())
                .map(existingMlTaskType -> {
                    modelFamilyTypeMapper.partialUpdate(existingMlTaskType, modelFamilyTypeDTO);

                    return existingMlTaskType;
                })
                .map(modelFamilyTypeRepository::save)
                .map(modelFamilyTypeMapper::toDto).get();
    }

    public void delete() throws Exception {
        modelFamilyTypeRepository.deleteById(id);
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