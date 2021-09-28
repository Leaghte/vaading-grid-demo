package software.netcore.vaadingriddemo;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

@Push
@SpringUI
@Theme("demo")
@PreserveOnRefresh
@Viewport("user-scalable=no,initial-scale=1.0")
public class VaadinUI extends UI {

    private final SimpleViewDisplay simpleViewDisplay;

    public VaadinUI(SimpleViewDisplay simpleViewDisplay) {
        this.simpleViewDisplay = simpleViewDisplay;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPushConfiguration().setTransport(Transport.WEBSOCKET);
        setContent(simpleViewDisplay);
    }

}
