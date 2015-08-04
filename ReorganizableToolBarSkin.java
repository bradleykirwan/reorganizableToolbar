package sample;

import com.sun.javafx.scene.control.skin.ToolBarSkin;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * Skin for ReorganizableToolBar that enables drag and drop on nodes (for re-ordering) within the toolbar
 */
public class ReorganizableToolBarSkin extends ToolBarSkin {
    private ToolBar toolBar;
    private Node currentlyDraggingNode;
    public ReorganizableToolBarSkin(ToolBar toolbar) {
        super(toolbar);
        this.toolBar = toolbar;
        setUpDragAndDrop();
    }

    private ToolBar getToolBar() {
        return toolBar;
    }

    private void setUpDragAndDrop() {
        getToolBar().getItems().forEach(this::addDragHandlers);

        getToolBar().getItems().addListener((ListChangeListener<Node>) c -> {
            c.next();
            c.getAddedSubList().forEach(this::addDragHandlers);
        });

        getToolBar().setOnDragDropped(event -> {
            if (currentlyDraggingNode != null && !getToolBar().getItems().contains(currentlyDraggingNode)) {
                getToolBar().getItems().add(currentlyDraggingNode);
            }
            event.setDropCompleted(true);
        });

        getToolBar().setOnDragDone(event -> currentlyDraggingNode = null);

        getToolBar().setOnDragOver(event -> {
            if (currentlyDraggingNode != null) {
                getToolBar().getItems().remove(currentlyDraggingNode);
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();

                Node closestNode = null;
                double closestNodePosition = Double.MAX_VALUE;
                double cursorPosition = event.getX();

                for (Node node : getToolBar().getItems()) {
                    // If cursor position falls within x bounds of node then check if should appear to left or right of node
                    double thisNodeX = node.localToScene(Point2D.ZERO).getX();
                    if (Math.abs(thisNodeX - cursorPosition) < closestNodePosition) {
                        closestNode = node;
                        closestNodePosition = Math.abs(thisNodeX - cursorPosition);
                    }
                }
                if (closestNode != null && cursorPosition <= closestNode.localToScene(Point2D.ZERO).getX()) {
                    // Then the dragged node should appear to left of closest node
                    moveNode(currentlyDraggingNode, getToolBar().getItems().indexOf(closestNode));
                } else {
                    // Then the dragged node should appear to right of closest node
                    moveNode(currentlyDraggingNode, getToolBar().getItems().indexOf(closestNode) + 1);
                }
            }
        });
    }

    private void moveNode(Node node, int index) {
        getToolBar().getItems().remove(node);
        getToolBar().getItems().add(index, node);
    }

    private void addDragHandlers(Node node) {
        node.setOnDragDetected(event -> {
            currentlyDraggingNode = node;
            Dragboard dragboard = getToolBar().startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(""); // Empty string on the clipboard since we need to transfer something
            dragboard.setContent(cc);
        });
    }
}