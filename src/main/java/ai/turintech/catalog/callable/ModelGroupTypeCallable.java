package ai.turintech.catalog.callable;

import ai.turintech.catalog.domain.ModelGroupType;
import ai.turintech.catalog.repository.ModelGroupTypeRepository;
import ai.turintech.catalog.service.dto.ModelGroupTypeDTO;
import ai.turintech.catalog.service.mapper.ModelGroupTypeMapper;
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
public class ModelGroupTypeCallable<T> implements Callable<T> {
    private String name;
    private UUID id;
    private ModelGroupTypeDTO modelGroupTypeDTO;

    public ModelGroupTypeCallable(String name) {
        this.name = name;
    }

    public ModelGroupTypeCallable(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public ModelGroupTypeCallable(String name, ModelGroupTypeDTO modelGroupTypeDTO) {
        this.name = name;
        this.modelGroupTypeDTO = modelGroupTypeDTO;
    }

    @Autowired
    private ModelGroupTypeRepository modelGroupTypeRepository;

    @Autowired
    private ModelGroupTypeMapper modelGroupTypeMapper;

    private List<ModelGroupTypeDTO> findAll(){
       return modelGroupTypeRepository.findAll().stream().map(modelGroupTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public ModelGroupTypeDTO findById() throws Exception {
        Optional<ModelGroupType> modelGroupTypeOptional = modelGroupTypeRepository.findById(id);
        if (!modelGroupTypeOptional.isPresent()) {
            throw new Exception("Model Group Type with ID " + id + " not found.");
        }
        return modelGroupTypeMapper.toDto(modelGroupTypeOptional.get());
    }

    public ModelGroupTypeDTO create() throws Exception {
        ModelGroupType modelGroupType = modelGroupTypeMapper.toEntity(modelGroupTypeDTO);
        modelGroupType = modelGroupTypeRepository.save(modelGroupType);
        return modelGroupTypeMapper.toDto(modelGroupType);
    }

    public ModelGroupTypeDTO update() throws Exception {
        ModelGroupType modelGroupType = modelGroupTypeMapper.toEntity(modelGroupTypeDTO);
        modelGroupType = modelGroupTypeRepository.save(modelGroupType);
        return modelGroupTypeMapper.toDto(modelGroupType);
    }

    public ModelGroupTypeDTO partialUpdate() throws Exception {
        return modelGroupTypeRepository
                .findById(modelGroupTypeDTO.getId())
                .map(existingMlTaskType -> {
                    modelGroupTypeMapper.partialUpdate(existingMlTaskType, modelGroupTypeDTO);

                    return existingMlTaskType;
                })
                .map(modelGroupTypeRepository::save)
                .map(modelGroupTypeMapper::toDto).get();
    }

    public void delete() throws Exception {
        modelGroupTypeRepository.deleteById(id);
    }

    @Override
    public T call() throws Exception {
        if (name.equalsIgnoreCase("create")) {
            return (T) create();
        } else if (name.equalsIgnoreCase("findAll")) {
            return (T) findAll();
        } else if (name.equalsIgnoreCase("findById")){
            return (T) findById();
        } else if(name.equalsIgnoreCase("update")) {
            return (T) update();
        } else if(name.equalsIgnoreCase("partialUpdate")) {
            return (T) partialUpdate();
        } else if(name.equalsIgnoreCase("delete")) {
            delete();
        }
        return null;
    }
}