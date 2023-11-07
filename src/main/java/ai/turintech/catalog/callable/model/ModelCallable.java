package ai.turintech.catalog.callable.model;

import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.domain.Parameter;
import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.repository.ParameterRepository;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.dto.ModelPaginatedListDTO;
import ai.turintech.catalog.service.dto.ParameterDTO;
import ai.turintech.catalog.service.mapper.ModelMapper;
import ai.turintech.catalog.service.mapper.ParameterMapper;
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
public class ModelCallable<T> implements Callable<T> {
    private String name;
    private UUID id;
    private ModelDTO modelDTO;

    private Pageable pageable;

    public ModelCallable(String name) {
        this.name = name;
    }

    public ModelCallable(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public ModelCallable(String name, ModelDTO modelDTO) {
        this.name = name;
        this.modelDTO = modelDTO;
    }

    public ModelCallable(String name, Pageable pageable) {
        this.name = name;
        this.pageable = pageable;
    }

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaginationConverter paginationConverter;

    private ModelPaginatedListDTO findAll(){
        List<Model> models = modelRepository.findAll(pageable).getContent();
        ModelPaginatedListDTO paginatedList = paginationConverter.getPaginatedList(
                models.stream().map(modelMapper::toDto).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                modelRepository.count());
        return paginatedList;
    }

    public ModelDTO findById() throws Exception {
        Optional<Model> model = modelRepository.findOneWithEagerRelationships(id);
        return model.map(modelMapper::toDto).orElse(null);
    }

    public ModelDTO create() throws Exception {
        Model model = modelMapper.toEntity(modelDTO);
        model = modelRepository.save(model);
        return modelMapper.toDto(model);
    }

    public ModelDTO update() throws Exception {
        Model model = modelMapper.toEntity(modelDTO);
        model = modelRepository.save(model);
        return modelMapper.toDto(model);
    }

    public ModelDTO partialUpdate() throws Exception {
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

    public void delete() throws Exception {
        modelRepository.deleteById(id);
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