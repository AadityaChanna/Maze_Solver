import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;
import java.util.List; // resolves problem with java.awt.List and java.util.List

public class Picture extends SimplePicture
{
  public static int boxHeight;
  public static int boxWidth;
  public static boolean[][] visited;
  public static boolean[][] walls;
  public static boolean flag = false;
  public static int[][] sol;
  ///////////////////// constructors //////////////////////////////////

  /*
   * Constructor that takes no arguments
   */
  public Picture ()
  {
    /* not needed but use it to show students the implicit call to super()
     * child constructors always call a parent constructor
     */
    super();
  }

  /**
   * Constructor that takes a file name and creates the picture
   * @param fileName the name of the file to create the picture from
   */
  public Picture(String fileName)
  {
    // let the parent class handle this fileName
    super(fileName);
  }

  /**
   * Constructor that takes the width and height
   * @param height the height of the desired picture
   * @param width the width of the desired picture
   */
  public Picture(int height, int width)
  {
    // let the parent class handle this width and height
    super(width,height);
  }

  /**
   * Constructor that takes a picture and creates a
   * copy of that picture
   * @param copyPicture the picture to copy
   */
  public Picture(Picture copyPicture)
  {
    // let the parent class do the copy
    super(copyPicture);
  }

  /**
   * Constructor that takes a buffered image
   * @param image the buffered image to use
   */
  public Picture(BufferedImage image)
  {
    super(image);
  }

  ////////////////////// methods ///////////////////////////////////////

  /**
   * Method to return a string with information about this picture.
   * @return a string with information about the picture such as fileName,
   * height and width.
   */
  public String toString()
  {
    String output = "Picture, filename " + getFileName() +
            " height " + getHeight()
            + " width " + getWidth();
    return output;

  }

  public int getBoxHeight(){
    return this.boxHeight;
  }

  public int getBoxWidth(){
    return this.boxWidth;
  }



  public int[] findColor(Color c){
    Pixel[][] pixels = this.getPixels2D();
    int dimensions[] = new int[2];
    int leftCornerX = 0;
    int leftCornerY = 0;
    int rightCornerX = 0;
    int rightCornerY = 0;
    for (int i = pixels.length - 1; i > 0; i --)
    {
      for (int j = 0; j < pixels[0].length; j ++)
      {
        if(pixels[i][j].colorDistance(c) < 100){
          leftCornerX = i;
          leftCornerY = j;
          break;
        }
      }
    }

    int tempCoord = leftCornerX;
    while(pixels[tempCoord][leftCornerY].colorDistance(c) < 100){
      if(tempCoord == pixels.length - 1){
        rightCornerX = tempCoord;
        break;
      }
      rightCornerX = tempCoord;
      tempCoord ++;
    }

    tempCoord = leftCornerY;
    while(pixels[rightCornerX][tempCoord].colorDistance(c) < 100){
      if(tempCoord == pixels[0].length - 1){
        rightCornerY = tempCoord;
        break;
      }
      rightCornerY = tempCoord;
      tempCoord ++;
    }

    dimensions[0] = rightCornerX - leftCornerX;
    dimensions[1] = rightCornerY - leftCornerY;
    this.boxHeight = Math.abs(dimensions[0]) + 1;
    this.boxWidth = Math.abs(dimensions[1]) + 1;
    //System.out.println(leftCornerX + " , " + leftCornerY);
    //System.out.println(rightCornerX + " , " + rightCornerY);
    //System.out.println(this.boxWidth);
    //System.out.println(this.boxHeight);
    //System.out.println(pixels.length);
    //System.out.println(pixels[0].length);
    return dimensions;
  }







  public Color getSquare(int x, int y){
    Pixel[][] picture = this.getPixels2D();
    int red = 0;
    int blue = 0;
    int green = 0;
    int totalPixels = 0;
    int limitX = (x + 1) * this.boxHeight;
    int limitY = (y + 1) * this.boxWidth;
    for(int i = picture.length - 1 - (x * this.boxHeight); i > picture.length - 1 - (limitX); i --){
      for(int j = y * this.boxWidth; j < limitY; j ++){
        Pixel p = picture[i][j];
        red += p.getRed();
        blue += p.getBlue();
        green += p.getGreen();
        totalPixels ++;
      }
    }
    Color c = new Color(red/totalPixels, green/totalPixels, blue/totalPixels);
    return c;
  }

  public boolean similarTo(Color col, Color c){
    double distance = (c.getRed() - col.getRed())*(c.getRed() - col.getRed()) + (c.getGreen() - col.getGreen())*(c.getGreen() - col.getGreen()) + (c.getBlue() - col.getBlue())*(c.getBlue() - col.getBlue());
    if(distance < 50){
        return true;
    }else{
        return false;
    }
}

public int[][] toIntArr(){
   Pixel[][] pixels = this.getPixels2D();
   int[][] map = new int[pixels.length/boxHeight][pixels[0].length/boxWidth];

   for (int i = 0; i < pixels.length/boxHeight; i ++) {
     for (int j = 0; j < pixels[0].length/boxWidth; j++) {
       if (similarTo(getSquare(i,j), Color.RED)){
         map[i][j] = 3;
       }
       else if (similarTo(getSquare(i,j), Color.GREEN)){
         map[i][j] = 2;
       }
       else if (similarTo(getSquare(i,j), Color.WHITE)){
         map[i][j] = 1;
       }
       else if (similarTo(getSquare(i,j), Color.BLACK)){
         map[i][j] = 0;
       }

     }
   }

   return map;
 }


 public void run(int[][] map, int x, int y){
       if (x==map.length || y==map[0].length || x<0 || y<0 || visited[x][y]||map[x][y] == 0){
           return;
       }
//        if(map[x][y] == '+'){
//            //backtrack? add wall locations to another boolean [][] ?
//            //maybe j add to mega if statement & see if that does sumthin
//        }
       if(map[x][y] == 3){

           flag = true;
           sol[x][y] = 1;
           for(int i = 0; i < map.length; i ++){
             for(int j = 0; j < map[0].length; j ++){
               System.out.print(sol[i][j] + " ");
             }
             System.out.println();
           }
           return;
       }


       visited[x][y] = true;
       sol[x][y] = 1;
       run(map, x+1, y);
       run(map, x-1,y);
       run(map, x,y+1);
       run(map, x,y-1);
       sol[x][y] = 0;
   }

  /* Main method for testing - each class in Java can have a main
   * method
   */
  public static void main(String[] args)
  {
    Scanner console = new Scanner(System.in);
    System.out.println("Enter the name of the maze:");
    String name = console.next();
    Picture maze1 = new Picture(name);
    Color c = new Color(0,255,0);
    int[] dim = maze1.findColor(c);
    maze1.explore();
    c = maze1.getSquare(1,1);
    //System.out.println(c);
    //System.out.println(boxHeight + ", " + boxWidth);
    int[][] map = maze1.toIntArr();
    int[][] newMap = new int[map.length][map[0].length];
    int h = 0;
    for(int i = map.length - 1; i >= 0; i --){
      for(int j = 0; j < map[0].length; j ++){
        System.out.print(map[i][j] + " ");
        newMap[h][j] = map[i][j];
      }
      h ++;
      System.out.println();
    }
    System.out.println();
    System.out.println();
    System.out.println();
    visited = new boolean[map.length][map[0].length];
    walls = new boolean[map.length][map[0].length];
    int x = 0;
    int y = 0;
    sol = new int[map.length][map[0].length];
    for(int i = 0; i < newMap.length; i ++){
      for(int j = 0; j < newMap[0].length; j ++){
        if(newMap[i][j] == 2){
          x = i;
          y = j;
        }
        sol[i][j] = 0;
      }
    }

    maze1.run(newMap, x,y);
        if (flag){
            System.out.println("YES");
        }
        else {
            System.out.println("NO");
        }

  }
} // this } is the end of class Picture, put all new methods before this
