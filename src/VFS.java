public class VFS {
    private final Directory root;
    private Directory currentDirectory;

    public VFS(){
        this.root = new Directory("", null);
        this.currentDirectory = root;
    }

    public void touch(String call){
        String[] paths = Parce.parceArguments(call);
        if (paths.length == 1){
            ColoredMessage.redLn("Wrong \"touch\" usage");
            return;
        }
        for (int i = 1; i < paths.length; i++)
            addFile(paths[i]);
    }

    public void md(String call){
        String[] paths = Parce.parceArguments(call);
        if (paths.length == 1){
            ColoredMessage.redLn("Wrong \"md\" usage");
            return;
        }
        for (int i = 1; i < paths.length; i++)
            addDirectory(paths[i]);
    }

    public void rm(String call){
        String[] paths = Parce.parceArguments(call);
        if (paths.length == 1){
            ColoredMessage.redLn("Wrong \"rm\" usage");
            return;
        }
        for (int i = 1; i < paths.length; i++)
            removeElem(paths[i]);
    }

    public void cd(String call){
        String[] commands = Parce.parceArguments(call);
        if (call.trim().equals("cd"))
            gotoRoot();
        else
            if (commands.length > 2){
                ColoredMessage.redLn("Wrong \"cd\" usage");
            }
            else changeDirectory(commands[1]);
    }

    public void addDirectory(String name){
        String[] paths = name.split("/");
        String directoryPath;

        Directory orig = currentDirectory;

        if (name.startsWith("/"))
            directoryPath = name;
        else
            directoryPath = getCurrentAbsolutePath() +
                    name.substring(0, name.length() - currentDirectory.getName().length());

        currentDirectory = root;

        for (String path : paths){
            Elem elem = currentDirectory.find(path);
            if (elem != null && elem.isDirectory()){
                changeDirectory(elem.getName());
            }
            else
                if (elem != null && !elem.isDirectory())
                    ColoredMessage.redLn("Can't reach \"" + directoryPath + "\"");
                else {
                    if (path.isEmpty()) continue;
                    Directory newDirectory = new Directory(path, currentDirectory);
                    try {
                        if (currentDirectory != null)
                            this.currentDirectory.add(newDirectory);
                        else
                            ColoredMessage.redLn("Path \"" + directoryPath + "\" not exists");
                    }catch (IllegalArgumentException e){
                        ColoredMessage.redLn(e.getMessage());
                        return;
                    }
                    changeDirectory(path);
                }
        }

        currentDirectory = orig;
    }

    public void addFile(String name) {
        String[] paths = name.split("/");
        String fileName = paths[paths.length - 1];
        String directoryPath;

        Directory orig = currentDirectory;

        if (name.startsWith("/"))
            directoryPath = name.substring(0, name.length() - fileName.length());
        else
            directoryPath = getCurrentAbsolutePath() + name.substring(0, name.length() -
                    fileName.length() - currentDirectory.getName().length());

        currentDirectory = navigateToDirectory(directoryPath, false);

        if (currentDirectory != null) {
            DFile file = new DFile(fileName, currentDirectory);
            try {
                currentDirectory.add(file);
            } catch (IllegalArgumentException e) {
                ColoredMessage.redLn(e.getMessage());
                return;
            }
        }
        else
            ColoredMessage.redLn("Path \"" + directoryPath + "\" not exists");
        currentDirectory = orig;
    }


    public void removeElem(String name){
        Elem elem = getElemByPath(name);
        Directory directory = elem.getParent();
        directory.remove(elem);
    }


    public Elem getElemByPath(String path){
        String[] parts = path.split("/");
        Directory dir = path.startsWith("/") ? root : currentDirectory;

        Elem lastElem = null;

        for (String part : parts) {
            if (part.isEmpty() || part.equals("."))
                continue;
            if (part.equals("..")) {
                dir = dir.getParent();
                if (dir == null)
                    return null;
            } else {
                boolean found = false;
                for (Elem elem : dir.getElems()) {
                    if (elem.isDirectory() && elem.getName().equals(part)) {
                        dir = (Directory) elem;
                        lastElem = dir;
                        found = true;
                    }
                    else if (elem.getName().equals(part)){
                        found = true;
                        lastElem = (DFile) elem;
                    }
                }
                if (!found)
                    return null;
            }
        }
        return lastElem;
    }

    public String getCurrentAbsolutePath() {
        if (currentDirectory == root) return "/";
        StringBuilder path = new StringBuilder();
        Directory dir = currentDirectory;
        while (dir != null) {
            path.insert(0, dir.getName());
            if (dir.getParent() != null)
                path.insert(0, "/");
            dir = dir.getParent();
        }
        return path.toString();
    }

    public void changeDirectory(String path){
        Directory newDir = navigateToDirectory(path, false);
        if (newDir != null)
            this.currentDirectory = newDir;
        else
            ColoredMessage.redLn("Invalid directory path: " + path);
    }

    public void gotoRoot(){
        this.currentDirectory = this.root;
    }

    public Directory navigateToDirectory(String path, boolean IGNORE_NOT_EXIST_PATH){
        String[] parts = path.split("/");
        Directory dir = path.startsWith("/") ? root : currentDirectory;

        for (String part : parts) {
            if (part.isEmpty() || part.equals("."))
                continue;
            if (part.equals("..")) {
                dir = dir.getParent();
                if (dir == null)
                    ColoredMessage.redLn("Can't navigate above root directory");
            } else {
                boolean found = false;
                for (Elem elem : dir.getElems()) {
                    if (elem.isDirectory() && elem.getName().equals(part)) {
                        dir = (Directory) elem;
                        found = true;
                        break;
                    }
                }
                if (IGNORE_NOT_EXIST_PATH)
                    return dir;
                if (!found)
                    return null;
            }
        }
        return dir;
    }

    public void ls(){
        for (Elem elem : this.currentDirectory.getElems()){
            elem.printName();
            System.out.println();
        }
    }

}
