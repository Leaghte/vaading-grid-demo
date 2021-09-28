package software.netcore.vaadingriddemo;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Image;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.components.grid.MultiSelectionModelImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.PageableDataProvider;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

@SpringView(name = "")
public class VaadinView extends MVerticalLayout implements View {

    private final transient UserService service;
    private final transient EventBus.ApplicationEventBus eventBus;
    private Grid<User> grid;

    public VaadinView(UserService service,
                      EventBus.ApplicationEventBus eventBus) {
        this.service = service;
        this.eventBus = eventBus;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setSizeFull();
        eventBus.subscribe(this);

        grid = new Grid<>();
        grid.setWidthFull();
        grid.setHeightFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        ((MultiSelectionModelImpl<User>) grid.getSelectionModel())
                .setSelectAllCheckBoxVisibility(MultiSelectionModel.SelectAllCheckBoxVisibility.VISIBLE);

        grid.addColumn(User::getId)
                .setId("id")
                .setCaption("ID");
        grid.addColumn(User::getFirstname)
                .setId("firstname")
                .setCaption("First name");
        grid.addColumn(User::getLastname)
                .setId("lastname")
                .setCaption("Last name");
        grid.addColumn(User::getUsername)
                .setId("username")
                .setCaption("Username");
        grid.addComponentColumn(user -> new Image("",
                        new ThemeResource(user.isOnline() ? "img/online.png" : "img/offline.png")))
                .setId("online")
                .setCaption("Online");

        grid.sort("id");

        grid.setDataProvider(new PageableDataProvider<>() {
            @Override
            protected Page<User> fetchFromBackEnd(Query<User, Object> query,
                                                  Pageable pageable) {
                return service.listUsers(pageable);
            }

            @Override
            protected List<QuerySortOrder> getDefaultSortOrders() {
                return QuerySortOrder.asc("id").build();
            }

            @Override
            protected int sizeInBackEnd(Query<User, Object> query) {
                return service.countUsers();
            }
        });

        add(grid, 1);

        add(new MHorizontalLayout()
                .add(new MButton("Start live updates")
                        .withListener(e -> service.run()))
                .add(new MButton("Stop live updates")
                        .withListener(e -> service.stop())));
    }

    @EventBusListenerMethod(scope = EventScope.APPLICATION)
    public void onUserChanged(UserChangedEvent event) {
        getUI().access(() ->
                grid.getDataProvider().refreshItem(event.getUser()));
    }

}
