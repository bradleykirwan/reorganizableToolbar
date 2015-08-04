import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.ToolBar;

/**
 * Toolbar for JavaFX that supports drag and drop for re-ordering nodes within the toolbar
 */
public class ReorganizableToolBar extends ToolBar {
    public ReorganizableToolBar() {
        super();
    }

    public ReorganizableToolBar(Node... items) {
        super(items);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ReorganizableToolBarSkin(this);
    }
}
