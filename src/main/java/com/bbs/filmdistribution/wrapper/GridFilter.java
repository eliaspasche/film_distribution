package com.bbs.filmdistribution.wrapper;

import com.bbs.filmdistribution.data.entity.AbstractEntity;
import com.bbs.filmdistribution.data.service.AbstractDatabaseService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.persistence.criteria.Path;
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
     * @param grid                    The {@link Grid}
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
     * @param searchValue The value to search
     * @param fieldName   The field name of the object
     */
    public void filterFieldName( String searchValue, String fieldName )
    {
        Specification<T> specificationFilter = fieldInputLike( searchValue, fieldName );
        grid.setItems( q -> abstractDatabaseService.list( PageRequest.of( q.getPage(), q.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( q ) ), specificationFilter ).stream() );
    }

    /**
     * Search in the database for a defined value and set it to the {@link Grid}
     *
     * @param searchValue The value to search
     * @param parentField The field name of the parent object
     * @param childField  The field name of the child object
     */
    public void filterFieldName( String searchValue, String parentField, String childField )
    {
        Specification<T> specificationFilter = fieldInputLike( searchValue, parentField, childField );
        grid.setItems( q -> abstractDatabaseService.list( PageRequest.of( q.getPage(), q.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( q ) ), specificationFilter ).stream() );
    }

    /**
     * The {@link Specification} that give the database the search parameter. (ignore case-sensitive)
     *
     * @param searchValue The value to search
     * @param parentField The field name of the parent object
     * @param childField  The field name of the child object
     * @return The {@link Specification}
     */
    private Specification<T> fieldInputLike( String searchValue, String parentField, String childField )
    {
        return ( root, query, criteriaBuilder ) ->
        {
            Path<String> path = root.get( parentField ).get( childField );

            return criteriaBuilder.like(
                    criteriaBuilder.lower( path ), "%" + searchValue.toLowerCase() + "%" );
        };
    }

    /**
     * The {@link Specification} that give the database the search parameter. (ignore case-sensitive)
     *
     * @param searchValue The value to search
     * @param fieldName   The field name of the object
     * @return The {@link Specification}
     */
    private Specification<T> fieldInputLike( String searchValue, String fieldName )
    {
        return ( root, query, criteriaBuilder ) -> criteriaBuilder.like(
                criteriaBuilder.lower( root.get( fieldName ) ), "%" + searchValue.toLowerCase() + "%" );
    }

}
