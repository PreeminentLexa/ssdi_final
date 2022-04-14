public class L_CorrectAnswerRevealController extends UtilControllerBase {
    public Label Question;

    public Label L1;
    public Label L2;
    public Label L3;
    public Label L4;
    public Button Button1;
    public Button Button2;
    public Button Button3;
    public Button Button4;


    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void Callback_getQuestion(String question){
        Question.setText(question);
    }
    public void Callback_getAnswers(String a1, String a2, String a3, String a4, int correct){
        if (correct==1){
            Button1.setText("Correct!");
            Button2.setText("Incorrect");
            Button3.setText("Incorrect");
            Button4.setText("Incorrect");
        }
        if (correct==2){
            Button1.setText("Incorrect");
            Button2.setText("Correct!");
            Button3.setText("Incorrect");
            Button4.setText("Incorrect");
        }
        if (correct==3){
            Button1.setText("Incorrect");
            Button2.setText("Incorrect");
            Button3.setText("Correct!");
            Button4.setText("Incorrect");
        }
        if (correct==4){
            Button1.setText("Incorrect");
            Button2.setText("Incorrect");
            Button3.setText("Correct!");
            Button4.setText("Incorrect");
        }
    }
    public void Callback_getAnswerCount(int a1count, int a2count, int a3count, int a4Count){
        L1.setText(K_WaitingForAnswerersController.L1.getText());
        L2.setText(K_WaitingForAnswerersController.L2.getText());
        L3.setText(K_WaitingForAnswerersController.L3.getText());
        L4.setText(K_WaitingForAnswerersController.L4.getText());
    }

    // This page has Utility.timeOnCorrectAnswerPage seconds to animate (I set it to 10 seconds originally)
}

