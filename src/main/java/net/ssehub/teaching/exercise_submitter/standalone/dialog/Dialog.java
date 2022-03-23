package net.ssehub.teaching.exercise_submitter.standalone.dialog;

import javax.swing.JDialog;
import javax.swing.JFrame;


/**
 * Super class for all dialogs with a result and useraction system.
 * 
 * @author lukas
 *
 * @param <Result>
 */
public class Dialog<Result> extends JDialog {
    
    private static final long serialVersionUID = 7462574162272078093L;
    
    private IDialogCallback<Result> callback;
//    private JFrame frame;
    private DialogResult<Result> dialogResult = new DialogResult<Result>();
    
    /**
     * Creates an new instance of {@link Dialog}.
     * 
     * 
     * @param frame , can be null
     * @param callback
     */
    public Dialog(JFrame frame, IDialogCallback<Result> callback) {
        super(frame);
        this.callback = callback;
//        this.frame = frame;
    }
    /**
     * gets called when a callback event is triggerd.
     * 
     */
    protected void doCallback() {
        this.setVisible(false);
        this.callback.run(dialogResult);
    }
    
    /**
     * Return the dialogresult.
     * 
     * @return {@link DialogResult}
     */
    public DialogResult<Result> getDialogResult() {
        return dialogResult;
    }
    
}
