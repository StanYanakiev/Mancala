package game;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * View Component of the game
 * @author Stan Yanakiev
 * @author Marietta Asemwota
 * @author Yihua Li
 */
public class BoardComponent extends JComponent implements ChangeListener
{
    BoardModel boardModel;

    private boolean designChosen;
    private boolean stonesChosen;
    private int design, stones;
    private BoardFormatter b;
    private JFrame frame;
    private JButton undo;

    //we are going to use panels like screens and switch back and forth
    private JPanel panel;
    private JPanel panel1;
    private JPanel pitsPanel;
    private int pitCounterA;
    private int pitCounterB;
    
    private JTextArea playerText;




    public BoardComponent(BoardModel m)
    {
        frame = new JFrame();
        boardModel = m;

        frame.setLayout(new BorderLayout());

        panel = new JPanel();
        panel1 = new JPanel();

        designChosen = false;
        stonesChosen = false;

        design = 0;
        stones = 0;

        getDecisions();
    }



    public void getDecisions()
    {
        JTextField textField = new JTextField(40);
        textField.setEditable(false);
        textField.setText("Please Choose A Design");

        JButton button1 = new JButton("Design1");
        JButton button2 = new JButton("Design2");

        button1.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        textField.setText("How many stones do you want in each pit (3 or 4)");

                        if(designChosen == false)
                        {
                            button1.setText("3 Stones");
                            button2.setText("4 Stones");
                            designChosen = true;
                            design = 1;
                            b = new DesignA();
                        }
                        else //design already chosen -- choose stones
                        {
                            stones = 3;

                            drawBoard(b);

                            frame.remove(panel);
                            frame.setContentPane(panel1);
                            frame.repaint();
                            getInfo();
                        }
                    }

                });


        button2.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        textField.setText("How many stones do you want in each pit (3 or 4)");
                        //if(!button2.getText().equals("4 Stones"))
                        if(designChosen == false)
                        {
                            button2.setText("4 Stones");
                            button1.setText("3 Stones");

                            designChosen = true;
                            design = 2;
                            b = new DesignB();
                        }
                        else
                        {
                            stonesChosen = true;
                            stones = 4;

                            //b = new DesignB();//should be design B
                            drawBoard(b);

                            frame.remove(panel);
                            frame.setContentPane(panel1);
                            frame.repaint();
                            getInfo();
                        }
                    }
                });
        panel.add(button1);
        panel.add(button2);

        frame.add(textField, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void drawBoard(BoardFormatter b)
    {
        System.out.println("Board Drawn");
        Color boardColor = b.formatBoardColor();
        panel1.setLayout(new BorderLayout());


        //pits panel
        //JPanel pitsPanel = new JPanel();
        pitsPanel = new JPanel();
        pitsPanel.setLayout(new GridLayout(2, 6, 5, 5));

        //B6-B1 => Pit12-Pit7
        for (pitCounterB = 12; pitCounterB > 6; pitCounterB--)
        {
            Pits pitCount = new Pits(pitCounterB);
            JLabel label = new JLabel(pitCount);
            label.addMouseListener(new MouseAdapter() 
            {
                @Override
				public void mouseClicked(MouseEvent e) {
                		//if a move is already made 
                		System.out.println("Undo chances: " + boardModel.stackIsEmpty()); 
					if (boardModel.stackIsEmpty()) {
						if (!boardModel.playerBTurn) 
						{
							JOptionPane.showMessageDialog(label, "This pit doesn't belongs to you");
						}
						// If clicked on an empty pit, pop up message
						else if (boardModel.isEmpty(boardModel.playerBTurn, pitCount.pitsIndex - 7 + 1)) 
						{
							JOptionPane.showMessageDialog(null, "Can not pick an empty pit. Try Again");
						} 
						
						else 
						{
							System.out.println("Player B Move Read");
							boardModel.placeStones(boardModel.playerBTurn, pitCount.pitsIndex - 7 + 1);
						}
					}
					
					else 
					{
						JOptionPane.showMessageDialog(null, "A move has already been made. Either undo it or mark it as done");
					}
				}
            });
            pitsPanel.add(label);
        }

		// change 2
		// A1-A6 => Pit0-Pit5
		for (pitCounterA = 0; pitCounterA < 6; pitCounterA++) 
		{
			Pits pitCount = new Pits(pitCounterA);
			JLabel label1 = new JLabel(pitCount);
			// JButton button1 = new JButton();
			// System.out.println("Outside of clicked: " + pitCounterA);
			label1.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mouseClicked(MouseEvent e) 
				{
					System.out.println("Undo chances: " + boardModel.stackIsEmpty());
					if(boardModel.stackIsEmpty())
					{

						if (boardModel.playerBTurn) 
						{
							JOptionPane.showMessageDialog(null, "This pit doesn't belong to you");
						} 
						// If clicked on an empty pit, pop up message
						else if(boardModel.isEmpty(boardModel.playerBTurn, pitCount.pitsIndex + 1))
						{
							JOptionPane.showMessageDialog(null, "Can not pick an empty pit. Try Again");
						}
						else 
						{
							System.out.println("Player A Move Read");
					
							boardModel.placeStones(boardModel.playerBTurn, pitCount.pitsIndex + 1);
							// System.out.println("Inside of clicked: " + pitCount.pitsIndex);
						}
					}
					
					else 
					{
						JOptionPane.showMessageDialog(null, "A move has already been made. Either undo it or mark it as done");
					}
				}
			});
            //label1.add(button1);
            //pitsPanel.add(button1);
            pitsPanel.add(label1);

        }
        pitsPanel.setBackground(boardColor);

        //mancala B panel
        JPanel mancalaB = new JPanel();
        mancalaB.setLayout(new BorderLayout());
        JTextArea leftText = new JTextArea();
        leftText.setText("M\nA\nN\nC\nA\nL\nA\n\nB");
        leftText.setEditable(false);
        mancalaB.add(leftText, BorderLayout.WEST);
        JLabel manA = new JLabel(new Pits(13));
        mancalaB.add(manA, BorderLayout.EAST);

        //mancala A panel
        JPanel mancalaA = new JPanel();
        mancalaA.setLayout(new BorderLayout());
        JTextArea rightText = new JTextArea();
        rightText.setText("M\nA\nN\nC\nA\nL\nA\n\nA");
        rightText.setEditable(false);
        mancalaA.add(rightText, BorderLayout.EAST);
        JLabel manB = new JLabel(new Pits(6));
        mancalaA.add(manB, BorderLayout.WEST);


        //undo panel and player turn panel
        JPanel southPanel = new JPanel(); 
        //JPanel undoPanel = new JPanel();
        undo = new JButton("undo" /* + this.boardModel.getUndo() */);
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Undo called");
                boolean actionUndone = boardModel.undo();
                if(!actionUndone)
                	JOptionPane.showMessageDialog(null, "There are no more undo chances. Mark move as done.");
            }
        });
        
        //timer
        JButton moveDone = new JButton("Move done"); 
        moveDone.addActionListener(
        		new ActionListener()
        		{
        			public void actionPerformed(ActionEvent e)
        			{
        				if(boardModel.stack.isEmpty())
                        	JOptionPane.showMessageDialog(null, "Make a move first");
        				else
        				{
        					System.out.println("Move is done");
            				boardModel.setTurn();
            				boardModel.emptyStack();
        				}
        				
        				//clear the stack 
        				
        			}
        		});
        
        
        southPanel.add(undo);//end undo panel
        southPanel.add(moveDone);
        

        //JPanel playerTurn = new JPanel();
        playerText = new JTextArea();
        playerText.setEditable(false);
        
        if(boardModel.playerBTurn)
        		playerText.setText("Player B Turn");
        
        else
        		playerText.setText("Player A Turn");
        
        southPanel.add(playerText);


        panel1.add(mancalaA, BorderLayout.EAST);
        panel1.add(pitsPanel, BorderLayout.CENTER);
        panel1.add(mancalaB, BorderLayout.WEST);
        panel1.add(southPanel,BorderLayout.SOUTH);
       //panel1.add(playerTurn, BorderLayout.SOUTH);
        frame.add(panel1);
        frame.pack();
        frame.setVisible(true);

    }

    /**
     *  The pits and mancala that implemented as icons
     */
    public class Pits implements Icon {
        private int pitsIndex;
        private Shape mancala, pits;
        public Pits(int pitsIndex) {
            this.pitsIndex = pitsIndex;
        }


        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D)g;

            if(pitsIndex == 6 || pitsIndex == 13){
                mancala = b.formatMancalaShape();
                g2.setColor(b.formatPitColor());
                g2.fill(mancala);

                //Mancala A => boardA[6]
                if (pitsIndex == 6) {
                    for (int i = 0; i < boardModel.boardA[pitsIndex]; i++) {
                        //System.out.println(boardModel.boardA[pitsIndex]);
                        Shape stones = b.formatStoneShape(boardModel.boardA[pitsIndex], i);
                        g2.setColor(b.formatStoneColor());
                        g2.draw(stones);
                        g2.fill(stones);
                    }
                }
                //Mancala B => boardB[ 13 - 7 = 6]
                else {
                    for (int i = 0; i < boardModel.boardB[pitsIndex - 7]; i++) {
                        Shape stones = b.formatStoneShape(boardModel.boardB[pitsIndex - 7], i);
                        g2.setColor(b.formatStoneColor());
                        g2.draw(stones);
                        g2.fill(stones);
                    }
                }
            }
            //Pits
            else{
                pits = b.formatPitShape();
                g2.setColor(b.formatPitColor());
                g2.fill(pits);
                //A1-A6 => boardA[0]-boardA[5]
                if (pitsIndex < 6) {
                    for (int i = 0; i < boardModel.boardA[pitsIndex]; i++) {
                        Shape stones = b.formatStoneShape(boardModel.boardA[pitsIndex], i);
                        g2.setColor(b.formatStoneColor());
                        g2.draw(stones);
                        g2.fill(stones);
                    }
                }
                //B6-B1 => boardB[12 - 7 = 5]-boardB[7 - 7 = 0]
                else {
                    for (int i = 0; i < boardModel.boardB[pitsIndex - 7]; i++) {
                        Shape stones = b.formatStoneShape(boardModel.boardB[pitsIndex - 7], i);
                        g2.setColor(b.formatStoneColor());
                        g2.draw(stones);
                        g2.fill(stones);
                    }
                }
            }
        }

        @Override
        public int getIconWidth() {
            return 100;
        }

        @Override
        public int getIconHeight() {
            if (pitsIndex == 6 || pitsIndex == 13){
                return 200;
            }
            else
                return 100;
        }

    }


    public void getInfo()
    {
    	    System.out.println("Design " + design +" chosen");
        System.out.println(stones + " Stones Chosen");
        System.out.println("-------------------------------");
        boardModel.setStones(stones);
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        panel1.repaint();
    
        if(boardModel.playerBTurn)
			playerText.setText("Player B Turn");

		else
			playerText.setText("Player A Turn");
  
    }
}