package blockWorld;
import java.util.Random;
import java.awt.Color;
public class Block {
    String name;
    Color color;

    public Block(String name) {
        this.name = name;
        this.setColor();
    }

    protected void setColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        this.color = randomColor;
    }
}
