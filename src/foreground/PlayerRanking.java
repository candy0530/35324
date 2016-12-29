package foreground;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import data.Player;
import data.PlayerInfo;

public class PlayerRanking extends JPanel{
    private PlayerInfo playerInfo;
    private int playerWin = 0;
    private JLabel[] burgerNum;
    private int ghostID = 8;
//  table cell style
    private final int crownHeight = 40;
    private int crownWidth;
    private int nameHeight ;
    private final int nameWidth = 150;
    private int burgerHeight;
    private int burgerWidth;
    private int[][] PlayerListColorTable = { { 255, 255, 255 }, { 255, 0, 0 }, { 255, 165, 0 }, { 255, 255, 0 },
            { 0, 255, 0 }, { 0, 127, 255 }, { 0, 0, 255 }, { 139, 0, 255 } };
 
    private final Font fontStyle = new Font("Serif", Font.BOLD, 20);
    private final Border borderStyle = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED); 
    private JLabel[] winner;
    private JPanel[] showPlayerRank;
    
    private ImageIcon crownIcon = new ImageIcon(new ImageIcon("image/Ranking/winner.png").getImage().getScaledInstance(crownHeight, crownHeight, Image.SCALE_DEFAULT));
    private ImageIcon ghostIcon = new ImageIcon(new ImageIcon("image/Ranking/ghost.png").getImage().getScaledInstance(crownHeight, crownHeight, Image.SCALE_DEFAULT));
    
    public PlayerRanking(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        crownWidth = crownHeight;
        nameHeight = crownHeight;
        burgerHeight = crownHeight;
        burgerWidth = crownHeight;
        
//        Player player = new Player("Tsest", "123456");
//        player.setBurger(10);
//        this.playerInfo.add(player);
        
//        setPicture();
        setLayout(null);        
        showPlayerRank = new JPanel[7];  
        
        //setting Background
        for(int i=0; i<showPlayerRank.length; i++) {
            showPlayerRank[i] = new JPanel();
            showPlayerRank[i].setLayout(null);
            showPlayerRank[i].setBackground(new java.awt.Color(PlayerListColorTable[i+1][0],
                  PlayerListColorTable[i+1][1], PlayerListColorTable[i+1][2]));
            showPlayerRank[i].setBorder(borderStyle);
            showPlayerRank[i].setLocation(0, i * crownHeight);
            showPlayerRank[i].setSize(crownWidth + nameWidth + burgerWidth, crownHeight);
            add(showPlayerRank[i]);
        }
        
        //set the winner
        winner = new JLabel[this.playerInfo.size()];        
        for(int i=0 ; i<playerInfo.size(); i++) {
            winner[i] = new JLabel();     
            winner[i].setSize(crownWidth, crownHeight);
            winner[i].setLocation(0, 0);
            winner[i].setOpaque(false);
            winner[i].setBackground(new Color(PlayerListColorTable[i+1][0], PlayerListColorTable[i+1][1], PlayerListColorTable[i+1][2]));
//            winner[i].setBorder(borderStyle);
            showPlayerRank[i].add(winner[i]);
        }
        
        
        //set the player name
        JLabel[] userID = new JLabel[this.playerInfo.size()];        
        for(int i=0 ; i<this.playerInfo.size(); i++) {
            userID[i] = new JLabel();
            userID[i].setHorizontalAlignment(SwingConstants.LEFT);
            userID[i].setText(this.playerInfo.get(i).getName());
            userID[i].setFont(fontStyle);
            userID[i].setBorder(borderStyle);
            userID[i].setSize(nameWidth, nameHeight);
            userID[i].setLocation(crownWidth, 0);
            userID[i].setForeground(new Color(255-PlayerListColorTable[i+1][0], 255-PlayerListColorTable[i+1][1], 255-PlayerListColorTable[i+1][2]));
            userID[i].setBackground(new Color(PlayerListColorTable[i+1][0], PlayerListColorTable[i+1][1], PlayerListColorTable[i+1][2]));
            userID[i].setOpaque(true);
            showPlayerRank[i].add(userID[i]);
        }
        
        
        //set the burger number
        burgerNum = new JLabel[this.playerInfo.size()];        
        for(int i=0 ; i<this.playerInfo.size(); i++) {
            burgerNum[i] = new JLabel();
            burgerNum[i].setHorizontalAlignment(SwingConstants.CENTER);
            burgerNum[i].setText(Integer.toString(this.playerInfo.get(i).getBurger()));
            burgerNum[i].setFont(fontStyle);
            burgerNum[i].setBorder(borderStyle);
            burgerNum[i].setSize(burgerWidth, burgerHeight);
            burgerNum[i].setLocation(crownWidth + nameWidth, 0);
            burgerNum[i].setForeground(new Color(255-PlayerListColorTable[i+1][0], 255-PlayerListColorTable[i+1][1], 255-PlayerListColorTable[i+1][2]));
            burgerNum[i].setBackground(new Color(PlayerListColorTable[i+1][0], PlayerListColorTable[i+1][1], PlayerListColorTable[i+1][2]));
            burgerNum[i].setOpaque(true);
            showPlayerRank[i].add(burgerNum[i]);
        }
        

        
        
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        
        playerWin = 0;
        for(int i=1; i<this.playerInfo.size(); i++) {
            if(this.playerInfo.get(playerWin).getBurger() < this.playerInfo.get(i).getBurger()){
                playerWin = i;
            }
        }
        
        for(int i=0 ; i<playerInfo.size(); i++) {
            showPlayerRank[i].remove(winner[i]);
            if(i == playerWin ) {
                winner[i].setIcon(crownIcon);
            }
            else {
                winner[i].setIcon(null);
            }
            if(i == this.ghostID) {
                
                winner[this.ghostID].setIcon(ghostIcon);            
            }
            showPlayerRank[i].add(winner[i]);
        }
        
        for(int i=0; i<playerInfo.size(); i++) {
            burgerNum[i].setText(playerInfo.get(i).getBurger()+"");
        }
        
    }   
    
    public void setGhostID(int ghostID) {
        this.ghostID = ghostID;
    }
   
}
