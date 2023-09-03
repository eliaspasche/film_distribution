package com.bbs.filmdistribution.wrapper;

import com.bbs.filmdistribution.data.entity.AbstractEntity;
import com.bbs.filmdistribution.data.service.AbstractDatabaseService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * Update a {@link Grid} by a specific search name and a field of the object.
 *
 * @param <T> The object
 */
public class GridFilter<T extends AbstractEntity>
{
    private final Grid<T> grid;
    private final AbstractDatabaseService<T, ?> abstractDatabaseService;


    /**
     * The constructor.
     *
     * @param grid The {@link Grid}
     * @param abstractDatabaseService The {@link AbstractDatabaseService}
     */
    public GridFilter( Grid<T> grid, AbstractDatabaseService<T, ?> abstractDatabaseService )
    {
        this.grid = grid;
        this.abstractDatabaseService = abstractDatabaseService;
    }

    /**
     * Search in the database for a defined value and set it to the {@link Grid}
     *
     * @param fieldName The field name of the object
     * @param searchValue The value to search
     */
    public void filterFieldName( String fieldName, String searchValue )
    {
        Specification<T> specificationFilter = fieldInputLike( fieldName, searchValue );
        grid.setItems( q -> abstractDatabaseService.list( PageRequest.of( q.getPage(), q.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( q ) ), specificationFilter ).stream() );
    }

    /**
     * The {@link Specification} that give the database the search parameter. (ignore case-sensitive)
     *
     * @param fieldName The field name of the object
     * @param searchValue The value to search
     * @return The {@link Specification}
     */
    private Specification<T> fieldInputLike( String fieldName, String searchValue )
    {
        return ( root, query, criteriaBuilder ) -> criteriaBuilder.like(
                criteriaBuilder.lower( root.get( fieldName ) ), "%" + searchValue.toLowerCase() + "%" );
    }

}
