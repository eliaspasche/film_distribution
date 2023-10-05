package com.bbs.filmdistribution.views.dashboard.account;

import com.bbs.filmdistribution.components.MasterDetailGridLayout;
import com.bbs.filmdistribution.converter.PasswordConverter;
import com.bbs.filmdistribution.data.entity.User;
import com.bbs.filmdistribution.data.entity.UserRole;
import com.bbs.filmdistribution.data.service.UserService;
import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;

/**
 * A view to manage the {@link User} objects.
 * This view is only accessible from users with the ADMIN {@link UserRole}.
 */
@PageTitle( "Account" )
@Route( value = "account/:accountID?/:action?(edit)", layout = DashboardLayout.class )
@RolesAllowed( "ADMIN" )
public class AccountView extends MasterDetailGridLayout<User, UserService>
{

    // Route
    private static final String ACCOUNT_ID = "accountID";
    private static final String ACCOUNT_EDIT_ROUTE_TEMPLATE = DashboardLayout.DASHBOARD_PATH + "/account/%s/edit";

    // Layout
    private H3 splitTitle;
    private TextField name;
    private TextField username;
    private PasswordField password;
    private Select<UserRole> userRoleSelect;

    private final Button saveButton = new Button( "Save" );

    /**
     * Constructor.
     *
     * @param databaseService The {@link UserService}
     */
    protected AccountView( UserService databaseService )
    {
        super( ACCOUNT_ID, ACCOUNT_EDIT_ROUTE_TEMPLATE, databaseService );
        setCreateButton( new Button( "New " + getEditItemName() ) );
    }

    @Override
    protected void buildGrid()
    {
        // Configure Grid
        Grid<User> grid = new Grid<>( User.class, false );
        setGrid( grid );

        grid.addThemeVariants( GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER );
        grid.setItems( query -> getDatabaseService().list( PageRequest.of( query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( query ) ) ).stream() );

        grid.addColumn( "name" ).setAutoWidth( true );
        grid.addColumn( "username" ).setAutoWidth( true );
        grid.addColumn( item -> item.getUserRole().name() ).setHeader( "Role" ).setAutoWidth( true );
        grid.addComponentColumn( item -> buildMenuBar( item, item.getName(), this ) ).setFrozenToEnd( true );


        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener( event -> {
            if ( event.getValue() != null )
            {
                UI.getCurrent().navigate( String.format( getEditRoute(), event.getValue().getId() ) );
            }
            else
            {
                clearForm();
                UI.getCurrent().navigate( this.getClass() );
            }
        } );

    }

    @Override
    protected void defineValidator()
    {
        // Configure Form
        BeanValidationBinder<User> binder = new BeanValidationBinder<>( User.class );
        setBinder( binder );

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField( name ).asRequired().bind( "name" );
        binder.forField( username ).asRequired().bind( "username" );
        binder.forField( password ).asRequired().withConverter( new PasswordConverter() ).bind( "hashedPassword" );
        binder.forField( userRoleSelect ).asRequired().bind( User::getUserRole, User::setUserRole );

        binder.bindInstanceFields( this );

        getCreateButton().addClickListener( e -> {
            clearForm();
            refreshGrid();
        } );

        saveButton.addClickListener( e -> {
            if ( getItemToEdit() == null )
            {
                setItemToEdit( new User() );
            }
            saveItem();
        } );
    }

    @Override
    protected void createEditorLayout()
    {
        FormLayout formLayout = new FormLayout();

        splitTitle = new H3( "New " + getEditItemName() );
        name = new TextField( "Name" );
        username = new TextField( "Username" );
        password = new PasswordField( "Password" );

        userRoleSelect = new Select<>();
        userRoleSelect.setLabel( "Role" );
        userRoleSelect.setItems( UserRole.values() );
        userRoleSelect.setItemLabelGenerator( UserRole::name );

        formLayout.add( name, username, password, userRoleSelect );

        getEditorDiv().add( splitTitle, formLayout );

        createButtonLayout();
    }

    @Override
    protected void createButtonLayout()
    {
        getCreateButton().addThemeVariants( ButtonVariant.LUMO_TERTIARY );
        saveButton.addThemeVariants( ButtonVariant.LUMO_PRIMARY );

        getButtonLayout().add( saveButton, getCreateButton() );
    }

    @Override
    protected String getEditItemName()
    {
        return "Account";
    }

    @Override
    protected void populateForm( User value )
    {
        super.populateForm( value );
        splitTitle.setText( ( getItemToEdit() == null ? "New" : "Edit" ) + " " + getEditItemName() );
    }

}
