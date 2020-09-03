package results;

public class Occurrence {
    private String file;
    private int startLine;
    private int endLine;
    private Occurrence linkedOccurrence;
    private boolean hasLink;

    public Occurrence(int startLine, int endLine){
        this("", startLine, endLine);
    }

    public Occurrence(String file, int startLine, int endLine){
        this.file = file;
        this.startLine = startLine;
        this.endLine = endLine;
        this.hasLink = false;
    }
    public Occurrence(Occurrence linkedOccurrence, String file, int startLine, int endLine) {
        this(file, startLine, endLine);
        this.linkedOccurrence = linkedOccurrence;
        hasLink = true;
    }

    public String getFile() { return file; }

    public int getStartLine(){
        return startLine;
    }

    public int getEndLine(){
        return endLine;
    }

    public Occurrence getLinkedOccurrence(){
        return hasLink ? linkedOccurrence : null;
    }

    public void setLinkedOccurrence(Occurrence occurrence){
        linkedOccurrence = occurrence;
        if(occurrence == null)
            hasLink = false;
        else
            hasLink = true;
    }

    public boolean hasLink(){ return hasLink; }
}