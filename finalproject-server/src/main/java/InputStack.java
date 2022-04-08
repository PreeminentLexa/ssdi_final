public class InputStack {
    private InputStack prev;
    private String input;
    public InputStack(String input){
        this.input = input;
    }
    public InputStack(InputStack prev, String input){
        this.prev = prev;
        this.input = input;
    }
    public boolean isBaseOfStack(){
        return null == this.prev;
    }
    public String getInput(){
        return this.input;
    }
    public InputStack getPrev(){
        return this.prev;
    }
}
