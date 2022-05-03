package test;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioInputStream;


public class GamePlay extends JPanel implements KeyListener, Runnable {
	private Space blockSpace[][];// 이차원 배열의 선언 :: 테트리스 공간의 각각의 블록 1개를 의미함
	private int checkTime;// checkTime : 시간에 따른 블록 드랍 속도 조절 변수
	private Block currentBlock, holdBlock, nextBlock_1, nextBlock_2, nextBlock_3; // 현재 내려오고 있는 블록 current / hlod는 쉬프트 키로
																					// 홀드칸에 저장되는 블록 , next123은 다음에 나올
																					// 블록들
	private JButton btn, startButton; // startButton : 시작 버튼 / btn : 키 리스너를 받을 임의의 버튼 (사용하지는 않는다)
	private JLabel backgroundLabel, next, holdLabel; // 다음 블록, 홀드 블록, 테트리스 배경 이미지 담을 라벨
	private int i, holdChecker, shold , crush , finalCrush;
	private boolean overChecker, playChecker, canStart, networkChecker, messageChecker,winChecker,startChecker; // canStart : 시작 가능 여부 확인, playChecker : true이면 게임 진행중, false이면
														// 게임 중단 / overCheck : 게임 오버 여부 확인 / networkChecker : 대전모드인지 확인  / downChcker 방금 블록을 다운시켯는지 확인 
	private boolean endChecker;
	private Thread play, gameoverAniThread;
	private GameoverAnimation gameoverThread;
	
	private File gameoverSound,crushSound,backgroundSound; // 게임오버 , 파괴음 파일 , 클립 선언 
	private Clip gameoverClip,crushClip,backgroundClip;

	private int messageType = 0;
	


	public GamePlay() {
		canStart = true;
		startButton = new JButton(new ImageIcon("Start.png")); // 시작 알림 레이블
		add(startButton);
		networkChecker= false;
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(networkChecker == false)
				{
					startButton();
				}
				else
				{
					messageChecker = true;
					messageType = 2;
					startButton.setVisible(false);
				}
				
			}
		});
		startButton.setBounds(50, 150, 100, 50);

		finalCrush = 0;
		crush = 0;
		checkTime = 1;
		gameoverThread = new GameoverAnimation();
		gameoverAniThread = new Thread(gameoverThread);

		
		endChecker = false;
		playChecker = false;
		overChecker = false;
		networkChecker = false;
		messageChecker = false;
		winChecker = false;
		shold = -1;
		holdChecker = -1;
		setLayout(null);
		setPreferredSize(new Dimension(IntClass.BLOCKSIZE * IntClass.xNumber, IntClass.BLOCKSIZE * IntClass.yNumber));
		next = new JLabel(new ImageIcon("next.png"));

		// 넥스트,홀드 블록 , 레이블 초기화, 자리 지정
		holdBlock = new Block();
		holdLabel = new JLabel(new ImageIcon("hold.png"));
		holdLabel.add(holdBlock);
		holdLabel.setBounds(350, 20, 100, 100);
		next.setBounds(200, 0, 150, 400);
		add(next);
		add(holdLabel);
		btn = new JButton("Btn");
		btn.setBackground(Color.black);
		add(btn);
		btn.setBounds(5000, 100, 0, 0);
		btn.addKeyListener(this);
		btn.setVisible(true);
		btn.requestFocus();

		currentBlock = new Block();
		currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
				IntClass.BLOCKSIZE * 4);// 이미지를 이동하고나서 사이즈가 다르면 움직이지 않음 !
		add(currentBlock);
		setVisible(true);
		setPreferredSize(new Dimension(IntClass.BLOCKSIZE, IntClass.BLOCKSIZE));
		blockSpace = new Space[IntClass.xNumber][IntClass.yNumber + 4];
		for (int i = 0; i < IntClass.xNumber; i++) // 레이블을 초기화 하는 작업
		{
			for (int j = 0; j < IntClass.yNumber + 4; j++) {

				blockSpace[i][j] = new Space();
			}
		}
		for (int i = 0; i < IntClass.xNumber; i++) // 레이블을 배치하는 작업
		{
			for (int j = 0; j < IntClass.yNumber + 4; j++) {
				this.add(blockSpace[i][j]);
				blockSpace[i][j].setBounds(IntClass.BLOCKSIZE * i,
						IntClass.BLOCKSIZE * IntClass.yNumber - IntClass.BLOCKSIZE * (j + 1), IntClass.BLOCKSIZE,
						IntClass.BLOCKSIZE);
			}
		}
		
		
		backgroundLabel = new JLabel(new ImageIcon("Back.png"));
		add(backgroundLabel);
		backgroundLabel.setBounds(0, 0, 200, 400);

		backgroundSound = new File("BackGround.wav");
		crushSound = new File("crush.wav");
		gameoverSound = new File("gameover.wav");
			
	

	}
	
	


	public void keyPressed(KeyEvent event) // 키 이벤트 처리
	{
		switch (event.getKeyCode()) {
		case KeyEvent.VK_RIGHT: {
			if (overChecker) {
				right();
			}
			break;
		}
		case KeyEvent.VK_LEFT: {
			if (overChecker) {
				left();
			}
			break;
		}
		case KeyEvent.VK_UP: {
			if (overChecker) {
				initLabel();
				change();
				shadow();
			}
			break;
		}
		case KeyEvent.VK_DOWN: {
			if (overChecker) {
				down();
			}

			break;
		}
		case KeyEvent.VK_SPACE: {
			if (overChecker) {
				initLabel();
				fillLabel();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				shadow();
			}

			break;
		}
		case KeyEvent.VK_SHIFT: {
			if (holdChecker == -1) {
				if (overChecker) {
					initLabel();
					hold();
					shadow();
				}
			}
			break;
		}
		}
	}

	public void keyTyped(KeyEvent event) {
	}

	public void keyReleased(KeyEvent event) {
	}

	public void startButton() // 게임 시작 메소드
	{
		checkTime = 1;
		canStart = false;
		startButton.setVisible(false);

		clear();

		overChecker = true;
		playChecker = true;

		play = new Thread(this);
		play.start();

		currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
				IntClass.BLOCKSIZE * 4);
		setNext();
		setNextPosition();
		holdBlock.setBounds(10, 15, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
		holdBlock.setState(999);
		holdBlock.BlockColor();
		shadow();
		i = currentBlock.getPt().y / IntClass.BLOCKSIZE + 3;
		nextBlock_1.setVisible(true);
		nextBlock_2.setVisible(true);
		nextBlock_3.setVisible(true);
		
		try { // 배경음 재생 시도 
			backgroundClip = AudioSystem.getClip();
			backgroundClip.open(AudioSystem.getAudioInputStream(backgroundSound));
			backgroundClip.start();
			
		}
		catch(Exception e)
		{
		}
		
	}

	public void clear() // 블록 모두 지우기
	{
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 22; j++) {
				blockSpace[i][j].setState(false);
				blockSpace[i][j].setType(0);
				blockSpace[i][j].setImg();
			}
		}
	}

	
	public void initGame() { // 게임을 초기화 하는 메소드 
		clear();
		try {
			nextBlock_1.setVisible(false);
			nextBlock_2.setVisible(false);
			nextBlock_3.setVisible(false);
		}catch(NullPointerException e)
		{
		}
		
		canStart = true;
		startButton.setVisible(true);
		playChecker = false;
		overChecker = false;
		currentBlock.blockInit();
		holdBlock.setState(999);
		holdBlock.BlockColor();
		holdChecker = -1;
		setHoldLabel();
	}
	
	public void setHoldPosition() // 홀드에 저장된 블록 설정
	{
		switch (holdBlock.getState()) {
		case 0: {
			holdBlock.setBounds(10, 15, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 1: {
			holdBlock.setBounds(0, 15, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 2: {
			holdBlock.setBounds(20, 15, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 3: {
			holdBlock.setBounds(0, 15, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 4: {
			holdBlock.setBounds(20, 15, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 5: {
			holdBlock.setBounds(20, 15, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 6: {
			holdBlock.setBounds(20, 10, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		}
	}

	public void setHoldLabel() // 홀드 버튼 사용 가능여부 표현
	{
		switch (holdChecker) {
		case -1: {
			holdLabel.setIcon(new ImageIcon("hold.png"));
			break;
		}
		case 1: {
			holdLabel.setIcon(new ImageIcon("Hold2.png"));
			break;
		}
		}
	}

	public void setNext() // 다음에 나올 블록들 설정
	{
		nextBlock_1 = new Block();
		nextBlock_1.setBounds(35, 30, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
		nextBlock_1.setState((int) (Math.random() * 7));
		nextBlock_1.BlockColor();
		nextBlock_1.setBlock();
		next.add(nextBlock_1);
		nextBlock_2 = new Block();
		nextBlock_2.setBounds(35, 150, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
		nextBlock_2.setState((int) (Math.random() * 7));
		nextBlock_2.BlockColor();
		nextBlock_2.setBlock();
		next.add(nextBlock_2);
		nextBlock_3 = new Block();
		nextBlock_3.setBounds(35, 270, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
		nextBlock_3.setState((int) (Math.random() * 7));
		nextBlock_3.BlockColor();
		nextBlock_3.setBlock();
		next.add(nextBlock_3);
	}

	public void setNextPosition() {
		switch (nextBlock_1.getState()) {
		case 0: {
			nextBlock_1.setBounds(35, 30, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 1: {
			nextBlock_1.setBounds(25, 30, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 2: {
			nextBlock_1.setBounds(45, 30, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 3: {
			nextBlock_1.setBounds(25, 30, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 4: {
			nextBlock_1.setBounds(45, 30, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 5: {
			nextBlock_1.setBounds(45, 30, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 6: {
			nextBlock_1.setBounds(45, 30, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		}
		switch (nextBlock_2.getState()) {
		case 0: {
			nextBlock_2.setBounds(35, 150, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 1: {
			nextBlock_2.setBounds(25, 150, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 2: {
			nextBlock_2.setBounds(45, 150, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 3: {
			nextBlock_2.setBounds(25, 150, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 4: {
			nextBlock_2.setBounds(45, 150, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 5: {
			nextBlock_2.setBounds(45, 150, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 6: {
			nextBlock_2.setBounds(45, 150, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		}
		switch (nextBlock_3.getState()) {
		case 0: {
			nextBlock_3.setBounds(35, 270, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 1: {
			nextBlock_3.setBounds(25, 270, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 2: {
			nextBlock_3.setBounds(45, 270, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 3: {
			nextBlock_3.setBounds(25, 270, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 4: {
			nextBlock_3.setBounds(45, 270, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 5: {
			nextBlock_3.setBounds(45, 270, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		case 6: {
			nextBlock_3.setBounds(45, 270, IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
			break;
		}
		}
	}

	public void GameoverCheck() {
		for (int i = 0; i < 10; i++) {
			if (blockSpace[i][20].getState() || blockSpace[i][19].getState()) {
				overChecker = false;
				System.out.println("GameOver");
				gameoverAniThread = new Thread(gameoverThread);
				gameoverAniThread.start();			
				endChecker = true;
				break;
			}
		}
	}

	public void run() // 시간에 따라 블록이 내려오는 속도 조절
	{
		while (overChecker) {
			down();
			if (checkTime < 10) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			} else if (checkTime >= 10 && checkTime < 30) {
				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
				}
			} else if (checkTime >= 30 && checkTime < 50) {
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
				}
			} else if (checkTime >= 50 && checkTime < 70) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			} else if (checkTime >= 70 && checkTime < 100) {
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
				}
			} else if (checkTime >= 100) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
			}
			checkTime++;
			
		}
	}

	public void nextBlock() // 다음 블록 읽어오기
	{
		if (overChecker) {
			GameoverCheck();
			currentBlock.setState(nextBlock_1.getState());
			nextBlock_1.setState(nextBlock_2.getState());
			nextBlock_2.setState(nextBlock_3.getState());
			nextBlock_3.setState((int) (Math.random() * 7));
			nextBlock_1.BlockColor();
			nextBlock_1.setBlock();
			nextBlock_2.BlockColor();
			nextBlock_2.setBlock();
			nextBlock_3.BlockColor();
			nextBlock_3.setBlock();
			setNextPosition();
			currentBlock.BlockColor();
			currentBlock.setx(IntClass.BLOCKSIZE * 3);
			currentBlock.sety(-IntClass.BLOCKSIZE * 2);
			currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
					IntClass.BLOCKSIZE * 4);
			currentBlock.setBlockPosition();
			shadow();
			currentBlock.setBlock();
			checkTime++;
			down();
		}

	}

	public void hold() {
		if (shold != -1) {
			initLabel();
			int x = -1;
			holdChecker = 1;
			setHoldLabel();
			switch (currentBlock.getState()) {
			case 0: {
			}
			case 1: {
			}
			case 2: {
			}
			case 3: {
			}
			case 4: {
			}
			case 5: {
			}
			case 6: {
				x = currentBlock.getState();
				break;
			}
			case 7: {
				x = 1;
				break;
			}
			case 8: {
				x = 2;
				break;
			}
			case 9: {
			}
			case 10: {
			}
			case 11: {
				x = 3;
				break;
			}
			case 12: {
			}
			case 13: {
			}
			case 14: {
				x = 4;
				break;
			}
			case 15: {
			}
			case 16: {
			}
			case 17: {
				x = 5;
				break;
			}
			case 18: {
				x = 6;
				break;
			}
			}

			currentBlock.setState(shold);
			currentBlock.BlockColor();
			holdBlock.setState(x);
			setHoldPosition();
			holdBlock.setBlock();
			holdBlock.BlockColor();
			shold = x;
			currentBlock.setx(IntClass.BLOCKSIZE * 3);
			currentBlock.sety(-IntClass.BLOCKSIZE * 2);
			currentBlock.setBlock();
			currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
					IntClass.BLOCKSIZE * 4);
			shadow();
		} else {
			initLabel();
			holdChecker = 1;
			setHoldLabel();
			switch (currentBlock.getState()) {
			case 0: {
			}
			case 1: {
			}
			case 2: {
			}
			case 3: {
			}
			case 4: {
			}
			case 5: {
			}
			case 6: {
				shold = currentBlock.getState();
				break;
			}
			case 7: {
				shold = 1;
				break;
			}
			case 8: {
				shold = 2;
				break;
			}
			case 9: {
			}
			case 10: {
			}
			case 11: {
				shold = 3;
				break;
			}
			case 12: {
			}
			case 13: {
			}
			case 14: {
				shold = 4;
				break;
			}
			case 15: {
			}
			case 16: {
			}
			case 17: {
				shold = 5;
				break;
			}
			case 18: {
				shold = 6;
				break;
			}
			}
			holdBlock.setState(shold);
			holdBlock.setBlock();
			setHoldPosition();
			holdBlock.BlockColor();
			nextBlock();
			currentBlock.BlockColor();
			shadow();
		}
	}

	public void checkBreak(int a)// 1줄 체크
	{
		for (int i = 0; i < 10; i++) {
			if (!blockSpace[i][a].getState()) {
				return;
			}
		}

		try {
			crushClip = AudioSystem.getClip();
			crushClip.open(AudioSystem.getAudioInputStream(crushSound));
			crushClip.start();
			crush++;
		}
		catch(Exception e)
		{
			System.out.println("??");
		}
		for (int i = a; i < 21; i++) {
			for (int j = 0; j < 10; j++) {
				blockSpace[j][i].setState(blockSpace[j][i + 1].getState());
				blockSpace[j][i].setType(blockSpace[j][i + 1].getType());
				blockSpace[j][i].setImg();
			}
		}
		
		

	}

	public void down() { // else구문은 하강시, 그 외에 if, else if문은 지면에 닿을 때를 의미함 
		int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE);
		int y = 0;
		crush = 0;
		switch (currentBlock.getState()) {
		case 0: {
			if (i > 0 && (blockSpace[(currentBlock.getPt().x / IntClass.BLOCKSIZE) + 1][i - 1].getState()
					|| blockSpace[(currentBlock.getPt().x / IntClass.BLOCKSIZE) + 2][i - 1].getState())) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(3);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(3);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(3);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(3);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
				
				
			} else if (i == 0) {

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(3);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(3);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(3);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(3);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();

			}

			else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 1: {
			if ((i > 0 && (blockSpace[(currentBlock.getPt().x / IntClass.BLOCKSIZE) + 1][i - 1].getState()
					|| blockSpace[(currentBlock.getPt().x / IntClass.BLOCKSIZE) + 2][i - 1].getState()))
					|| blockSpace[(currentBlock.getPt().x / IntClass.BLOCKSIZE) + 3][i].getState()) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();


			} else if (i == 0) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();


			}

			else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 2: {
			if (i > 0
					&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState()
							|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i - 1].getState())
					|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState()) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				throwNetworkData();
				holdChecker = -1;
				setHoldLabel();
				nextBlock();


			} else if (i == 0) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				holdChecker = -1;
				setHoldLabel();
				nextBlock();

			}

			else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 3: {
			if (i > 0 && (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState()
					|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i - 1].getState()
					|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i - 1].getState())) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				throwNetworkData();
				holdChecker = -1;
				setHoldLabel();
				nextBlock();

				
			} else if (i == 0) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;


			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 4: {
			if (i > 0 && (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState()
					|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i - 1].getState()
					|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i - 1].getState())) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				throwNetworkData();
				holdChecker = -1;
				setHoldLabel();
				nextBlock();

			} else if (i == 0) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();

			}

			else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 5: {
			if (i > 0 && (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState()
					|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i - 1].getState()
					|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i - 1].getState())) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				throwNetworkData();
				holdChecker = -1;
				setHoldLabel();

				nextBlock();
				

			} else if (i == 0) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();


			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 6: {
			if ((i > 1 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 2].getState())) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
				y = i + 2;
				checkBreak(y);
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				y = i - 1;
				checkBreak(y);
				throwNetworkData();
				holdChecker = -1;
				setHoldLabel();
				nextBlock();


			} else if (i == 1) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][3].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][3].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][3].setImg();
				y = 3;
				checkBreak(y);
				y = 2;
				checkBreak(y);
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				

			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}

			break;
		}

		case 7: {
			if ((i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i - 1].getState()
					|| (i > 1 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i - 2].getState()))) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i - 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i - 1].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i - 1].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				y = i - 1;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				

			} else if (i == 1) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(5);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
				y = 2;
				checkBreak(y);
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				

			}

			else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 8: {
			if ((i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState()
					|| (i > 1 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i - 2].getState()))) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i - 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i - 1].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i - 1].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				y = i - 1;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
	
			} else if (i == 1) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(7);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
				y = 2;
				checkBreak(y);
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
			
			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 9: {
			if ((i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState()
					|| (i < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].getState()))) {

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
				y = i + 2;
				checkBreak(y);
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
			} else if (i == 0) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
				y = 2;
				checkBreak(y);
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}
		case 10: {
			if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
					|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i - 1].getState())
					|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()))) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
				
			} else if (i == 0) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 11: {
			if (i > 0 && (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())
					|| i > 0 && (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i - 1].getState())) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
				y = i + 2;
				checkBreak(y);
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				throwNetworkData();
				holdChecker = -1;
				setHoldLabel();
				nextBlock();
				
			} else if (i == 0) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(9);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				y = 2;
				checkBreak(y);
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
			
			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 12: {
			if ((i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())
					|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i - 1].getState())) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
				y = i + 2;
				checkBreak(y);
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
			} else if (i == 0) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				y = 2;
				checkBreak(y);
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 13: {
			if ((i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())
					|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
					|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].getState()) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
			

			}

			else if (i == 0) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
				
			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 14: {
			if ((i < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].getState())
					|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i - 1].getState())) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
				y = i + 2;
				checkBreak(y);
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
				
			} else if (i == 0) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(11);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				y = 2;
				checkBreak(y);
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
				
			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 15: {
			if ((i > 1 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 2].getState())
					|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i - 1].getState())) {

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				y = i - 1;
				checkBreak(y);
				throwNetworkData();
				holdChecker = -1;
				setHoldLabel();

				nextBlock();
				
				
			} else if (i == 1) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				y = 2;
				checkBreak(y);
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 16: {
			if ((i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i - 1].getState())
					|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i - 1].getState())
					|| (i > 1 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 2].getState())) {

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();
				y = i;
				checkBreak(y);
				y = i - 1;
				checkBreak(y);
				throwNetworkData();
				holdChecker = -1;
				setHoldLabel();

				nextBlock();
				
				
			} else if (i == 1) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				

			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 17: {

			if ((i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i - 1].getState())
					|| i > 1 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 2].getState()) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();

				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
				y = i + 1;
				checkBreak(y);
				y = i;
				checkBreak(y);
				y = i - 1;
				checkBreak(y);
				
				throwNetworkData();
				holdChecker = -1;
				setHoldLabel();

				nextBlock();
				
	
			} else if (i == 1) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(13);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
				y = 2;
				checkBreak(y);
				y = 1;
				checkBreak(y);
				y = 0;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
		

			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}

		case 18: {
			if ((i < 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i - 1].getState())
					|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())
					|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i - 1].getState())
					|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i - 1].getState())) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setImg();
				y = i;
				checkBreak(y);
				throwNetworkData();
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
		

			} else if (i == 0) {
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(true);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(1);
				blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
				y = 0;
				checkBreak(y);
				
				nextBlock();
				holdChecker = -1;
				setHoldLabel();
				
		

			} else {
				currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}
		}
		
	}

	public void right() {
		int x = 17 - currentBlock.getPt().y / IntClass.BLOCKSIZE;

		switch (currentBlock.getState()) {
		case 0: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 3)) {
				if ((x < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				} else if (x == 19
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 1: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 4)) {
				if ((x < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 4][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				} else if (x == 19
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 2: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 3)) {
				if ((x < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				} else if (x == 19
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 3: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 4)) {
				if ((x < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 4][x].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				} else if (x == 19
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 4][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 4: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 3)) {
				if ((x < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				} else if (x == 19
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 5: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 3)) {
				if ((x < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x + 1].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				} else if (x == 19
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 6: // else if / left
		{
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 2)) {
				if ((x > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x - 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x + 2].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}

			}
			break;
		}
		case 7: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 4)) {
				if ((x > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 4][x - 1].getState() == false)
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 4][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x + 1].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}

			break;
		}
		case 8: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 2)) {
				if ((x > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][x - 1].getState() == false)
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x + 1].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 9: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 3)) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x + 1].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x + 2].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 10: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 4)) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 4][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 4][x + 1].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 11: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 3)) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x + 1].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x + 2].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 12: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 3)) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x + 1].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x + 2].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 13: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 4)) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 4][x + 1].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 14: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 3)) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x + 1].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x + 2].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 15: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 3)) {
				if ((x > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x - 1].getState() == false)
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x + 1].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 16: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 3)) {
				if ((x > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x - 1].getState() == false)
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 17: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 2)) {
				if ((x > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x - 1].getState() == false)
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x + 1].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 18: {
			if (currentBlock.getPt().x < IntClass.BLOCKSIZE * (IntClass.xNumber - 4)) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 4][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x + IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}

		}
	}

	public void left() {
		int x = 17 - currentBlock.getPt().y / IntClass.BLOCKSIZE;

		switch (currentBlock.getState()) {
		case 0: {
			if (currentBlock.getPt().x > -IntClass.BLOCKSIZE) {
				if ((x < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				} else if (x == 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 1: {
			if (currentBlock.getPt().x > -IntClass.BLOCKSIZE) {
				if ((x < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				} else if (x == 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 2: {
			if (currentBlock.getPt().x >= IntClass.BLOCKSIZE) {
				if ((x < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE - 1][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				} else if (x == 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 3: {
			if (currentBlock.getPt().x > -IntClass.BLOCKSIZE) {
				if ((x < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				} else if (x == 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 4: {
			if (currentBlock.getPt().x > 0) {
				if ((x < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE - 1][x].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				} else if (x == 19
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE - 1][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 5: {
			if (currentBlock.getPt().x > 0) {
				if ((x < 19 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE - 1][x].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				} else if (x == 19
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE - 1][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 6: {
			if (currentBlock.getPt().x > -IntClass.BLOCKSIZE) {
				if ((x > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x - 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 1].getState() == false)
						&& (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 2].getState() == false)) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 7: {
			if (currentBlock.getPt().x > -IntClass.BLOCKSIZE * 2) {
				if ((x > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x - 1].getState() == false)
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][x + 1].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 8: {
			if (currentBlock.getPt().x >= IntClass.BLOCKSIZE) {
				if ((x > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE - 1][x - 1].getState() == false)
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE - 1][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 1].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 9: {
			if (currentBlock.getPt().x > -IntClass.BLOCKSIZE) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 1].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 2].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 10: {
			if (currentBlock.getPt().x > -IntClass.BLOCKSIZE) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 1].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 11: {
			if (currentBlock.getPt().x > -IntClass.BLOCKSIZE) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][x + 1].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][x + 2].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 12: {
			if (currentBlock.getPt().x > -IntClass.BLOCKSIZE) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 1].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 2].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 13: {
			if (currentBlock.getPt().x > -IntClass.BLOCKSIZE) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 1].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 14: {
			if (currentBlock.getPt().x > -IntClass.BLOCKSIZE) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][x + 1].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 2].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 15: {
			if (currentBlock.getPt().x > -IntClass.BLOCKSIZE) {
				if ((x > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x - 1].getState() == false)
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 1].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 16: {
			if (currentBlock.getPt().x > 0) {
				if ((x > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x - 1].getState() == false)
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE - 1][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 17: {
			if (currentBlock.getPt().x > 0) {
				if ((x > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x - 1].getState() == false)
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE - 1][x].getState() == false
						&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][x + 1].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}
			break;
		}
		case 18: {
			if (currentBlock.getPt().x > 0) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE - 1][x].getState() == false) {
					initLabel();
					currentBlock.setBounds(currentBlock.getPt().x - IntClass.BLOCKSIZE, currentBlock.getPt().y,
							IntClass.BLOCKSIZE * 4, IntClass.BLOCKSIZE * 4);
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					shadow();
				}
			}

			break;
		}

		}

	}

	public void change() {
		int x = currentBlock.getPt().x / IntClass.BLOCKSIZE;
		int y = 17 - currentBlock.getPt().y / IntClass.BLOCKSIZE;
		switch (currentBlock.getState()) {
		case 0: {
			break;
		}
		case 1: {
			if (y > 0 && blockSpace[x + 3][y - 1].getState() == false && blockSpace[x + 3][y].getState() == false) {
				currentBlock.setState(7);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}

			else if (blockSpace[x + 1][y + 1].getState() == false && blockSpace[x + 1][y + 2].getState() == false) {
				currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
				currentBlock.setState(7);
				currentBlock.setBlock();
				currentBlock.BlockColor();
				currentBlock.sety(currentBlock.getPt().y - IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}
		case 2: {
			if (y > 0 && blockSpace[x][y - 1].getState() == false && blockSpace[x][y].getState() == false) {
				currentBlock.setState(8);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			} else if (blockSpace[x + 2][y + 1].getState() == false && blockSpace[x + 2][y + 2].getState() == false) {
				currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
				currentBlock.setState(8);
				currentBlock.BlockColor();
				currentBlock.setBlock();
				currentBlock.sety(currentBlock.getPt().y - IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}
		case 3: {
			if (blockSpace[x + 1][y + 1].getState() == false && blockSpace[x + 2][y + 2].getState() == false) {
				currentBlock.setState(9);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}
			break;
		}
		case 4: {
			if (blockSpace[x + 1][y + 1].getState() == false && blockSpace[x + 1][y + 2].getState() == false) {
				currentBlock.setState(12);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}
			break;
		}
		case 5: {
			if (y > 0 && blockSpace[x + 1][y - 1].getState() == false) {
				currentBlock.setState(15);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			} else {
				currentBlock.sety(currentBlock.getPt().y - IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
				currentBlock.setState(15);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}
			break;
		}
		case 6: {
			if (currentBlock.getPt().x == (IntClass.xNumber - 2) * IntClass.BLOCKSIZE)// 우측에 붙을 시
			{
				if (blockSpace[x - 2][y].getState() == false && blockSpace[x - 1][y].getState() == false
						&& blockSpace[x][y].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE * 2);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(18);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (currentBlock.getPt().x == (IntClass.xNumber - 3) * IntClass.BLOCKSIZE)// 우측 -1에 부ㅡㅌ을 시
			{
				if (blockSpace[x - 1][y].getState() == false && blockSpace[x][y].getState() == false
						&& blockSpace[x + 2][y].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(18);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (currentBlock.getPt().x == -IntClass.BLOCKSIZE)// 좌측
			{
				if (blockSpace[x + 4][y].getState() == false && blockSpace[x + 2][y].getState() == false
						&& blockSpace[x + 3][y].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(18);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (blockSpace[x][y].getState() == false && blockSpace[x + 2][y].getState() == false
					&& blockSpace[x + 3][y].getState() == false) {
				currentBlock.setState(18);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}

			break;
		}
		case 7: {
			if (currentBlock.getPt().x == -IntClass.BLOCKSIZE * 2) {
				if (blockSpace[x + 4][y + 1].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(1);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (blockSpace[x + 1][y].getState() == false && blockSpace[x + 3][y + 1].getState() == false) {
				currentBlock.setState(1);
				currentBlock.BlockColor();
				currentBlock.setBlock();

			}
			break;
		}
		case 8: {
			if (currentBlock.getPt().x == (IntClass.xNumber - 2) * IntClass.BLOCKSIZE) {
				if (blockSpace[x - 1][y + 1].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(2);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (blockSpace[x][y + 1].getState() == false && blockSpace[x + 2][y].getState() == false) {
				currentBlock.setState(2);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}
			break;
		}
		case 9: {
			if (currentBlock.getPt().x == (IntClass.xNumber - 3) * IntClass.BLOCKSIZE) {
				if (blockSpace[x][y + 1].getState() == false && blockSpace[x + 2][y + 1].getState() == false
						&& blockSpace[x + 2][y].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(10);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (blockSpace[x + 2][y + 1].getState() == false && blockSpace[x + 3][y + 1].getState() == false
					&& blockSpace[x + 3][y].getState() == false) {
				currentBlock.setState(10);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}
			break;
		}
		case 10: {
			if (blockSpace[x + 1][y].getState() == false && blockSpace[x + 2][y].getState() == false
					&& blockSpace[x + 2][y + 2].getState() == false) {
				currentBlock.setState(11);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			} else if (blockSpace[x + 2][y].getState() == false && blockSpace[x + 3][y + 2].getState() == false) {
				currentBlock.setState(11);
				currentBlock.BlockColor();
				currentBlock.setBlock();
				currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
				currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
						IntClass.BLOCKSIZE * 4);
			}
			break;
		}
		case 11: {
			if (currentBlock.getPt().x == (IntClass.xNumber - 3) * IntClass.BLOCKSIZE) {
				if (blockSpace[x][y + 1].getState() == false && blockSpace[x][y].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(3);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (blockSpace[x + 1][y + 1].getState() == false && blockSpace[x + 3][y + 1].getState() == false
					&& blockSpace[x + 3][y].getState() == false) {
				currentBlock.setState(3);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}
			break;
		}
		case 12: {
			if (currentBlock.getPt().x == (IntClass.xNumber - 3) * IntClass.BLOCKSIZE) {
				if (blockSpace[x][y + 1].getState() == false && blockSpace[x][y].getState() == false
						&& blockSpace[x + 2][y + 1].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(13);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (blockSpace[x + 2][y + 1].getState() == false && blockSpace[x + 3][y + 1].getState() == false) {
				currentBlock.setState(13);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}
			break;
		}
		case 13: {
			if (blockSpace[x + 1][y + 2].getState() == false && blockSpace[x + 2][y + 2].getState() == false
					&& blockSpace[x + 2][y].getState() == false) {
				currentBlock.setState(14);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}
			break;
		}
		case 14: {
			if (currentBlock.getPt().x == -IntClass.BLOCKSIZE) {
				if (blockSpace[x + 3][y + 1].getState() == false && blockSpace[x + 3][y].getState() == false
						&& blockSpace[x + 1][y].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(4);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (blockSpace[x][y].getState() == false && blockSpace[x + 1][y].getState() == false) {
				currentBlock.setState(4);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}
			break;
		}
		case 15: {
			if (currentBlock.getPt().x == -IntClass.BLOCKSIZE) {
				if (y > 0 && blockSpace[x + 2][y - 1].getState() == false && blockSpace[x + 3][y].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(16);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (blockSpace[x][y].getState() == false) {
				currentBlock.setState(16);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}

			break;
		}
		case 16:// 시작
		{
			if (blockSpace[x + 1][y + 1].getState() == false) {
				currentBlock.setState(17);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}
			break;
		}
		case 17: {
			if (currentBlock.getPt().x == (IntClass.xNumber - 2) * IntClass.BLOCKSIZE) {
				if (blockSpace[x - 1][y].getState() == false && blockSpace[x][y + 1].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					if (y == 1 && blockSpace[x][y - 1].getState() == false
							&& blockSpace[x + 2][y - 1].getState() == false) {
						currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
						currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
								IntClass.BLOCKSIZE * 4);
					}
					currentBlock.setState(5);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (blockSpace[x + 2][y].getState() == false) {
				if (y == 1 && blockSpace[x][y - 1].getState() == false
						&& blockSpace[x + 2][y - 1].getState() == false) {
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
				}
				currentBlock.setState(5);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			}

			break;
		}
		case 18: {
			if (y == 0) {
				if (blockSpace[x + 1][y + 1].getState() == false && blockSpace[x + 1][y + 2].getState() == false
						&& blockSpace[x + 1][y + 3].getState() == false) {
					currentBlock.sety(currentBlock.getPt().y - IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (blockSpace[x + 1][y - 1].getState() == false && blockSpace[x + 1][y + 1].getState() == false
					&& blockSpace[x + 1][y + 2].getState() == false) {
				currentBlock.setState(6);
				currentBlock.BlockColor();
				currentBlock.setBlock();
			} else if (y == 1) {
				if (blockSpace[x][y - 1].getState() == false && blockSpace[x][y + 1].getState() == false
						&& blockSpace[x][y + 2].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x + 2][y - 1].getState() == false && blockSpace[x + 2][y + 1].getState() == false
						&& blockSpace[x + 2][y + 2].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x + 3][y - 1].getState() == false && blockSpace[x + 3][y + 1].getState() == false
						&& blockSpace[x + 3][y + 2].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE * 2);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (y == 2) {
				if (blockSpace[x][y - 2].getState() == false && blockSpace[x][y - 1].getState() == false
						&& blockSpace[x][y + 1].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x + 2][y - 1].getState() == false && blockSpace[x + 2][y - 2].getState() == false
						&& blockSpace[x + 2][y + 1].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x + 3][y - 1].getState() == false && blockSpace[x + 3][y - 2].getState() == false
						&& blockSpace[x + 3][y + 1].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE * 2);
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (y == 3) {
				if (blockSpace[x][y - 3].getState() == false && blockSpace[x][y - 2].getState() == false
						&& blockSpace[x][y - 1].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE * 2);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x + 2][y - 1].getState() == false && blockSpace[x + 2][y - 2].getState() == false
						&& blockSpace[x + 2][y - 3].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE * 2);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x + 3][y - 1].getState() == false && blockSpace[x + 3][y - 2].getState() == false
						&& blockSpace[x + 3][y - 3].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE * 2);
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE * 2);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			} else if (y > 3) {
				if (blockSpace[x][y - 3].getState() == false && blockSpace[x][y - 2].getState() == false
						&& blockSpace[x][y - 1].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE * 2);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x + 2][y - 1].getState() == false && blockSpace[x + 2][y - 2].getState() == false
						&& blockSpace[x + 2][y - 3].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE * 2);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x + 3][y - 1].getState() == false && blockSpace[x + 3][y - 2].getState() == false
						&& blockSpace[x + 3][y - 3].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE * 2);
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE * 2);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x][y - 2].getState() == false && blockSpace[x][y - 1].getState() == false
						&& blockSpace[x][y + 1].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x + 2][y - 1].getState() == false && blockSpace[x + 2][y - 2].getState() == false
						&& blockSpace[x + 2][y + 1].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();

				} else if (blockSpace[x + 3][y - 1].getState() == false && blockSpace[x + 3][y - 2].getState() == false
						&& blockSpace[x + 3][y + 1].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE * 2);
					currentBlock.sety(currentBlock.getPt().y + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x][y - 1].getState() == false && blockSpace[x][y + 1].getState() == false
						&& blockSpace[x][y + 2].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x - IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x + 2][y - 1].getState() == false && blockSpace[x + 2][y + 1].getState() == false
						&& blockSpace[x + 2][y + 2].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				} else if (blockSpace[x + 3][y - 1].getState() == false && blockSpace[x + 3][y + 1].getState() == false
						&& blockSpace[x + 3][y + 2].getState() == false) {
					currentBlock.setx(currentBlock.getPt().x + IntClass.BLOCKSIZE * 2);
					currentBlock.setBounds(currentBlock.getPt().x, currentBlock.getPt().y, IntClass.BLOCKSIZE * 4,
							IntClass.BLOCKSIZE * 4);
					currentBlock.setState(6);
					currentBlock.BlockColor();
					currentBlock.setBlock();
				}
			}
			break;
		}

		}
	}

	public void fillLabel() {
		int y = 0;
		crush = 0;
		switch (currentBlock.getState()) {
		case 0: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(3);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(3);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(3);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(3);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(3);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(3);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(3);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(3);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}

			}
			break;
		}
		case 1: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(5);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(5);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setType(5);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(5);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(5);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setType(5);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(5);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(5);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}

			}
			break;
		}
		case 2: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| (currentBlock.getPt().x / IntClass.BLOCKSIZE >= 0
								&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(7);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(7);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(7);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 2].setType(7);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 2].setImg();
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(7);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(7);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(7);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(7);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}

			}
			break;
		}
		case 3: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}
			}
			break;
		}
		case 4: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}
			}
			break;
		}
		case 5: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}
			}
			break;
		}
		case 6: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState()) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setImg();
					y = i + 3;
					checkBreak(y);

					y = i + 2;
					checkBreak(y);

					y = i + 1;
					checkBreak(y);

					y = i;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][3].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][3].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][3].setImg();
					y = 3;
					checkBreak(y);
					y = 2;
					checkBreak(y);
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}
			}
			break;
		}
		case 7: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i - 1].getState())
						|| i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setType(5);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(5);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(5);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(5);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);
					y = i;
					checkBreak(y);

					i = -1;
				}
			}
			break;
		}
		case 8: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i - 1].getState())
						|| i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setType(7);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(7);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(7);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(7);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);
					y = i;
					checkBreak(y);

					i = -1;
				}
			}
			break;
		}
		case 9: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].getState())) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setImg();
					y = i + 3;
					checkBreak(y);
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
					y = 2;
					checkBreak(y);
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}
			}
			break;
		}
		case 10: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setImg();
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}
			}
			break;
		}
		case 11: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setImg();
					y = i + 3;
					checkBreak(y);
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(9);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					y = 2;
					checkBreak(y);
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);
					;
				}

			}
			break;
		}
		case 12: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setImg();
					y = i + 3;
					checkBreak(y);
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					y = 2;
					checkBreak(y);
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}

			}
			break;
		}
		case 13: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setImg();
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}

			}
			break;
		}
		case 14: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].getState())
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setImg();
					y = i + 3;
					checkBreak(y);
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(11);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					y = 2;
					checkBreak(y);
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}

			}
			break;
		}
		case 15: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if ((i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState())) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);
					y = i;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					y = 2;
					checkBreak(y);
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}

			}
			break;
		}
		case 16: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState())
						|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					y = i + 1;
					checkBreak(y);
					y = i;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}

			}
			break;
		}
		case 17: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState())
						|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					y = i + 2;
					checkBreak(y);
					y = i + 1;
					checkBreak(y);
					y = i;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(13);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					y = 2;
					checkBreak(y);
					y = 1;
					checkBreak(y);
					y = 0;
					checkBreak(y);

				}

			}
			break;
		}
		case 18: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();
					y = i + 1;
					checkBreak(y);

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(true);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(1);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
					y = 0;
					checkBreak(y);

				}

			}
			break;
		}
		}
		throwNetworkData();
	}

	public void initLabel() {
		switch (currentBlock.getState()) {
		case 0: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				}

			}
			break;
		}
		case 1: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				}

			}
			break;
		}
		case 2: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| (currentBlock.getPt().x / IntClass.BLOCKSIZE >= 0
								&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				}

			}
			break;
		}
		case 3: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				}
			}
			break;
		}
		case 4: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				}
			}
			break;
		}
		case 5: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				}
			}
			break;
		}
		case 6: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState()) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][3].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][3].setImg();
				}
			}
			break;
		}
		case 7: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i - 1].getState())
						|| i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					i = -1;
				}
			}
			break;
		}
		case 8: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i - 1].getState())
						|| i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					i = -1;
				}
			}
			break;
		}
		case 9: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
				}
			}
			break;
		}
		case 10: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
				}
			}
			break;
		}
		case 11: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				}

			}
			break;
		}
		case 12: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				}

			}
			break;
		}
		case 13: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
				}

			}
			break;
		}
		case 14: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].getState())
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				}

			}
			break;
		}
		case 15: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if ((i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				}

			}
			break;
		}
		case 16: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState())
						|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					i = -1;
				} else if (i == 1) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				}

			}
			break;
		}
		case 17: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState())
						|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
				}

			}
			break;
		}
		case 18: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(0);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
				}

			}
			break;
		}
		}
	}

	public void shadow() {
		switch (currentBlock.getState()) {
		case 0: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(4);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(4);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(4);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(4);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(4);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(4);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(4);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(4);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				}

			}
			break;
		}
		case 1: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(6);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(6);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setType(6);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(6);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(6);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setType(6);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(6);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(6);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				}

			}
			break;
		}
		case 2: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| (currentBlock.getPt().x / IntClass.BLOCKSIZE >= 0
								&& blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(8);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(8);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(8);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 2].setType(8);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(8);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(8);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(8);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(8);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				}

			}
			break;
		}
		case 3: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				}
			}
			break;
		}
		case 4: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				}
			}
			break;
		}
		case 5: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				}
			}
			break;
		}
		case 6: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState()) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][3].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][3].setImg();
				}
			}
			break;
		}
		case 7: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i - 1].getState())
						|| i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setType(6);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(6);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(6);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(6);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					i = -1;
				}
			}
			break;
		}
		case 8: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i - 1].getState())
						|| i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setType(8);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(8);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(8);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(8);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					i = -1;
				}
			}
			break;
		}
		case 9: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
				}
			}
			break;
		}
		case 10: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {
				if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
				}
			}
			break;
		}
		case 11: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(10);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				}

			}
			break;
		}
		case 12: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
				}

			}
			break;
		}
		case 13: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].getState())) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][1].setImg();
				}

			}
			break;
		}
		case 14: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].getState())
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 2].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 3].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 3].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(12);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
				}

			}
			break;
		}
		case 15: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if ((i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				}

			}
			break;
		}
		case 16: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState())
						|| (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState())
						|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();

					i = -1;
				} else if (i == 1) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][1].setImg();
				}

			}
			break;
		}
		case 17: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if ((blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState())
						|| (i > 0 && blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i - 1].getState())) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 2].setImg();

					i = -1;
				} else if (i == 1) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setType(14);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][2].setImg();
				}

			}
			break;
		}
		case 18: {
			for (int i = 17 - (currentBlock.getPt().y / IntClass.BLOCKSIZE); i >= 0; i--) {

				if (blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i].getState()
						|| blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i].getState()) {

					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][i + 1].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][i + 1].setImg();

					i = -1;
				} else if (i == 0) {
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 1][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 2][0].setImg();
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setState(false);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setType(2);
					blockSpace[currentBlock.getPt().x / IntClass.BLOCKSIZE + 3][0].setImg();
				}

			}
			
			break;
		}
		
		
		}
		
	}

	public class GameoverAnimation extends Thread { // 서브클래스로서, 게임 오버 애니메이션과 후처리 진행 
		public GameoverAnimation() {

		}

		public void run() {
			while (playChecker) // 게임 진행중일 때
			{
				try {
					Thread.sleep(00);
				} catch (InterruptedException e) {
				}
	
				if (!overChecker) // 게임오버 당한경우 
				{
					
					try
					{					
						gameoverClip = AudioSystem.getClip();
						gameoverClip.open(AudioSystem.getAudioInputStream(gameoverSound));
						gameoverClip.start();	
						
					}
					catch(Exception e)
					{
					}
					
					nextBlock_1.setVisible(false);
					nextBlock_2.setVisible(false);
					nextBlock_3.setVisible(false);
					canStart = true;

					// overChecker = false;
					for (int i = 0; i < 20; i++) // 게임오버 애니메이션
					{
						for (int j = 0; j < 10; j++) {
							blockSpace[j][i].setType(15);
							blockSpace[j][i].setImg();
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
					}
					startButton.setVisible(true);
					playChecker = false;
					
				
					gameoverAniThread.interrupt();
					
					
					
					
				}

			}
		}
	}
	

	public void setNetworkChecker(boolean in)
	{
		networkChecker = in;
	
	}

	public boolean getMessageChecker() //서버로 내 블록 상태를 전송할 때 사용하는 함수로, downChcker가 true일 경우 서버로 메세지 전송 
	{
		return messageChecker;
	}
	public void setMessageChecker(boolean in) // 서버로 데이터를 전송하고 나서 downChecker를 false로 변환할 때 사용 
	{
		messageChecker = in;
	}
	public String readBlock() // 블록의 현재 상태를 읽어들이는 메소드 
	{
		String readString = "b "+finalCrush + " ";
		
		for (int i = 0; i < 20; i++) // 게임오버 애니메이션
		{
			for (int j = 0; j < 10; j++) {
				readString = readString + blockSpace[j][i].getType()+" ";
			}
		}
		
		return readString;
		
	}
	
	public int getCrush() // 블록의 파괴 숫자를 파악하기 위한 메소드 
	{
		return finalCrush;
	}
	public void setCrush(int in)
	{
		finalCrush = in;
	}
	public void throwNetworkData() // 서버로 전송할 데이터 모으기 
	{
		if(networkChecker)
		{
			messageChecker = true;
			finalCrush = crush;
			readBlock();
		}
	}
	
	public void crush(int in) // 상대방이 블록을 부쉈을 때 처리 
	{
		int rand;
		rand = (int)Math.random()*10;
	}
	
	public int getMessageType()
	{
		return messageType;
	}
	
	public void setMessageType(int in)
	{
		messageType=  in;
	}
	
	public void win()
	{
		System.out.println("Win!!");

		initGame();
	}
	
	
	public boolean getEndChecker() {
		return endChecker;
	}
	public void setEndChecker(boolean in)
	{
		endChecker = in;
	}
	

}

