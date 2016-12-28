package foreground;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.stream.Collector.Characteristics;

import javax.swing.JPanel;

import Sprite.AnimationPlayer;
import character.Character;

public class AnimationPanel extends JPanel{
    
    ArrayList<AnimationPlayer> playerAnimation = new ArrayList<AnimationPlayer>();
    
    int ID;
    Character user;    
    Character[] characters;
    static final int mapSize = 60;
    static final int offsetBlock = 4;
    static final int offset = mapSize * offsetBlock;
    public AnimationPanel(character.Character[] characters,int ID) {
        setLayout(null);
        setBounds(0, 0, 540, 540);
        setFocusable(true);
        addAllPlayer(characters);
        this.characters = characters;
        //use server viewer as init viewer. 
        user = characters[1];

        this.ID = ID;
    }
    @Override
    public void paint(Graphics g) {
        //g.clearRect(0, 0, 540, 540);
        //System.out.println("~~~~AAA~~~~" + playerAnimation.size() + ID+perCoordinate.length);
        for (int i = 0; i < playerAnimation.size(); i++) {
            int x = characters[i].getCoordinate().x - user.getCoordinate().x;
            int y = characters[i].getCoordinate().y - user.getCoordinate().y;
            
            //align from the bottom left corner of pic.
            g.drawImage(
                playerAnimation.get(i).getSprite(),
                x +offset - Character.imgWidth/4 + mapSize,
                y +offset - Character.imgHeight/4 + mapSize,
                Character.imgWidth/4, Character.imgHeight/4, null);
        }
    }
    public void addAllPlayer(Character[] characters) {
        for(int i = 0;i<characters.length;i++)
            playerAnimation.add(new AnimationPlayer(characters[i],i));
    }
    public void update(Character[] characters, Character me) {
        this.user = me;
      //System.out.println("~~~~AAA~~~~" + playerAnimation.size() + ID+perCoordinate.length +characters.length );
        for (int i = 0; i < playerAnimation.size(); i++) {
            //切換圖片                      
                        //System.out.println(i+" "+displacement);
            playerAnimation.get(i).animationMove( characters[i].getDisplacement());           
        }
        repaint();
    }
}
