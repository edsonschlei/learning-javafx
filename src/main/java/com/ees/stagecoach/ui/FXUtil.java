package com.ees.stagecoach.ui;

import java.util.function.Consumer;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * 
 * https://stackoverflow.com/questions/25942999/using-java-lambdas-to-replace-javafx-builders#new-answer
 * 
 */
public class FXUtil {

    public static <T> T build(T node, Consumer<T> initializer) {
        initializer.accept(node);
        return node;
    }
}
