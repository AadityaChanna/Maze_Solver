public class StartMazeSolver{
  public static void main(String[] args) {
    try{
         Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"java Picture\"");
        }
        catch (Exception e){
            System.out.println("Something went wrong");
            e.printStackTrace();
        }
  }
}
