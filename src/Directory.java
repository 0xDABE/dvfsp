import java.util.ArrayList;

public class Directory extends Elem {

    private final ArrayList<Elem> elems = new ArrayList<>();

    public Directory(String name, Directory parent){
        super(name, true);
        this.setParent(parent);
    }

    public ArrayList<Elem> getElems(){
        return this.elems;
    }

    public boolean contains(Elem elem){
        for (Elem temp : elems)
            if (elem.getName().equals(temp.getName())) return true;
        return false;
    }

    public boolean contains(String elemName){
        for (Elem elem : elems)
            if (elem.getName().equals(elemName)) return true;
        return false;
    }

    public void add(Elem newElem) throws IllegalArgumentException{
        if (this.contains(newElem))
            throw new IllegalArgumentException("Elem \"" + newElem.getName() + "\" already exists");
        else
            elems.add(newElem);
    }

    public Elem find(String name){
        for(Elem elem : elems){
            if (elem.getName().equals(name)) return elem;
        }
        return null;
    }

    public void remove(Elem elem){
        if (!this.contains(elem))
            throw new IllegalArgumentException("Elem \"" + elem + "\" not exists");
        else
            elems.remove(elem);
    }

}
