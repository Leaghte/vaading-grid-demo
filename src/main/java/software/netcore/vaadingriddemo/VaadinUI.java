package software.netcore.vaadingriddemo;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Push
@SpringUI
@PreserveOnRefresh
@RequiredArgsConstructor
@Viewport("user-scalable=no,initial-scale=1.0")
public class VaadinUI extends UI {

    @NonNull
    private final SimpleViewDisplay simpleViewDisplay;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPushConfiguration().setTransport(Transport.WEBSOCKET);
        setContent(simpleViewDisplay);
    }

}
