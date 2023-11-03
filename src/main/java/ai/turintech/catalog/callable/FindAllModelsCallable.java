package ai.turintech.catalog.callable;

import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.dto.ModelPaginatedListDTO;
import ai.turintech.catalog.service.mapper.ModelMapper;
import ai.turintech.catalog.utils.PaginationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

@Transactional
@Component
@Scope("prototype")
public class FindAllModelsCallable implements Callable<ModelPaginatedListDTO> {

    private Pageable pageable;

    // Constructor injection is the recommended way for mandatory dependencies
    public FindAllModelsCallable(Pageable pageable) {
        this.pageable = pageable;
    }

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaginationConverter paginationConverter;

    @Override
    public ModelPaginatedListDTO call() throws Exception {
        List<Model> models = modelRepository.findAll(pageable).getContent();
        ModelPaginatedListDTO paginatedList = paginationConverter.getPaginatedList(
                models.stream().map(modelMapper::toDto).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                modelRepository.count()
        );
        return paginatedList;
    }
}