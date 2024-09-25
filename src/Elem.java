import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Elem {

    private String name;
    private long size;

    private Directory parent;

    private final boolean isDirectory;

    Elem(String name, boolean isDirectory) throws IllegalArgumentException{
        if (isNameIllegal(name)) throw new IllegalArgumentException("Elem can't contain chars \",',* and /");
        this.name = name;
        this.isDirectory = isDirectory;
    }

    protected void setName(String newName) throws IllegalAccessException {
        if (isNameIllegal(newName))
            this.name = newName;
        else throw new IllegalAccessException("Elem can't contain chars \",',* and /");
    }

    protected String getName(){
        return this.name;
    }

    protected boolean isDirectory(){
        return this.isDirectory;
    }

    protected void setParent(Directory newParent){
        this.parent = newParent;
    }

    protected Directory getParent(){
        return this.parent;
    }

    public void printName(){
        if (this instanceof Directory){
            ColoredMessage.yellow(String.format(" [D] %-10s", this.getName()));
        }
        else {
            ColoredMessage.blue(String.format(" [F] %-10s", this.getName()));
        }
    }

    static boolean isNameIllegal(String name){
        String regex = "[\"'/*]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        return matcher.find();
    }

    static Elem getInstance(Elem oldElem){
        Elem newElem;
        if (oldElem.isDirectory()) {
            Directory dir = (Directory) oldElem;
            newElem = new Directory(oldElem.getName(), oldElem.getParent());
            Directory newElemAsDir = (Directory) newElem;
            for (Elem elem : dir.getElems()){
                newElemAsDir.add(Elem.getInstance(elem));
            }
        }
        else
            newElem = new DFile(oldElem.getName(), oldElem.getParent());


        return newElem;
    }

    // abstract public long getSize();
}
