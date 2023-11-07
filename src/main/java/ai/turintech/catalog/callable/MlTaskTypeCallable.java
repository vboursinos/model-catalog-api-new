package ai.turintech.catalog.callable;

import ai.turintech.catalog.domain.MlTaskType;
import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.repository.MlTaskTypeRepository;
import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.dto.MlTaskTypeDTO;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.dto.ModelPaginatedListDTO;
import ai.turintech.catalog.service.mapper.MlTaskTypeMapper;
import ai.turintech.catalog.service.mapper.ModelMapper;
import ai.turintech.catalog.utils.PaginationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
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
public class MlTaskTypeCallable<T> implements Callable<T> {
    private String name;
    private UUID id;
    private MlTaskTypeDTO mlTaskTypeDTO;

    private Pageable pageable;

    public MlTaskTypeCallable(String name) {
        this.name = name;
    }

    public MlTaskTypeCallable(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public MlTaskTypeCallable(String name, MlTaskTypeDTO mlTaskTypeDTO) {
        this.name = name;
        this.mlTaskTypeDTO = mlTaskTypeDTO;
    }

    public MlTaskTypeCallable(String name, Pageable pageable) {
        this.name = name;
        this.pageable = pageable;
    }

    @Autowired
    private MlTaskTypeRepository mlTaskTypeRepository;

    @Autowired
    private MlTaskTypeMapper mlTaskTypeMapper;

    @Autowired
    private PaginationConverter paginationConverter;

    private List<MlTaskTypeDTO> findAll(){
       return mlTaskTypeRepository.findAll().stream().map(mlTaskTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public MlTaskTypeDTO findById() throws Exception {
        Optional<MlTaskType> taskTypeOptional = mlTaskTypeRepository.findById(id);
        if (!taskTypeOptional.isPresent()) {
            throw new Exception("ML Task Type with ID " + id + " not found.");
        }
        return mlTaskTypeMapper.toDto(taskTypeOptional.get());
    }

    public MlTaskTypeDTO create() throws Exception {
        MlTaskType mlTaskType = mlTaskTypeMapper.toEntity(mlTaskTypeDTO);
        mlTaskType = mlTaskTypeRepository.save(mlTaskType);
        return mlTaskTypeMapper.toDto(mlTaskType);
    }

    public MlTaskTypeDTO update() throws Exception {
        MlTaskType mlTaskType = mlTaskTypeMapper.toEntity(mlTaskTypeDTO);
        mlTaskType = mlTaskTypeRepository.save(mlTaskType);
        return mlTaskTypeMapper.toDto(mlTaskType);
    }

    public MlTaskTypeDTO partialUpdate() throws Exception {
        return mlTaskTypeRepository
                .findById(mlTaskTypeDTO.getId())
                .map(existingMlTaskType -> {
                    mlTaskTypeMapper.partialUpdate(existingMlTaskType, mlTaskTypeDTO);

                    return existingMlTaskType;
                })
                .map(mlTaskTypeRepository::save)
                .map(mlTaskTypeMapper::toDto).get();
    }

    public void delete() throws Exception {
        mlTaskTypeRepository.deleteById(id);
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