import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class QuizApp extends JFrame implements ActionListener {
    private static final int NUM_OPTIONS = 4;
    private static final int TIME_LIMIT = 15;

    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup optionGroup;
    private JButton submitButton;
    private JLabel timerLabel;
    private int currentQuestion = 0;
    private int score = 0;
    private List<Question> questions;
    private Timer timer;
    private int seconds;

    public QuizApp() {
        questions = new ArrayList<>();
        questions.add(new Question("What is the capital of France?",
                new String[] { "Paris", "Berlin", "Rome", "Madrid" }, 1));
        questions.add(new Question("What is the highest mountain in the world?",
                new String[] { "K2", "Kangchenjunga", "Mount Everest", "Lhotse" }, 3));
        questions.add(new Question("Who painted the Mona Lisa?",
                new String[] { "Leonardo da Vinci", "Michelangelo", "Raphael", "Donatello" }, 1));
        questions.add(new Question("What is the largest lake in the world?",
                new String[] { "Caspian Sea", "Baikal", "Lake Superior", "Ontario" }, 2));
        questions.add(new Question("Which planet in the solar system is known as the “Red Planet”?",
                new String[] { "Mars", "Venus", "Earth", "Jupiter" }, 1));
        questions.add(new Question("Which county is the biggest grower of coffee?",
                new String[] { "India", "Brazil", "Japan", "China" }, 2));

        setTitle("Quiz Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLayout(new GridLayout(NUM_OPTIONS + 3, 1));
        setLocationRelativeTo(null);

        questionLabel = new JLabel();
        options = new JRadioButton[NUM_OPTIONS];
        optionGroup = new ButtonGroup();
        submitButton = new JButton("Submit");
        timerLabel = new JLabel();
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        submitButton.addActionListener(this);

        add(questionLabel);
        for (int i = 0; i < NUM_OPTIONS; i++) {
            options[i] = new JRadioButton();
            optionGroup.add(options[i]);
            add(options[i]);
        }
        add(submitButton);
        add(timerLabel);

        loadQuestion(currentQuestion);

        seconds = TIME_LIMIT;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerLabel.setText("Time: " + seconds--);
                if (seconds < 0) {
                    nextQuestion();
                }
            }
        });
        timer.start();

        setVisible(true);
    }

    private void loadQuestion(int index) {
        Question currentQuestion = questions.get(index);
        questionLabel.setText(currentQuestion.getQuestionText());
        for (int i = 0; i < NUM_OPTIONS; i++) {
            options[i].setText(currentQuestion.getOptions()[i]);
            options[i].setSelected(false);
        }
        seconds = TIME_LIMIT;
        timerLabel.setText("Time: " + seconds);
    }

    private void nextQuestion() {
        currentQuestion++;
        if (currentQuestion < questions.size()) {
            loadQuestion(currentQuestion);
        } else {
            showResults();
        }
    }

    private void showResults() {
        timer.stop();
        String results = "Your Score: " + score + "/" + questions.size() + "\n\n";
        JOptionPane.showMessageDialog(this, results, "Quiz Results", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private int getSelectedIndex() {
        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            int selectedOption = getSelectedIndex();
            if (selectedOption != -1) {
                if (selectedOption + 1 == questions.get(currentQuestion).getCorrectAnswer()) {
                    score++;
                }
                nextQuestion();
            } else {
                JOptionPane.showMessageDialog(this, "Please select an option.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizApp();
            }
        });
    }
}

class Question {
    private String questionText;
    private String[] options;
    private int correctAnswer;

    public Question(String questionText, String[] options, int correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}
