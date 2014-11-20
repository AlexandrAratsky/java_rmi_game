package game.client.gui.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import game.enigne.Side;
import game.server.GameServer;

import javax.swing.JPanel;

public class JBoardArea extends JPanel {

	private static final long serialVersionUID = -7855104718557647476L;

	public static class Options {
		public static final int PREF_W = 600;
	   	public static final int PREF_H = 400;
	   	public static final int PREF_TICKNESS = 5;
	   	public static final float PREF_COEF = 0.8F;
	   	private static float coefSize = PREF_COEF;
	   	private static int tickness = PREF_TICKNESS;	
	   	
	   	private static boolean assistant = false;
	   	private static boolean debug = false;
	}
	
   	private GameServer board;
	private float offsetX, offsetY, cellSize;
	private Point current = null;
	private Side side = null;
	private boolean hand = false;
	
	public JBoardArea(GameServer gameBoard) {
		setFocusable(true);
        this.board = gameBoard;
        setEnabled(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	try {
					cellSize = Math.min((float)(getWidth()*Options.coefSize)/board.getWidht(), 
							(float)(getHeight()*Options.coefSize)/board.getHeight());
					offsetX = (getWidth()-board.getWidht()*cellSize)/2;
					offsetY = (getHeight()-board.getHeight()*cellSize)/2;
				} catch (RemoteException e1) { e1.printStackTrace(); }
        		repaint();
            }

         });
        addMouseMotionListener(new MouseAdapter() {
       	 @Override
       	 public void mouseMoved(MouseEvent e) {
       		 if (isEnabled() && inArea(e.getX(), e.getY())) // && !board.isEnd())
       		 {
       			 current = getCell(e.getX(),e.getY());
       			 float X = e.getX() - offsetX - current.x*cellSize;
       			 float Y = e.getY() - offsetY - current.y*cellSize;
       			 try {
					if (!board.getCellWall(current.x, current.y, getSide(X, Y)).getState())
					 {
						side = getSide(X, Y);
						if (changeCursor(X, Y))
							{
								setCursor(new Cursor(Cursor.HAND_CURSOR));
								hand = true;
							}
						 	else 
						 		{
						 			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						 			hand = false;
						 		}
					 } 
					 else side = null;
				} catch (RemoteException e1) { e1.printStackTrace(); }
       			 repaint();
       		 }
       		 else
       		 {
       			 current = null;
       			 side = null;
       			 repaint();
       		 }
       	 }
		});
        addMouseListener(new MouseAdapter() {
       	 @Override
       	 public void mouseClicked(MouseEvent e) {
       		 if(isEnabled() && inArea(e.getX(), e.getY()) && hand) // && !board.isEnd())
       			{
       			 try {
					board.cellClick(current.x, current.y, side);
				} catch (RemoteException e1) { e1.printStackTrace(); }
       			 repaint();
       			}
       	 }
		});
	
	}
	
	private boolean inArea(int x, int y) {
  		if (x>offsetX && x<getWidth()-offsetX && y>offsetY && y<getHeight()-offsetY) return true;
  		else return false;
  	}
	private boolean changeCursor(float X,float Y) {
    	if (X<cellSize*0.1 || X>cellSize*0.9 || Y<cellSize*0.1 || Y>cellSize*0.9) 
    		return true;
    	else return false;
    }
	private Side getSide(float X, float Y) {
  		if (Y>X)
  			if (cellSize-X>Y) return Side.left;
  			else return Side.bottom;
  		else
  			if (cellSize-X>Y) return Side.top;
  			else return Side.right;
  		
  	} 
	public Point getCell(int x, int y) {
		return new Point((int)(Math.floor((x-offsetX))/cellSize),(int)(Math.floor((y-offsetY))/cellSize));
	}
	
	private void drawLine(int i,int j, Side s, Graphics2D g2d) {
 		switch(s) {
  		case bottom:
  			g2d.drawLine((int)(offsetX+i*cellSize), (int)(offsetY+(j+1)*cellSize), 
  					(int)(offsetX+(i+1)*cellSize), (int)(offsetY+(j+1)*cellSize));
  			break;
  		case left:
  			g2d.drawLine((int)(offsetX+i*cellSize), (int)(offsetY+j*cellSize), 
  					(int)(offsetX+i*cellSize), (int)(offsetY+(j+1)*cellSize));
  			break;
  		case right:
  			g2d.drawLine((int)(offsetX+(i+1)*cellSize), (int)(offsetY+j*cellSize), 
  					(int)(offsetX+(i+1)*cellSize), (int)(offsetY+(j+1)*cellSize));
  			break;
  		case top:
  			g2d.drawLine((int)(offsetX+i*cellSize), (int)(offsetY+j*cellSize), 
  					(int)(offsetX+(i+1)*cellSize), (int)(offsetY+j*cellSize));
  			break;
  		}
    }
	private void drawWalls(Graphics2D g2d) {
    	g2d.setStroke(new BasicStroke(Options.tickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
    	  try {
			for(int i=0;i<board.getWidht();i++)
					for(int j=0;j<board.getHeight();j++)
				{
					if (board.getCellWall(i, j, Side.top).getState()) drawLine(i, j, Side.top, g2d); 
					if (board.getCellWall(i, j, Side.bottom).getState()) drawLine(i, j, Side.bottom, g2d); 
					if (board.getCellWall(i, j, Side.left).getState()) drawLine(i, j, Side.left, g2d); 
					if (board.getCellWall(i, j, Side.right).getState()) drawLine(i, j, Side.right, g2d); 
				}
		} catch (RemoteException e) { e.printStackTrace(); }
  	  }
	private void drawCellInfo(Graphics2D g2d) {
  		try {
			for(int i=0;i<board.getWidht();i++)
				for(int j=0;j<board.getHeight();j++)
					{
						g2d.drawString(board.getCellInfo(i, j), (int)(offsetX+i*cellSize)+2, (int)(offsetY+(j+1)*cellSize)-3);
						g2d.drawString(board.getCellPlayerInfo(i, j), 
						(int)(offsetX+i*cellSize+0.2*cellSize), (int)(offsetY+j*cellSize+0.4*cellSize));
					}
		} catch (RemoteException e) { e.printStackTrace(); }
  				
	}
	private void drawWallsInfo(Graphics2D g2d) {
  		try {
			for(int i=0;i<board.getWidht();i++)
				for(int j=0;j<board.getHeight();j++)
				{
					float x = offsetX+i*cellSize;
					float y = offsetY+j*cellSize;
					g2d.drawString(board.getCellWall(i, j, Side.top).getState()?"--":"", x+cellSize/2, y+10);
					g2d.drawString(board.getCellWall(i, j, Side.left).getState()?"|":"", x+5, y+cellSize/2);
					g2d.drawString(board.getCellWall(i, j, Side.bottom).getState()?"--":"", x+cellSize/2, y+cellSize-6);
					g2d.drawString(board.getCellWall(i, j, Side.right).getState()?"|":"", x+cellSize-10, y+cellSize/2);
				}
		} catch (RemoteException e) { e.printStackTrace(); }
	}
	private void draw1Player(int i, int j,Graphics2D g2d) {
    	g2d.setStroke(new BasicStroke(2*Options.tickness, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND));
    	g2d.setColor(Color.GREEN);
    	g2d.drawLine((int)(offsetX+i*cellSize+cellSize*0.3), (int)(offsetY+j*cellSize+cellSize*0.7), 
    			(int)(offsetX+i*cellSize+cellSize*0.7), (int)(offsetY+j*cellSize+cellSize*0.3));
    	g2d.drawLine((int)(offsetX+i*cellSize+cellSize*0.3), (int)(offsetY+j*cellSize+cellSize*0.3), 
    			(int)(offsetX+i*cellSize+cellSize*0.7), (int)(offsetY+j*cellSize+cellSize*0.7));
    	g2d.setColor(Color.BLACK);
    }
    private void draw2Player(int i, int j,Graphics2D g2d) {
    	g2d.setStroke(new BasicStroke(2*Options.tickness, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND));
    	g2d.setColor(Color.RED);
    	g2d.drawOval((int)(offsetX+i*cellSize+cellSize*0.3), (int)(offsetY+j*cellSize+cellSize*0.3), 
    			(int)(cellSize*0.4), (int)(cellSize*0.4));
    	g2d.setColor(Color.BLACK);
    }
    private void draw3Player(int i, int j,Graphics2D g2d) {
    	g2d.setStroke(new BasicStroke(2*Options.tickness, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND));
    	g2d.setColor(Color.BLUE);
    	int xP[] = {(int)(offsetX+i*cellSize+cellSize*0.5),(int)(offsetX+i*cellSize+cellSize*0.7),(int)(offsetX+i*cellSize+cellSize*0.3)};
    	int yP[] = {(int)(offsetY+j*cellSize+cellSize*0.3),(int)(offsetY+j*cellSize+cellSize*0.7),(int)(offsetY+j*cellSize+cellSize*0.7)};
    	g2d.drawPolygon(xP, yP, 3);
    	g2d.setColor(Color.BLACK);
    }
    private void draw4Player(int i, int j,Graphics2D g2d) {
    	g2d.setStroke(new BasicStroke(2*Options.tickness, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND));
    	g2d.setColor(Color.ORANGE);
    	g2d.drawRect((int)(offsetX+i*cellSize+cellSize*0.3), (int)(offsetY+j*cellSize+cellSize*0.3), 
    			(int)(cellSize*0.4), (int)(cellSize*0.4));
    	g2d.setColor(Color.BLACK);
    }
    private void drawPlayers(Graphics2D g2d) {
    	try {
			for(int i=0;i<board.getWidht();i++)
				for(int j=0;j<board.getHeight();j++)
					if (board.getCellPlayer(i, j)!=null)
					{
						switch (board.getCellPlayer(i, j).getID()) {
						case 1:
							draw1Player(i, j, g2d);
							break;
						case 2:
							draw2Player(i, j, g2d);
							break;
						case 3:
							draw3Player(i, j, g2d);
							break;
						case 4:
							draw4Player(i, j, g2d);
							break;
						default:
							break;
						}
					}
		} catch (RemoteException e) { e.printStackTrace(); }
    }	   
        
	private void selecter(Graphics2D g2d) {
    	g2d.setColor(Color.BLACK);
 		g2d.setStroke(new BasicStroke(Options.tickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
 		drawLine(current.x, current.y, side, g2d);
    }
	private void assistant(Graphics2D g2d) {
    	if (current!=null)
 		{
 			g2d.setColor(new Color(0.95F,0.95F,0.95F,0.5F));
 			g2d.fillRoundRect((int)(offsetX+current.x*cellSize+cellSize*0.1) , (int)(offsetY+current.y*cellSize+cellSize*0.1), 
 				(int)(cellSize*0.8), (int)(cellSize*0.8), (int)(cellSize*0.3), (int)(cellSize*0.3));
 			g2d.setColor(Color.LIGHT_GRAY);
 			g2d.drawString(side!=null?Side.toString(side):"---", 
 					(int)(offsetX+current.x*cellSize+cellSize*0.2), (int)(offsetY+current.y*cellSize+cellSize*0.4));
 			try {
				g2d.drawString(board.getCellInfo(current.x, current.y), 
						(int)(offsetX+current.x*cellSize+cellSize*0.2), (int)(offsetY+current.y*cellSize+cellSize*0.6));
				g2d.drawString(board.getCellPlayerInfo(current.x, current.y), 
						(int)(offsetX+current.x*cellSize+cellSize*0.2), (int)(offsetY+current.y*cellSize+cellSize*0.8));
			} catch (RemoteException e) { e.printStackTrace(); }
 		}
    }
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 		
 		g2d.setColor(Color.WHITE); 
 		g2d.fillRect((int)(offsetX), (int)(offsetY), (int)(getWidth()- 2*offsetX), (int)(getHeight()- 2*offsetY));
 			
 		g2d.setColor(Color.BLACK);
 		g2d.setStroke(new BasicStroke(1.0f));
 		try {
			for(int i=0;i<=board.getWidht();i++)
				g2d.drawLine((int)(offsetX+i*cellSize), (int)(offsetY), 
						(int)(offsetX+i*cellSize), (int)(getHeight()-offsetY-1));
			for(int j=0;j<=board.getHeight();j++) 
				g2d.drawLine((int)(offsetX), (int)(offsetY+j*cellSize), 
						(int)(getWidth()-offsetX-1), (int)(offsetY+j*cellSize));
		} catch (RemoteException e) { e.printStackTrace(); }

 		if (Options.debug) {
 			drawWallsInfo(g2d);
 			drawCellInfo(g2d);
 		} 
 		else {
 			drawWalls(g2d);
 			drawPlayers(g2d);
 		}
 		if (Options.assistant) assistant(g2d);
 		if (current!=null && side!=null && hand) selecter(g2d);
  }
	
  @Override
  	public Dimension getPreferredSize() {
       return new Dimension(Options.PREF_W, Options.PREF_H);
    }


	public float getCoefSize() {
		return Options.coefSize;
	}
	public void setCoefSize(float coefSize) {
		Options.coefSize = coefSize;
		try {
			cellSize = Math.min((float)(getWidth()*coefSize)/board.getWidht(), 
					(float)(getHeight()*coefSize)/board.getHeight());
			offsetX = (getWidth()-board.getWidht()*cellSize)/2;
			offsetY = (getHeight()-board.getHeight()*cellSize)/2;
		} catch (RemoteException e) { e.printStackTrace(); }
		repaint();
	}
	public int getTickness() {
		return Options.tickness;
	}
	public void setTickness(int tickness) {
		Options.tickness = tickness;
		repaint();
	}
	public boolean isAssistant() {
		return Options.assistant;
	}
	public void setAssistant(boolean assistant) {
		Options.assistant = assistant;
		repaint();
	}
	public boolean isDebug() {
		return Options.debug;
	}
	public void setDebug(boolean debug) {
		Options.debug = debug;
		repaint();
	}
	
	public void on() {
		setEnabled(true);
	}
	public void off() {
		setEnabled(false);
	}

	public void endGame() {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setEnabled(false);
	}
}
