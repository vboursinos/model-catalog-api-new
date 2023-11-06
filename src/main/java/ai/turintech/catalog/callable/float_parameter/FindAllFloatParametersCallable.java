package ai.turintech.catalog.callable.float_parameter;

import ai.turintech.catalog.repository.FloatParameterRepository;
import ai.turintech.catalog.service.dto.FloatParameterDTO;
import ai.turintech.catalog.service.mapper.FloatParameterMapper;
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
public class FindAllFloatParametersCallable implements Callable<List<FloatParameterDTO>> {

    // Constructor injection is the recommended way for mandatory dependencies
    public FindAllFloatParametersCallable() {
    }

    @Autowired
    private FloatParameterRepository floatParameterRepository;

    @Autowired
    private FloatParameterMapper floatParameterMapper;

    @Autowired
    private PaginationConverter paginationConverter;

    @Override
    public List<FloatParameterDTO> call() throws Exception {
        return floatParameterRepository
                .findAll()
                .stream()
                .map(floatParameterMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}