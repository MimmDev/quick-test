import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class QuickTest extends JFrame {
    private static final int WIDTH = 350;
    private static final int HEIGHT = 100;
    private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);

    private JLabel feedbackLabel;
    private JLabel question;
    private JTextField answer;

    private ArrayList<String[]> words;
    private int index;

    public QuickTest() {
        super("QuickTest");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setPreferredSize(dimension);

        feedbackLabel = new JLabel();

        question = new JLabel();
        question.setFont(question.getFont().deriveFont(20.0f));
        answer = new JTextField(20);
        answer.addKeyListener(new TestKeyboardInput(this));

        JPanel centerPanel = new JPanel();
        centerPanel.add(question);
        centerPanel.add(answer);

        JButton check = new JButton("Check");
        check.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    check();
                }
            }
        );

        JButton next = new JButton("Next");
        next.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    next();
                }
            }
        );

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(check);
        bottomPanel.add(next);

        this.getContentPane().add(feedbackLabel, BorderLayout.NORTH);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);

        this.words = new ArrayList<>();
    }

    private void initData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("words.txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] lineWords = line.split(",");
                this.words.add(lineWords);
                line = reader.readLine();
            }

            reader.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        index = -1;
        next();
    }

    public void next() {
        index++;
        if (index >= words.size()) {
            index = 0;
        }
        feedbackLabel.setText("Translate this word:");
        question.setText(words.get(index)[0]);
        answer.setText("");
    }

    public void previous() {
        index--;
        if (index < 0) {
            index = words.size() - 1;
        }
        feedbackLabel.setText("Translate this word:");
        question.setText(words.get(index)[0]);
        answer.setText("");
    }

    public void check() {
        String a = words.get(index)[1];
        String b = answer.getText();

        if (a.equalsIgnoreCase(b)) {
            feedbackLabel.setText("Correct!");
        } else {
            feedbackLabel.setText("Wrong! Correct word: " + a);
        }
    }

    public static void main(String[] args) {
        QuickTest test = new QuickTest();
        test.setVisible(true);
        test.initData();
        test.start();
    }
}

class TestKeyboardInput extends KeyAdapter {
    private QuickTest quickTest;

    public TestKeyboardInput(QuickTest quickTest) {
        this.quickTest = quickTest;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: quickTest.previous(); break;
            case KeyEvent.VK_RIGHT: quickTest.next(); break;
            case KeyEvent.VK_ENTER: quickTest.check(); break;
        }
    }
}
