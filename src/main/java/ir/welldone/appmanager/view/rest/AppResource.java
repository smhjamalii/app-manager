package ir.welldone.appmanager.view.rest;

import ir.welldone.appmanager.service.bvGroups.Edit;
import ir.welldone.appmanager.service.interfaces.app.AppService;
import ir.welldone.appmanager.view.dto.AppDTO;
import ir.welldone.appmanager.view.dto.PagedResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/apps", produces = AppManagerMediaTypes.V1)
@Slf4j
public class AppResource {

    @Autowired
    private AppService appService;

    @PostMapping
    public ResponseEntity<EntityModel<AppDTO>> create(@Valid @RequestBody AppDTO appDTO){
        log.debug("Creating an app: {}", appDTO.getName());
        appDTO = appService.save(appDTO);
        return ResponseEntity.ok(
                EntityModel.of(appDTO, linkTo(methodOn(AppResource.class).create(appDTO)).withSelfRel())
        );
    }

    @GetMapping(path = "/{appId}")
    public ResponseEntity<EntityModel<AppDTO>> find(@NotNull @PathVariable Long appId){
        log.debug("Loading app with id: {}", appId);
        Optional<AppDTO> appDTOOptional = appService.findById(appId);
        return appDTOOptional.map(app ->
                EntityModel.of(app,
                linkTo(methodOn(AppResource.class).update(app)).withSelfRel(),
                linkTo(methodOn(AppResource.class).delete(app.getId())).withSelfRel()
        ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<AppDTO>> search(
            @PathParam(value = "name") String name,
            @PathParam(value = "desc") String desc,
            @PathParam(value = "domain") String domain,
            @PathParam(value = "active") Boolean active,
            @PathParam(value = "start") Integer start,
            @PathParam(value = "count") Integer count){

        AppDTO appDTO = AppDTO.builder()
                .name(name)
                .desc(desc)
                .domain(domain)
                .enabled(active)
                .build();

        PagedResult<AppDTO> searchResult = appService.search(appDTO, start, count);
        if(! searchResult.getResult().isEmpty()){
            return ResponseEntity.ok(
                    CollectionModel.of(searchResult.getResult(),
                            linkTo(methodOn(AppResource.class).search(name, desc, domain, active,
                                    Math.min(start + count, searchResult.getTotalRecords().intValue()), count)).withSelfRel(),
                            linkTo(methodOn(AppResource.class).search(name, desc, domain, active,
                                    Math.max(start - count, 0), count)).withSelfRel()
                            )
            );
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<EntityModel<AppDTO>> update(@Validated(Edit.class) @Valid @RequestBody AppDTO appDTO){
        log.debug("Updating app: {}", appDTO.getName());
        appDTO = appService.update(appDTO);
        return ResponseEntity.ok(
                EntityModel.of(appDTO,
                        linkTo(methodOn(AppResource.class).delete(appDTO.getId())).withSelfRel())
        );
    }

    @DeleteMapping(path = "/{appId}")
    public ResponseEntity<String> delete(@NotNull @PathVariable Long appId){
        log.debug("Deleting an app with id: {}", appId);
        appService.delete(appId);
        return ResponseEntity.ok("Selected app deleted");
    }

}
