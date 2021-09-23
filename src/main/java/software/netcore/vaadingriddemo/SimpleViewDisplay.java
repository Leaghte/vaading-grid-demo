package software.netcore.vaadingriddemo;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MCssLayout;

@SpringViewDisplay
public class SimpleViewDisplay extends MCssLayout implements ViewDisplay {

    public SimpleViewDisplay() {
        setSizeFull();
    }

    @Override
    public void showView(View view) {
        add((Component) view);
    }

}
