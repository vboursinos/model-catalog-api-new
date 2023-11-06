package ai.turintech.catalog.callable.boolean_parameter;

import ai.turintech.catalog.repository.BooleanParameterRepository;
import ai.turintech.catalog.service.dto.BooleanParameterDTO;
import ai.turintech.catalog.service.mapper.BooleanParameterMapper;
import ai.turintech.catalog.utils.PaginationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Transactional
@Component
@Scope("prototype")
public class FindAllBooleanParametersCallable implements Callable<List<BooleanParameterDTO>> {

    // Constructor injection is the recommended way for mandatory dependencies
    public FindAllBooleanParametersCallable() {
    }

    @Autowired
    private BooleanParameterRepository booleanParameterRepository;

    @Autowired
    private BooleanParameterMapper booleanParameterMapper;

    @Autowired
    private PaginationConverter paginationConverter;

    @Override
    public List<BooleanParameterDTO> call() throws Exception {
        return booleanParameterRepository
                .findAll()
                .stream()
                .map(booleanParameterMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}