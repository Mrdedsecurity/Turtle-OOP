import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import uk.ac.leedsbeckett.oop.LBUGraphics;

import static java.lang.Integer.parseInt;

public class GraphicsSystem extends LBUGraphics
{

    public GraphicsSystem()
    {
        JFrame TurtleFrame = new JFrame();                //Creates frame for the turtle to be displayed on
        TurtleFrame.setLayout(new FlowLayout());      //Straight forward sets the frame's layout to FlowLayout (not necessary)
        TurtleFrame.add(this);                                        //"this" Is an object extending turtle graphics and we're adding a panel to the frame
        TurtleFrame.pack();                                           //Resizes the frame/window to optimal size user is able to see
        TurtleFrame.setVisible(true);                         //Shows the frame/window
        penDown();
        turnLeft();
        setBackground_Col(Color.black);
    }

    public static void main(String[] args)
    {
        new GraphicsSystem(); //Creates new instance of the class extended by LBUGraphics
    }

    @Override
    public void about()																					//Overrides command to display my name whilst still making the turtle "dance"
    {
        super.about();
        displayMessage("Jordan Powell");
    }

    @Override
    public void circle(int radius)																		//Overrides command to draw a circle with specified radius
    {
        getGraphics2DContext().drawOval(getxPos() - radius, getyPos() - radius, 2*radius, 2*radius );
    }

    public void triangle(int length)
    {
        getGraphics2DContext().drawPolygon(new int[]{getxPos() - (length / 2), getxPos(), getxPos() + (length / 2) }, new int[]{getyPos(), (int)(getyPos() - (length * (Math.sqrt(3)/2))), getyPos()}, 3);
    }

    public void RGB(int redValue, int greenValue, int blueValue)
    {
        Color rgbColour = new Color(redValue, greenValue, blueValue);
        setPenColour(rgbColour);
    }

    private ArrayList<String> commands = new ArrayList<String>();
    private boolean writeCommand = true;
    private boolean saved = false;
    private boolean saveWarn = false;


    public void processCommand(String command)      //This method must be provided because LBUGraphics will call it when it's JTextField is used
    {

        //String parameter is the text typed into the LBUGraphics JTextfield
        //Processed after return or the okay button has been used
        boolean invalid = false;

        String[] commandsEntered = command.toLowerCase().split(" ");									//Makes all commands entered lower-case and splits at space (" ") for commands with values
        if(commandsEntered[0].matches("forward|backward|save|load|savecommands|loadcommands" ))		//this allows for processing of those values by the program
        {
            if(commandsEntered.length!=2)
            {
                displayMessage("Please input a valid value!");
                invalid = true;
            }

            else if(commandsEntered[0].matches("forward|backward" ))
            {
                try
                {
                    int distance = parseInt(commandsEntered[1]);
                    if(distance<0||distance>1000)
                    {
                        displayMessage("Please enter value 1-1000");
                        invalid = true;
                    }
                }

                catch(Exception e)
                {
                    displayMessage("Please enter a valid value");
                    invalid = true;
                }
            }
        }

        if(!invalid)
        {

            switch (commandsEntered[0])
            {
                case "penup":
                    penUp();
                    displayMessage("Pen's up turtle won't draw");
                    break;

                case "pendown":
                    penDown();
                    displayMessage("Pen's down Turtle will draw");
                    break;

                case "forward":
                    forward(parseInt(commandsEntered[1]));
                    displayMessage("Turtle moved forwards");
                    break;

                case "backward":
                    forward((parseInt(commandsEntered[1])*-1));
                    displayMessage("Turtle moved backwards");
                    break;

                case "turnleft":
                    if (commandsEntered.length != 2)
                    {
                        turnLeft();
                        displayMessage("Turtle turned left!");
                    }

                    else
                    {
                        turnLeft(parseInt(commandsEntered[1]));
                        displayMessage("Command successful");
                    }
                    break;

                case "turnright":
                    if (commandsEntered.length != 2)
                    {
                        turnRight();
                        displayMessage("Turtle turned right!");
                    }

                    else

                    {
                        turnRight(parseInt(commandsEntered[1]));
                        displayMessage("Command successful");
                    }
                    break;

                case "about":
                    about();
                    break;

                case "circle":
                    circle(parseInt(commandsEntered[1]));
                    displayMessage("Circle drawn with radius given");
                    break;

                case "triangle":
                    triangle(parseInt(commandsEntered[1]));
                    displayMessage("Triangle drawn with side length given");
                    break;

                case "save":

                    try
                    {
                        BufferedImage bufferedImage = getBufferedImage();
                        File outputfile = new File(commandsEntered[1] + ".png");
                        ImageIO.write(bufferedImage, "png", outputfile);
                        saved = true;
                        displayMessage("Command was successful");
                    }

                    catch (IOException e)
                    {
                        displayMessage("SaveImage Error");
                    }
                    break;

                case "load":
                    if(saved)
                    {
                        try
                        {
                            File inputfile = new File(commandsEntered[1] + ".png");
                            BufferedImage bufferedImage = ImageIO.read(inputfile);
                            setBufferedImage(bufferedImage);
                            displayMessage("Command was successful");
                        }

                        catch (IOException e)
                        {
                            displayMessage("Load Failed: File does not exist");
                        }
                    }

                    else
                    {
                        displayMessage("Warning: Current Image/commands never been saved!");
                        saveWarn = true;
                    }
                    break;

                case "savecommand":

                    try
                    {
                        FileWriter myWriter = new FileWriter(commandsEntered[1] + ".txt");
                        for(String str: commands)
                        {
                            myWriter.write(str + System.lineSeparator());
                        }

                        myWriter.close();
                        saved = true;
                        displayMessage("Command was successful");

                    }

                    catch (Exception e)
                    {
                        displayMessage("SaveCommand Error");
                    }
                    break;

                case "loadcommand":
                    if(saved)
                    {
                        try
                        {
                            File comoutputfile = new File(commandsEntered[1] + ".txt");
                            Scanner myReader = new Scanner(comoutputfile);
                            writeCommand = false;
                            while (myReader.hasNextLine()) {
                                String data = myReader.nextLine();
                                commands.add(data);
                                processCommand(data);
                            }

                            writeCommand = true;
                            myReader.close();
                            displayMessage("Command was successful");
                        }

                        catch (Exception e) {
                            displayMessage("Load Failed: File never existed!");
                        }
                    }

                    else
                    {
                        displayMessage("Warning: Current Image/commands was never saved!");
                        saveWarn = true;
                    }
                    break;

                case "black":
                    setPenColour(Color.black);
                    displayMessage("Pen colour set to black");
                    break;

                case "green":
                    setPenColour(Color.green);
                    displayMessage("Pen colour set to green");
                    break;

                case "red":
                    setPenColour(Color.red);
                    displayMessage("Pen colour set to red");
                    break;

                case "white":
                    setPenColour(Color.white);
                    displayMessage("Pen colour set to white");
                    break;

                case "rgb":
                    RGB(parseInt(commandsEntered[1]),parseInt(commandsEntered[2]),parseInt(commandsEntered[3]));
                    displayMessage("Pen colour set to RGB of choice");
                    break;

                case "reset":
                    reset();
                    turnLeft();
                    penDown();
                    displayMessage("Turtle returned to starting position pen's down");
                    break;

                case "clear":
                    if(saved)
                    {
                        clear();
                        commands = new ArrayList<String>();
                        displayMessage("Canvas cleared");
                    }

                    else
                    {
                        saveWarn = true;
                        displayMessage("Warning: Current Image/commands has not been saved" +
                                "Type 'confirm' if you wish to clear the frame");
                    }
                    break;

                case "confirm":
                    if(saveWarn)
                    {
                        clear();
                        saveWarn = false;
                        displayMessage("Command successful");
                    }
                    else
                    {
                        displayMessage("Confirmation not required");
                    }
                    break;
                default:
                    displayMessage("Invalid command");
                    invalid = true;
            }

            if(!invalid && !commandsEntered[0].equals("clear") && !commandsEntered[0].equals("loadcommand") && !commandsEntered[0].equals("savecommand") && !commandsEntered[0].equals("load") && !commandsEntered[0].equals("save") && writeCommand)
            {
                commands.add(command);
            }
        }
    }
}
