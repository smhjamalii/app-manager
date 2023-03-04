package ir.welldone.appmanager.service;

import ir.welldone.appmanager.service.bvGroups.Delete;
import ir.welldone.appmanager.service.bvGroups.Edit;
import ir.welldone.appmanager.view.dto.PagedResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

public interface CRUDService<T> {

    /**
     * Saves a new record into database
     * @param dto new record's data
     * @return T The saved record
     */
    T save(@NotNull @Valid T dto);

    /**
     * Finds a record of type T by its Id
     * @param id Identifier of the record
     * @return Optional<T> An entity if any record with the specified id exist, otherwise an empty optional
     */
    Optional<T> findById(@NotNull Long id);

    /**
     * Searches for records that their data match with specified criteria, results are paginated.
     *
     * @param criteria Criteria for search
     * @param first    Pagination is started from here
     * @param size     Count of records in each page
     * @return List<T> A list of type T
     */
    PagedResult<T> search(T criteria, Integer first, Integer size);

    /**
     * Updates the record which is passed as parameter
     * @param dto Updating record
     * @return T updated record
     */
    @Validated(Edit.class)
    T update(@NotNull @Valid T dto);

    /**
     * Removes the specified record, if any
     * @param id Primary key of removing record
     */
    void delete(@NotNull Long id);
}
