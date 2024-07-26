package blockWorld;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;

public class Table extends JPanel implements Runnable {

    @Serial
    private static final long serialVersionUID = 1L;
    // We define the blocks on the table as a list of stacks(since the blocks are a stack in java)
    List<Stack<Block>> blocks = new ArrayList<>();
    List<Stack<Block>> result = new ArrayList<>();

    boolean ARMEMPTY = true;
    boolean HOLD = false;
    Block pickedUpBlock;
    List<Plan> plan;
    int blocksNumber;
    int spacing;
    int windowSize = 500;
    final String ON = "ON";
    final String ONTABLE = "ONTABLE";

    public Table(List<String> commands, List<String> commandsResult) {
        // To initialize the table we must go through all the commands from the last one to the first one(last one being the on the table blocks)
        for(int i=commands.size() - 1; i >= 0; i --) {
            // We get the command
            String command = commands.get(i);
            // We check first if the command is ONTABLE
            if(command.contains(ONTABLE)) {
                applyOnTable(command, false);
                this.blocksNumber +=1;
            }
            // Now for the next command ON
            else if(command.contains(ON)) {
                applyOn(command, false);
                this.blocksNumber +=1;
            }
        }

        for(int i=commandsResult.size() - 1; i >= 0; i --) {
            // We get the command
            String command = commandsResult.get(i);
            // We check first if the command is ONTABLE
            if(command.contains(ONTABLE)) {
                applyOnTable(command, true);
            }
            // Now for the next command ON
            else if(command.contains(ON)) {
                applyOn(command, true);
            }
        }
        System.out.println(this.blocksNumber);
        this.spacing = this.windowSize / this.blocksNumber;
        Collections.reverse(this.blocks);
        Collections.reverse(this.result);
        this.plan = new ArrayList<>();
        new Thread(this).start();

    }

    private void applyOn(String command, boolean result) {
        // We parse the command to only get the blocks
        command = command.substring(command.indexOf("(") + 1);
        command = command.substring(0, command.indexOf(")"));
        // We define what block to search for in the list of stacks
        // The first block on the stack should always be the one we are looking for.
        Block blockToSearch = new Block(Character.toString(command.charAt(2)));
        Block blockToStack =  new Block(Character.toString(command.charAt(0)));
        if (!result) {
            this.blocks = this.stack(blockToSearch, blockToStack, this.blocks);
        }
        else {
            // Looping through all the stacks for the result to see which one contains our initial block
            for(int j=0; j<this.result.size(); j++) {
                // See if the last block in the stack has the name we are looking for
                if(this.result.get(j).get(this.result.get(j).size() - 1).name.equals(blockToSearch.name)) {
                    //If it does, create a block object and add it to the stack
                    this.result.get(j).add(new Block(blockToStack.name));
                }
            }
        }
    }

    private void applyOnTable(String command, boolean result) {
        // We parse the command to only get the block
        command = command.substring(command.indexOf("(") + 1);
        command = command.substring(0, command.indexOf(")"));
        // We create a new block with the name of the block
        Block block = new Block(command);
        // We add the block to the stack
        if(!result) {
            this.pickedUpBlock = block;
            // Being on the table it means that we need to create a new stack since this is the first block in it for which we use the putDown method;
            this.blocks = this.pickUp(this.blocks);
            System.out.println("Block was added, list is now: " + this.blocks.toString());
        }
        else {
            Stack<Block> stack = new Stack<>();
            stack.add(block);
            this.result.add(stack);
        }
    }

    public void createPlan() {
        // Now we are just creating the plan so, we have to wait for the blocks to be drawn initially
        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // First de-stack everything
        for(int i=0; i<this.blocks.size(); i++) {
            Stack<Block> stack = this.blocks.get(i);
            for(int j=stack.size() - 1; j>=2; j--) {
                Block block = stack.get(j);
                this.plan.add(new Plan("UNSTACK", block));
            }
        }
        //Now we go through the result list
        for(int i=0; i<this.result.size(); i++) {
            // We now get every block starting with the second one in length
            for(int j=1; j<this.result.get(i).size(); j++) {
                Block block = this.result.get(i).get(j);
                // We now add to the plan a putDown because all the blocks are unstacked on the table
                this.plan.add(new Plan("PUTDOWN", block));
                this.plan.add(new Plan("PUTDOWN", this.result.get(i).get(j-1), block));
                this.plan.add(new Plan("PICKUP", block));
                this.plan.add(new Plan("STACK", this.result.get(i).get(j-1), block));
            }
        }
    }

    private String getPlan1String() {
        StringBuilder planString = new StringBuilder();
        for(int i=0; i<this.plan.size(); i++) {
            planString.append(this.plan.get(i).getPlan1String());
        }
        return planString.toString();
    }

    private String getPlan2String() {
        StringBuilder planString = new StringBuilder();
        for(int i=0; i<this.plan.size() - 2; i++) {
            planString.append(this.plan.get(i).getPlan2String());
        }
        return planString.toString();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.drawGrid(g);
    }

    public void update() {
        // Wait for 1100 seconds to make sure program is running properly
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
    }

    public void drawGrid(Graphics g) {
        for(int i=0; i<this.blocks.size(); i++) {
            for(int j=0; j<this.blocks.get(i).size(); j++) {
                Block block = this.blocks.get(i).get(j);
                g.setColor(block.color);
                g.fillRect(i*this.spacing + 40, this.windowSize-j*this.spacing - this.spacing, this.spacing, this.spacing);
                g.setColor(Color.black);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
                g.drawString(block.name, i*this.spacing + this.spacing, this.windowSize-j*this.spacing - this.spacing + this.spacing/2);
            }
        }
        // Draw the table
        g.setColor(Color.cyan);
        g.fillRect(40, this.windowSize, this.windowSize + 100, 40);
        //Draw the table's legs
        g.fillRect(60, this.windowSize + 30, 30, 720 - this.windowSize);
        g.fillRect(this.windowSize +80, this.windowSize + 30, 30, 720 - this.windowSize);
        //Style the text
        g.setColor(Color.black);
        if(this.plan.size() > 0)
        {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 14));
            g.drawString("Plan1: " + this.getPlan1String(), 40, this.windowSize - 450);
            g.drawString("Plan2: " + this.getPlan2String(), 40, this.windowSize - 430);
        }
    }

    public  List<Stack<Block>> stack(Block blockOn, Block blockTo, List<Stack<Block>> blocksList) {
        this.ARMEMPTY = true;
        this.HOLD = false;
        // Looping through all the stacks for the initial state to see which one contains our base block
        for(int j=0; j<blocksList.size(); j++) {
            Stack<Block> stack = blocksList.get(j);
            if(stack.size() == 0) {
                continue;
            }
            // See if the last block in the stack has the name we are looking for
            if(stack.get(stack.size() - 1).name.equals(blockOn.name)) {
                //If it does, create a block object and add it to the stack
                blocksList.get(j).add(blockTo);
                break;
            }
        }
        return blocksList;
    }

    private void emptyStackOrNewStack(Block block, List<Stack<Block>> blocksList) {
        for(int j=0; j < blocksList.size(); j++) {
            if(blocksList.get(j).size() == 0) {
                blocksList.get(j).add(block);
                return;
            }
        }
        Stack<Block> stack = new Stack<>();
        stack.add(block);
        blocksList.add(stack);
    }

    public List<Stack<Block>> unStack(Block block,  List<Stack<Block>> blocksList) {
        this.ARMEMPTY = true;
        this.HOLD = false;
        for(int j=0; j < blocksList.size(); j++) {
            Stack<Block> stack = blocksList.get(j);
            if(stack.size() <= 1) {
                continue;
            }
            Block blockTo = stack.get(stack.size() - 1);
            if(blockTo.name.equals(block.name)) {

                this.emptyStackOrNewStack(blockTo, blocksList);
                stack.pop();

            }
        }
        return blocksList;
    }

    public void putDown(Block block) {
        //Check if the robot arm is holding a block,if it is empty pick up the block and store the block object
        this.ARMEMPTY = false;
        this.HOLD = true;
        for(int i=0; i<this.blocks.size();i++) {
            Stack<Block> stack = this.blocks.get(i);
            if(stack.size() >0 && stack.get(stack.size() - 1).name.equals(block.name)) {
                this.pickedUpBlock = stack.pop();
                break;
            }
        }
    }

    public List<Stack<Block>> pickUp(List<Stack<Block>> blocksList) {
        //Check if the robot arm is holding a block, if it isn't empty put the block down
        this.ARMEMPTY = true;
        this.HOLD = false;
        Stack<Block> stack = new Stack<>();
        stack.add(this.pickedUpBlock);
        blocksList.add(stack);
        return blocksList;
    }

    public void Plan() {
        System.out.println("The Stack is now: " + this.blocks.toString() );
        System.out.println("Plan1: " + this.getPlan1String());
        System.out.println("Plan2: " + this.getPlan2String());
        for(int i=0; i<this.plan.size(); i++) {
            Plan plan = this.plan.get(i);
            System.out.println(plan.getPlan1String());
            System.out.println(plan.getPlan2String());
            switch (plan.method) {
                case "UNSTACK" -> {
                    this.blocks = this.unStack(plan.blockTo, this.blocks);
                    update();
                }
                case "PUTDOWN" -> this.putDown(plan.blockTo);
                case "STACK" -> this.blocks = this.stack(plan.blockOn, plan.blockTo, this.blocks);
            }
            update();
        }

    }

    @Override
    public void run() {
        this.createPlan();
        this.Plan();
        update();
    }

}
