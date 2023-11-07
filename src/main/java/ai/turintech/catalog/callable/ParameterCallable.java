package ai.turintech.catalog.callable;

import ai.turintech.catalog.domain.Parameter;
import ai.turintech.catalog.repository.ParameterRepository;
import ai.turintech.catalog.service.dto.ParameterDTO;
import ai.turintech.catalog.service.mapper.ParameterMapper;
import ai.turintech.catalog.utils.PaginationConverter;
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
public class ParameterCallable<T> implements Callable<T> {
    private String name;
    private UUID id;
    private ParameterDTO parameterDTO;

    public ParameterCallable(String name) {
        this.name = name;
    }

    public ParameterCallable(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public ParameterCallable(String name, ParameterDTO parameterDTO) {
        this.name = name;
        this.parameterDTO = parameterDTO;
    }

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterMapper parameterMapper;

    @Autowired
    private PaginationConverter paginationConverter;

    private List<ParameterDTO> findAll(){
        return parameterRepository
                .findAll()
                .stream()
                .map(parameterMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public ParameterDTO findById() throws Exception {
        Optional<Parameter> model = parameterRepository.findById(id);
        return model.map(parameterMapper::toDto).orElse(null);
    }

    public ParameterDTO update() throws Exception {
        Parameter parameter = parameterMapper.toEntity(parameterDTO);
        parameter = parameterRepository.save(parameter);
        return parameterMapper.toDto(parameter);
    }

    public Optional<ParameterDTO> partialUpdate() throws Exception {
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

    public void delete() throws Exception {
        parameterRepository.deleteById(id);
    }

    @Override
    public T call() throws Exception {
        if (name.equalsIgnoreCase("findAll")) {
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