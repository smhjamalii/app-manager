package ir.welldone.appmanager.service.impl;

import ir.welldone.appmanager.data.entity.App;
import ir.welldone.appmanager.data.repository.app.AppRepository;
import ir.welldone.appmanager.mapper.AppMapper;
import ir.welldone.appmanager.service.interfaces.app.AppService;
import ir.welldone.appmanager.view.dto.AppDTO;
import ir.welldone.appmanager.view.dto.PagedResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service @Validated @Slf4j
public class AppServiceImpl implements AppService {

    @Autowired
    private AppRepository appRepo;

    @Override
    public AppDTO save(@NotNull @Valid AppDTO dto) {
        App app = AppMapper.transferObjectToEntity(dto);
        log.debug("Persisting new App \"{}\"", app.getName());
        app = appRepo.save(app);
        return AppMapper.entityToTransferObject(app);
    }

    @Override
    public Optional<AppDTO> findById(@NotNull Long id) {
        log.debug("Finding the app with id {}", id);
        Optional<App> appOptional = appRepo.findById(id);
        return appOptional.map(AppMapper::entityToTransferObject);
    }

    @Override
    public PagedResult<AppDTO> search(AppDTO criteria, Integer first, Integer size) {
        first = first == null ? 0 : first;
        size  = size  == null ? 5 : size;
        List<App> list = appRepo.search(criteria, first, size);
        Long count = appRepo.count(criteria);
        PagedResult<AppDTO> result = new PagedResult<>();
        result.setResult(list.stream().map(AppMapper::entityToTransferObject).collect(Collectors.toList()));
        result.setTotalRecords(count);
        return result;
    }

    @Override
    public AppDTO update(@NotNull @Valid AppDTO dto) {
        App app = AppMapper.transferObjectToEntity(dto);
        log.debug("Updating App \"{}\"", app.getName());
        app = appRepo.save(app);
        return AppMapper.entityToTransferObject(app);
    }

    @Override
    public void delete(@NotNull Long id) {
        log.debug("Removing the app with id {}", id);
        Optional<App> appOptional = appRepo.findById(id);
        appOptional.ifPresent(app -> appRepo.delete(app));
    }
}
