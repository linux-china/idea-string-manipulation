package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import osmedile.intellij.stringmanip.utils.DuplicatUtils;
import osmedile.intellij.stringmanip.utils.StringUtil;
import osmedile.intellij.stringmanip.utils.StringUtils;

/**
 * @author Olivier Smedile
 * @version $Id: DecrementAction.java 62 2008-04-20 11:11:54Z osmedile $
 */
public class DecrementAction extends EditorAction {

    public DecrementAction() {
        super(new EditorWriteActionHandler() {
            public void executeWriteAction(Editor editor, DataContext dataContext) {

                //Column mode not supported
                if (editor.isColumnMode()) {
                    return;
                }
                final CaretModel caretModel = editor.getCaretModel();

                final int line = caretModel.getLogicalPosition().line;
                final int column = caretModel.getLogicalPosition().column;


                final SelectionModel selectionModel = editor.getSelectionModel();
                if (selectionModel.hasSelection() == false) {
                    selectionModel.selectLineAtCaret();
                }
                final String selectedText = selectionModel.getSelectedText();

                if (selectedText != null) {
                    String[] textParts = StringUtil
                            .splitPreserveAllTokens(selectedText, DuplicatUtils.SIMPLE_NUMBER_REGEX);
                    for (int i = 0; i < textParts.length; i++) {
                        textParts[i] = DuplicatUtils.simpleDec(textParts[i]);
                    }

                    final String s = StringUtils.join(textParts);
                    editor.getDocument().insertString(selectionModel.getSelectionStart(), s);

                    selectionModel.removeSelection();
                    caretModel.moveToLogicalPosition(new LogicalPosition(line + 1, column));
                }
            }
        });
    }
}