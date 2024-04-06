package edu.duke.zg73.Gui;

import java.io.OutputStream;
import java.io.PrintStream;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class TextAreaPrintStream extends PrintStream {
    private TextArea textArea;

    public TextAreaPrintStream(OutputStream out, TextArea textArea) {
        super(out);
        this.textArea = textArea;
    }

    @Override
    public void println(String x) {
        Platform.runLater(() -> textArea.appendText(x + "\n"));
    }

    @Override
    public void println(Object x) {
        String s = String.valueOf(x);
        Platform.runLater(() -> textArea.appendText(s + "\n"));
    }
}
